package com.waggle.global.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ManyToOne;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvService {

    private final EntityManager entityManager;

    public List<String[]> readCsv(String filePath) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            return reader.readAll();
        }
    }

    @Transactional
    public void insertCsvDataToDb(String filePath, Class<?> entityClass) {
        try {
            List<String[]> csvData = readCsv(filePath);
            if (csvData.isEmpty()) return;

            String[] headers = csvData.get(0); // CSV 헤더 가져오기
            List<Object> entities = new ArrayList<>();

            for (int i = 1; i < csvData.size(); i++) { // 첫 번째 행은 헤더이므로 제외
                String[] row = csvData.get(i);
                Object entity = entityClass.getDeclaredConstructor().newInstance();

                for (int j = 0; j < headers.length; j++) {
                    String fieldName = toCamelCase(headers[j].replaceAll("[\\uFEFF]", ""));
                    Field field = entityClass.getDeclaredField(fieldName);
                    field.setAccessible(true);

                    Object value;
                    if (field.isAnnotationPresent(ManyToOne.class)) {
                        Class<?> relatedEntity = field.getType();
                        Long foreignKey = Long.parseLong(row[j]);  // CSV 값 → Long 변환
                        value = entityManager.find(relatedEntity, foreignKey);  // 엔티티 조회
                    } else {
                        value = convertType(field.getType(), row[j]);  // 일반 필드 변환
                    }

                    field.set(entity, value);
                }

                entities.add(entity);
            }

            // 엔티티를 배치 저장
            for (Object entity : entities) {
                entityManager.persist(entity);
            }

            entityManager.flush();
            entityManager.clear();

        } catch (Exception e) {
            throw new RuntimeException("CSV 데이터를 DB에 삽입하는 중 오류 발생", e);
        }
    }

    private Object convertType(Class<?> fieldType, String value) {
        if (fieldType == Integer.class || fieldType == int.class) {
            return Integer.parseInt(value);
        } else if (fieldType == Long.class || fieldType == long.class) {
            return Long.parseLong(value);
        } else if (fieldType == Double.class || fieldType == double.class) {
            return Double.parseDouble(value);
        } else if (fieldType == Boolean.class || fieldType == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (fieldType == LocalDateTime.class) {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy.M.d H:m"));
        } else {
            return value; // 기본적으로 문자열로 저장
        }
    }

    private String toCamelCase(String snakeCase) {
        String[] parts = snakeCase.split("_");
        StringBuilder camelCase = new StringBuilder(parts[0]); // 첫 단어는 그대로 사용
        for (int i = 1; i < parts.length; i++) {
            camelCase.append(Character.toUpperCase(parts[i].charAt(0)))
                    .append(parts[i].substring(1));
        }
        return camelCase.toString();
    }
}

# 1단계: Gradle 빌드를 위한 환경 설정
FROM gradle:7.5.1-jdk17 AS build
WORKDIR /app

# 프로젝트 파일을 복사하고, 종속성만 먼저 다운로드
COPY . .
RUN ./gradlew dependencies

# 빌드 시 MySQL 관련 테스트를 제외하고 빌드
RUN ./gradlew clean build --no-daemon -x test  # 테스트 제외

# 2단계: 실행 환경을 위한 이미지 설정
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드 결과물 복사
COPY --from=build /app/build/libs/*.jar app.jar

# # 실행 시 필요한 MySQL 정보는 환경 변수로 주입
# ENV MYSQL_HOST=localhost
# ENV MYSQL_PORT=3306
# ENV MYSQL_USER=root
# ENV MYSQL_PASSWORD=root
# ENV MYSQL_DB=example

# MySQL 정보는 컨테이너 실행 시점에 전달
ENTRYPOINT ["java", "-jar", "app.jar"]
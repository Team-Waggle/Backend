package com.waggle.domain.user.service;

import com.waggle.domain.reference.entity.*;
import com.waggle.domain.user.dto.UserInputDto;
import com.waggle.domain.user.dto.UserJobDto;
import com.waggle.domain.user.dto.UserPortfolioUrlDto;
import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.repository.UserRepository;
import com.waggle.global.csv.CsvService;
import com.waggle.global.secure.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@TestPropertySource(locations = "file:./.env")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // 각 테스트 메소드 실행 후 DB 초기화
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CsvService csvService;

    @BeforeEach
    void setReferenceData() {
        csvService.insertCsvDataToDb("src/main/resources/reference_data/job_type.csv", Job.class);
        csvService.insertCsvDataToDb("src/main/resources/reference_data/dow_type.csv", DurationOfWorking.class);
        csvService.insertCsvDataToDb("src/main/resources/reference_data/industry_type.csv", Industry.class);
        csvService.insertCsvDataToDb("src/main/resources/reference_data/introduce_main_type.csv", MainIntroduce.class);
        csvService.insertCsvDataToDb("src/main/resources/reference_data/introduce_sub_type.csv", SubIntroduce.class);
        csvService.insertCsvDataToDb("src/main/resources/reference_data/sido_type.csv", Sido.class);
        csvService.insertCsvDataToDb("src/main/resources/reference_data/skill_type.csv", Skill.class);
        csvService.insertCsvDataToDb("src/main/resources/reference_data/portfolio_url_type.csv", PortfolioUrl.class);
        csvService.insertCsvDataToDb("src/main/resources/reference_data/wow_type.csv", WaysOfWorking.class);
        csvService.insertCsvDataToDb("src/main/resources/reference_data/tow_type.csv", TimeOfWorking.class);
        csvService.insertCsvDataToDb("src/main/resources/reference_data/week_days_type.csv", WeekDays.class);
    }

    @Test
    void 현재_사용자_조회() {
        // Given
        User user = User.builder()
                .provider("kakao")
                .providerId("12345")
                .name("Test User")
                .email("test@example.com")
                .profileImageUrl("http://example.com/profile.jpg")
                .build();
        userRepository.save(user);

        String token = jwtUtil.generateAccessToken(user.getId(), 3600000);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(createMockRequest(token)));

        // When
        User currentUser = userService.getCurrentUser();

        // Then
        assertNotNull(currentUser);
        assertThat(user.getId()).isEqualTo(currentUser.getId());
    }

    @Test
    void 사용자_수정_적용_확인() {
        // Given
        User user = User.builder()
                .provider("kakao")
                .providerId("12345")
                .name("Test User")
                .email("test@example.com")
                .profileImageUrl("http://example.com/profile.jpg")
                .build();
        userRepository.save(user);

        String token = jwtUtil.generateAccessToken(user.getId(), 3600000);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(createMockRequest(token)));

        // When
        Set<UserJobDto> jobs = new HashSet<>();
        jobs.add(new UserJobDto(1L, 5));

        Set<Long> industries = new HashSet<>();
        industries.add(1L);
        industries.add(3L);
        industries.add(5L);

        Set<Long> skills = new HashSet<>();
        skills.add(2L);
        skills.add(4L);
        skills.add(7L);

        Set<Long> preferWeekDays = new HashSet<>();
        preferWeekDays.add(1L);
        preferWeekDays.add(2L);
        preferWeekDays.add(3L);

        Set<Long> introduces = new HashSet<>();
        introduces.add(3L);
        introduces.add(13L);
        introduces.add(26L);
        introduces.add(31L);
        introduces.add(45L);

        Set<UserPortfolioUrlDto> portfolioUrls = new HashSet<>();
        portfolioUrls.add(new UserPortfolioUrlDto(1L, "http://example.com/portfolio1"));
        portfolioUrls.add(new UserPortfolioUrlDto(2L, "http://example.com/portfolio2"));

        UserInputDto userInputDto = UserInputDto.builder()
                .name("홍길동")
                .jobs(jobs)
                .industries(industries)
                .skills(skills)
                .preferWeekDays(preferWeekDays)
                .preferTowId(1L)
                .preferWowId(2L)
                .preferSidoId("11")
                .introduces(introduces)
                .detail("안녕하세요.")
                .portfolioUrls(portfolioUrls)
                .build();

        User updatedUser = userService.updateCurrentUser(null, userInputDto);

        // Then
        assertNotNull(updatedUser);
        User currentUser = userService.getCurrentUser();
        assertThat(updatedUser).isEqualTo(currentUser);

    }

    private MockHttpServletRequest createMockRequest(String token) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        return request;
    }
}
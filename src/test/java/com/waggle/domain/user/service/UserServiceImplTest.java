package com.waggle.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.waggle.domain.auth.service.AuthService;
import com.waggle.domain.reference.enums.Industry;
import com.waggle.domain.reference.enums.Skill;
import com.waggle.domain.user.UserInfo;
import com.waggle.domain.user.dto.UserInputDto;
import com.waggle.domain.user.dto.UserIntroductionDto;
import com.waggle.domain.user.dto.UserJobRoleDto;
import com.waggle.domain.user.dto.UserPortfolioDto;
import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.entity.UserDayOfWeek;
import com.waggle.domain.user.entity.UserIndustry;
import com.waggle.domain.user.entity.UserIntroduction;
import com.waggle.domain.user.entity.UserJobRole;
import com.waggle.domain.user.entity.UserPortfolio;
import com.waggle.domain.user.entity.UserSkill;
import com.waggle.domain.user.repository.UserDayOfWeekRepository;
import com.waggle.domain.user.repository.UserIndustryRepository;
import com.waggle.domain.user.repository.UserIntroductionRepository;
import com.waggle.domain.user.repository.UserJobRoleRepository;
import com.waggle.domain.user.repository.UserPortfolioRepository;
import com.waggle.domain.user.repository.UserRepository;
import com.waggle.domain.user.repository.UserSkillRepository;
import com.waggle.global.aws.service.S3Service;
import jakarta.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private AuthService authService;

    @Mock
    private S3Service s3Service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDayOfWeekRepository userDayOfWeekRepository;

    @Mock
    private UserIndustryRepository userIndustryRepository;

    @Mock
    private UserIntroductionRepository userIntroductionRepository;

    @Mock
    private UserJobRoleRepository userJobRoleRepository;

    @Mock
    private UserPortfolioRepository userPortfolioRepository;

    @Mock
    private UserSkillRepository userSkillRepository;

    @Mock
    private MultipartFile profileImage;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = User.builder()
            .id(userId)
            .email("test@example.com")
            .name("Test User")
            .build();
    }

    @Test
    @DisplayName("유저 ID로 유저 조회 - 성공")
    void getUserById_Success() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // When
        User result = userService.getUserById(userId);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Test User", result.getName());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("유저 ID로 유저 조회 - 실패 (유저 없음)")
    void getUserById_NotFound() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(nonExistentId));
        verify(userRepository).findById(nonExistentId);
    }

    @Test
    @DisplayName("유저 정보 조회")
    void getUserInfoByUser_Success() {
        // Given
        List<UserJobRole> jobRoles = new ArrayList<>();
        List<UserIndustry> industries = new ArrayList<>();
        List<UserSkill> skills = new ArrayList<>();
        List<UserDayOfWeek> daysOfWeek = new ArrayList<>();
        List<UserIntroduction> introductions = new ArrayList<>();
        List<UserPortfolio> portfolios = new ArrayList<>();

        when(userJobRoleRepository.findByUserId(userId)).thenReturn(jobRoles);
        when(userIndustryRepository.findByUserId(userId)).thenReturn(industries);
        when(userSkillRepository.findByUserId(userId)).thenReturn(skills);
        when(userDayOfWeekRepository.findByUserId(userId)).thenReturn(daysOfWeek);
        when(userIntroductionRepository.findByUserId(userId)).thenReturn(introductions);
        when(userPortfolioRepository.findByUserId(userId)).thenReturn(portfolios);

        // When
        UserInfo result = userService.getUserInfoByUser(testUser);

        // Then
        assertNotNull(result);
        assertEquals(testUser, result.user());
        assertEquals(jobRoles, result.userJobRoles());
        assertEquals(industries, result.userIndustries());
        assertEquals(skills, result.userSkills());
        assertEquals(daysOfWeek, result.userDaysOfWeek());
        assertEquals(introductions, result.userIntroductions());
        assertEquals(portfolios, result.userPortfolios());

        verify(userJobRoleRepository).findByUserId(userId);
        verify(userIndustryRepository).findByUserId(userId);
        verify(userSkillRepository).findByUserId(userId);
        verify(userDayOfWeekRepository).findByUserId(userId);
        verify(userIntroductionRepository).findByUserId(userId);
        verify(userPortfolioRepository).findByUserId(userId);
    }

    @Test
    @DisplayName("현재 유저 정보 업데이트")
    void updateCurrentUser_Success() {
        // Given
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(userRepository.getReferenceById(userId)).thenReturn(testUser);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(s3Service.getUrlFromFileName(anyString())).thenReturn("oldImageUrl");
        when(s3Service.isFileExist(anyString())).thenReturn(false);
        when(profileImage.isEmpty()).thenReturn(false);
        when(s3Service.uploadFile(any(MultipartFile.class), anyString())).thenReturn("newImageUrl");

        Set<UserJobRoleDto> jobRoles = Set.of(new UserJobRoleDto(null, 5));
        Set<Industry> industries = Set.of(Industry.FINANCE);
        Set<Skill> skills = Set.of(Skill.JAVA);
        Set<DayOfWeek> daysOfWeek = Set.of(DayOfWeek.MONDAY);
        UserIntroductionDto introduction = new UserIntroductionDto(null, null, null, null, null);
        Set<UserPortfolioDto> portfolios = Set.of(
            new UserPortfolioDto(null, "https://example.com"));

        UserInputDto inputDto = new UserInputDto(
            "Updated Name",
            jobRoles,
            industries,
            skills,
            daysOfWeek,
            null, // workTime
            null, // workWay
            null, // sido
            introduction,
            "Updated user detail information",
            portfolios
        );

        // When
        User result = userService.updateCurrentUser(profileImage, inputDto);

        // Then
        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(authService).getCurrentUser();
        verify(userRepository).save(testUser);
        verify(userJobRoleRepository).deleteByUserId(userId);
        verify(userIndustryRepository).deleteByUserId(userId);
        verify(userSkillRepository).deleteByUserId(userId);
        verify(userDayOfWeekRepository).deleteByUserId(userId);
        verify(userIntroductionRepository).deleteByUserId(userId);
        verify(userPortfolioRepository).deleteByUserId(userId);
        verify(userJobRoleRepository).saveAll(anyList());
        verify(userIndustryRepository).saveAll(anyList());
        verify(userSkillRepository).saveAll(anyList());
        verify(userDayOfWeekRepository).saveAll(anyList());
        verify(userPortfolioRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("현재 유저 삭제")
    void deleteCurrentUser_Success() throws Exception {
        // Given
        when(authService.getCurrentUser()).thenReturn(testUser);

        // 리플렉션을 사용해 private 필드 설정
        Field profileImageUrlField = User.class.getDeclaredField("profileImageUrl");
        profileImageUrlField.setAccessible(true);
        profileImageUrlField.set(testUser, "profileImageUrl");

        // When
        userService.deleteCurrentUser();

        // Then
        verify(authService).getCurrentUser();
        verify(s3Service).deleteFile("profileImageUrl");
        verify(userRepository).delete(testUser);
        verify(userJobRoleRepository).deleteByUserId(userId);
        verify(userIndustryRepository).deleteByUserId(userId);
        verify(userSkillRepository).deleteByUserId(userId);
        verify(userDayOfWeekRepository).deleteByUserId(userId);
        verify(userIntroductionRepository).deleteByUserId(userId);
        verify(userPortfolioRepository).deleteByUserId(userId);
    }
}

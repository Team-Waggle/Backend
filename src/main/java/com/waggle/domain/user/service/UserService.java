package com.waggle.domain.user.service;

import com.waggle.domain.reference.enums.Industry;
import com.waggle.domain.reference.enums.IntroductionType;
import com.waggle.domain.reference.enums.Skill;
import com.waggle.domain.user.UserInfo;
import com.waggle.domain.user.dto.UserInputDto;
import com.waggle.domain.user.dto.UserIntroductionDto;
import com.waggle.domain.user.dto.UserPortfolioDto;
import com.waggle.domain.user.dto.UserPositionDto;
import com.waggle.domain.user.entity.User;
import com.waggle.domain.user.entity.UserDayOfWeek;
import com.waggle.domain.user.entity.UserIndustry;
import com.waggle.domain.user.entity.UserIntroduction;
import com.waggle.domain.user.entity.UserPortfolio;
import com.waggle.domain.user.entity.UserPosition;
import com.waggle.domain.user.entity.UserSkill;
import com.waggle.domain.user.repository.UserDayOfWeekRepository;
import com.waggle.domain.user.repository.UserIndustryRepository;
import com.waggle.domain.user.repository.UserIntroductionRepository;
import com.waggle.domain.user.repository.UserPortfolioRepository;
import com.waggle.domain.user.repository.UserPositionRepository;
import com.waggle.domain.user.repository.UserRepository;
import com.waggle.domain.user.repository.UserSkillRepository;
import com.waggle.global.aws.service.S3Service;
import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final UserDayOfWeekRepository userDayOfWeekRepository;
    private final UserIndustryRepository userIndustryRepository;
    private final UserIntroductionRepository userIntroductionRepository;
    private final UserPositionRepository userPositionRepository;
    private final UserPortfolioRepository userPortfolioRepository;
    private final UserSkillRepository userSkillRepository;

    @Transactional(readOnly = true)
    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

    @Transactional(readOnly = true)
    public UserInfo getUserInfoByUser(User user) {
        List<UserPosition> userPositions = userPositionRepository.findByUserId(user.getId());
        List<UserIndustry> userIndustries = userIndustryRepository.findByUserId(user.getId());
        List<UserSkill> userSkills = userSkillRepository.findByUserId(user.getId());
        List<UserDayOfWeek> userDaysOfWeek = userDayOfWeekRepository.findByUserId(user.getId());
        List<UserIntroduction> userIntroductions =
            userIntroductionRepository.findByUserId(user.getId());
        List<UserPortfolio> userPortfolios = userPortfolioRepository.findByUserId(user.getId());

        return UserInfo.of(
            user,
            userPositions,
            userIndustries,
            userSkills,
            userDaysOfWeek,
            userIntroductions,
            userPortfolios
        );
    }

    @Transactional
    public User updateUser(MultipartFile profileImage, UserInputDto userInputDto, User user) {
        user.update(
            userInputDto.name(),
            getProfileImageUrl(profileImage, user),
            userInputDto.workTime(),
            userInputDto.workWay(),
            userInputDto.sido(),
            userInputDto.detail()
        );

        updateUserPositions(user.getId(), userInputDto.positions());
        updateUserIndustries(user.getId(), userInputDto.industries());
        updateUserSkills(user.getId(), userInputDto.skills());
        updateUserDaysOfWeek(user.getId(), userInputDto.daysOfWeek());
        updateUserIntroduction(user.getId(), userInputDto.introduction());
        updateUserPortfolios(user.getId(), userInputDto.portfolios());

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(User user) {
        s3Service.deleteFile(user.getProfileImageUrl());
        userRepository.delete(user);
        userPositionRepository.deleteByUserId(user.getId());
        userIndustryRepository.deleteByUserId(user.getId());
        userSkillRepository.deleteByUserId(user.getId());
        userDayOfWeekRepository.deleteByUserId(user.getId());
        userIntroductionRepository.deleteByUserId(user.getId());
        userPortfolioRepository.deleteByUserId(user.getId());
    }

    private void updateUserPositions(UUID userId, Set<UserPositionDto> userPositions) {
        if (userPositions == null) {
            return;
        }

        userPositionRepository.deleteByUserId(userId);

        User user = userRepository.getReferenceById(userId);
        List<UserPosition> entities = userPositions.stream()
            .map(dto -> UserPosition.builder()
                .user(user)
                .position(dto.position())
                .yearCount(dto.yearCount())
                .build())
            .toList();

        userPositionRepository.saveAll(entities);
    }

    private void updateUserIndustries(UUID userId, Set<Industry> industries) {
        if (industries == null) {
            return;
        }

        userIndustryRepository.deleteByUserId(userId);

        User user = userRepository.getReferenceById(userId);
        List<UserIndustry> entities = industries.stream()
            .map(industry -> UserIndustry.builder()
                .user(user)
                .industry(industry)
                .build())
            .toList();

        userIndustryRepository.saveAll(entities);
    }

    private void updateUserSkills(UUID userId, Set<Skill> skills) {
        if (skills == null) {
            return;
        }

        userSkillRepository.deleteByUserId(userId);

        User user = userRepository.getReferenceById(userId);
        List<UserSkill> entities = skills.stream()
            .map(skill -> UserSkill.builder()
                .user(user)
                .skill(skill)
                .build())
            .toList();

        userSkillRepository.saveAll(entities);
    }

    private void updateUserDaysOfWeek(UUID userId, Set<DayOfWeek> dayOfWeeks) {
        if (dayOfWeeks == null) {
            return;
        }

        userDayOfWeekRepository.deleteByUserId(userId);

        User user = userRepository.getReferenceById(userId);
        List<UserDayOfWeek> entities = dayOfWeeks.stream()
            .map(dayOfWeek -> UserDayOfWeek.builder()
                .user(user)
                .dayOfWeek(dayOfWeek)
                .build())
            .toList();

        userDayOfWeekRepository.saveAll(entities);
    }

    private void updateUserIntroduction(UUID userId, UserIntroductionDto introduction) {
        if (introduction == null) {
            return;
        }

        userIntroductionRepository.deleteByUserId(userId);

        User user = userRepository.getReferenceById(userId);
        List<UserIntroduction> entities = new ArrayList<>();

        if (introduction.communicationStyles() != null) {
            introduction.communicationStyles().forEach(style ->
                entities.add(UserIntroduction.builder()
                    .user(user)
                    .introductionType(IntroductionType.COMMUNICATION_STYLE)
                    .subIntroduction(style.name())
                    .build()
                )
            );
        }

        if (introduction.collaborationStyles() != null) {
            introduction.collaborationStyles().forEach(style ->
                entities.add(UserIntroduction.builder()
                    .user(user)
                    .introductionType(IntroductionType.COLLABORATION_STYLE)
                    .subIntroduction(style.name())
                    .build()
                )
            );
        }

        if (introduction.workStyles() != null) {
            introduction.workStyles().forEach(style ->
                entities.add(UserIntroduction.builder()
                    .user(user)
                    .introductionType(IntroductionType.WORK_STYLE)
                    .subIntroduction(style.name())
                    .build()
                )
            );
        }

        if (introduction.problemSolvingApproaches() != null) {
            introduction.problemSolvingApproaches().forEach(approach ->
                entities.add(UserIntroduction.builder()
                    .user(user)
                    .introductionType(IntroductionType.PROBLEM_SOLVING_APPROACH)
                    .subIntroduction(approach.name())
                    .build()
                )
            );
        }

        if (introduction.mbti() != null) {
            entities.add(UserIntroduction.builder()
                .user(user)
                .introductionType(IntroductionType.MBTI)
                .subIntroduction(introduction.mbti().name())
                .build());
        }

        userIntroductionRepository.saveAll(entities);
    }

    private void updateUserPortfolios(UUID userId, Set<UserPortfolioDto> portfolios) {
        if (portfolios == null) {
            return;
        }

        userPortfolioRepository.deleteByUserId(userId);

        User user = userRepository.getReferenceById(userId);
        List<UserPortfolio> entities = portfolios.stream()
            .map(dto -> UserPortfolio.builder()
                .user(user)
                .portfolioType(dto.portfolioType())
                .url(dto.url())
                .build())
            .toList();

        userPortfolioRepository.saveAll(entities);
    }

    private String getProfileImageUrl(MultipartFile profileImage, User user) {
        String checkUrl = s3Service.getUrlFromFileName("user/profile_img/" + user.getId());
        if (s3Service.isFileExist(checkUrl)) {
            s3Service.deleteFile(checkUrl);
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            if (user.getProfileImageUrl() != null) {
                s3Service.deleteFile(user.getProfileImageUrl());
            }

            return s3Service.uploadFile(profileImage, "user/profile_img/" + user.getId());
        }
        return null;
    }
}

package com.waggle.domain.user.service;

import com.waggle.domain.reference.entity.*;
import com.waggle.domain.reference.service.ReferenceService;
import com.waggle.domain.user.dto.UpdateUserDto;
import com.waggle.domain.user.entity.*;
import com.waggle.domain.user.repository.UserRepository;
import com.waggle.global.aws.service.S3Service;
import com.waggle.global.exception.JwtTokenException;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.secure.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ReferenceService referenceService;
    private final S3Service s3Service;

    @Override
    public User getCurrentUser() {
        String authorizationHeader = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getHeader("Authorization");

        String token = jwtUtil.getTokenFromHeader(authorizationHeader);

        String userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            throw new JwtTokenException(ApiStatus._INVALID_ACCESS_TOKEN);
        }

        return userRepository.findByUserId(UUID.fromString(userId))
                .orElseThrow(() -> new JwtTokenException(ApiStatus._INVALID_ACCESS_TOKEN));
    }

    @Override
    public User updateUser(MultipartFile profileImage, UpdateUserDto updateUserDto) {
        User user = getCurrentUser();
        user.clearInfo();

        user.setProfileImageUrl(getProfileImageUrl(profileImage, user));
        user.setName(updateUserDto.getName());
        user.setUserJobs(getUserJobs(updateUserDto, user));
        user.setUserIndustries(getUserIndustries(updateUserDto, user));
        user.setUserSkills(getUserSkills(updateUserDto, user));
        user.setUserWeekDays(getUserWeekDays(updateUserDto, user));
        user.setPreferTow(referenceService.getTimeOfWorkingById(updateUserDto.getPreferTowId()));
        user.setPreferWow(referenceService.getWaysOfWorkingById(updateUserDto.getPreferWowId()));
        user.setPreferSido(referenceService.getSidoesById(updateUserDto.getPreferSidoId()));
        user.setUserIntroduces(getIntroduces(updateUserDto, user));
        user.setDetail(updateUserDto.getDetail());
        user.setUserPortfolioUrls(getUserPortfolioUrls(updateUserDto, user));

        return userRepository.save(user);
    }

    @Override
    public void deleteUser() {
        User user = getCurrentUser();
        userRepository.delete(user);
    }

    private Set<UserJob> getUserJobs(UpdateUserDto updateUserDto, User user) {
        Set<UserJob> userJobs = new HashSet<>();
        updateUserDto.getJobs().forEach(userJobDto -> {
            Job job = referenceService.getJobById(userJobDto.getJobId());
            UserJob userJob = UserJob.builder()
                    .job(job)
                    .user(user)
                    .yearCnt(userJobDto.getYearCnt())
                    .build();
            userJobs.add(userJob);
        });
        return userJobs;
    }

    private Set<UserIndustry> getUserIndustries(UpdateUserDto updateUserDto, User user) {
        Set<UserIndustry> userIndustries = new HashSet<>();
        updateUserDto.getIndustries().forEach(industryId -> {
            Industry industry = referenceService.getIndustryById(industryId);
            UserIndustry userIndustry = UserIndustry.builder()
                    .industry(industry)
                    .user(user)
                    .build();
            userIndustries.add(userIndustry);
        });
        return userIndustries;
    }

    private Set<UserSkill> getUserSkills(UpdateUserDto updateUserDto, User user) {
        Set<UserSkill> userSkills = new HashSet<>();
        updateUserDto.getSkills().forEach(skillId -> {
            Skill skill = referenceService.getSkillById(skillId);
            UserSkill userSkill = UserSkill.builder()
                    .skill(skill)
                    .user(user)
                    .build();
            userSkills.add(userSkill);
        });
        return userSkills;
    }

    private Set<UserWeekDays> getUserWeekDays(UpdateUserDto updateUserDto, User user) {
        Set<UserWeekDays> userWeekDays = new HashSet<>();
        updateUserDto.getPreferWeekDays().forEach(userWeekDayId -> {
            WeekDays weekDays = referenceService.getWeekDaysById(userWeekDayId);
            UserWeekDays userWeekDay = UserWeekDays.builder()
                    .user(user)
                    .weekDays(weekDays)
                    .build();
            userWeekDays.add(userWeekDay);
        });
        return userWeekDays;
    }

    private Set<UserPortfolioUrl> getUserPortfolioUrls(UpdateUserDto updateUserDto, User user) {
        Set<UserPortfolioUrl> userPortfolioUrls = new HashSet<>();
        updateUserDto.getPortfolioUrls().forEach(portfolioUrlDto -> {
            PortfolioUrl portfolioUrl = referenceService.getPortfolioUrlById(portfolioUrlDto.getPortfolioUrlId());
            UserPortfolioUrl userPortfolioUrl = UserPortfolioUrl.builder()
                    .portfolioUrl(portfolioUrl)
                    .user(user)
                    .url(portfolioUrlDto.getUrl())
                    .build();
            userPortfolioUrls.add(userPortfolioUrl);
        });
        return userPortfolioUrls;
    }

    private Set<UserIntroduce> getIntroduces(UpdateUserDto updateUserDto, User user) {
        Set<UserIntroduce> introduces = new HashSet<>();
        updateUserDto.getIntroduces().forEach(introduceId -> {
            SubIntroduce introduce = referenceService.getSubIntroduceById(introduceId);
            UserIntroduce userIntroduce = UserIntroduce.builder()
                    .user(user)
                    .subIntroduce(introduce)
                    .build();
            introduces.add(userIntroduce);
        });
        return introduces;
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

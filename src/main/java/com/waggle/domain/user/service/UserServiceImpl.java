package com.waggle.domain.user.service;

import com.waggle.domain.project.repository.ProjectRepository;
import com.waggle.domain.reference.entity.*;
import com.waggle.domain.reference.service.ReferenceService;
import com.waggle.domain.user.dto.UserInputDto;
import com.waggle.domain.user.entity.*;
import com.waggle.domain.user.repository.UserRepository;
import com.waggle.global.aws.service.S3Service;
import com.waggle.global.exception.JwtTokenException;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.secure.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
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
    @Transactional
    public User updateCurrentUser(MultipartFile profileImage, UserInputDto userInputDto) {
        User user = getCurrentUser();
        //user.clearInfo();

        user.setProfileImageUrl(getProfileImageUrl(profileImage, user));
        user.setName(userInputDto.getName());
        setUserJobs(userInputDto, user);
        setUserIndustries(userInputDto, user);
        setUserSkills(userInputDto, user);
        setUserWeekDays(userInputDto, user);
        user.setPreferTow(referenceService.getTimeOfWorkingById(userInputDto.getPreferTowId()));
        user.setPreferWow(referenceService.getWaysOfWorkingById(userInputDto.getPreferWowId()));
        user.setPreferSido(referenceService.getSidoesById(userInputDto.getPreferSidoId()));
        setIntroduces(userInputDto, user);
        user.setDetail(userInputDto.getDetail());
        setUserPortfolioUrls(userInputDto, user);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteCurrentUser() {
        User user = getCurrentUser();
        s3Service.deleteFile(user.getProfileImageUrl());
        userRepository.delete(user);
    }

    @Override
    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(UUID.fromString(userId))
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    private void setUserJobs(UserInputDto userInputDto, User user) {
        user.getUserJobs().clear();
        userInputDto.getJobs().forEach(userJobDto -> {
            Job job = referenceService.getJobById(userJobDto.getJobId());
            UserJob userJob = UserJob.builder()
                    .job(job)
                    .user(user)
                    .yearCnt(userJobDto.getYearCnt())
                    .build();
            user.getUserJobs().add(userJob);
        });
    }

    private void setUserIndustries(UserInputDto userInputDto, User user) {
        user.getUserIndustries().clear();
        userInputDto.getIndustries().forEach(industryId -> {
            Industry industry = referenceService.getIndustryById(industryId);
            UserIndustry userIndustry = UserIndustry.builder()
                    .industry(industry)
                    .user(user)
                    .build();
            user.getUserIndustries().add(userIndustry);
        });
    }

    private void setUserSkills(UserInputDto userInputDto, User user) {
        user.getUserSkills().clear();
        userInputDto.getSkills().forEach(skillId -> {
            Skill skill = referenceService.getSkillById(skillId);
            UserSkill userSkill = UserSkill.builder()
                    .skill(skill)
                    .user(user)
                    .build();
            user.getUserSkills().add(userSkill);
        });
    }

    private void setUserWeekDays(UserInputDto userInputDto, User user) {
        user.getUserWeekDays().clear();
        userInputDto.getPreferWeekDays().forEach(userWeekDayId -> {
            WeekDays weekDays = referenceService.getWeekDaysById(userWeekDayId);
            UserWeekDays userWeekDay = UserWeekDays.builder()
                    .user(user)
                    .weekDays(weekDays)
                    .build();
            user.getUserWeekDays().add(userWeekDay);
        });
    }

    private void setUserPortfolioUrls(UserInputDto userInputDto, User user) {
        user.getUserPortfolioUrls().clear();
        userInputDto.getPortfolioUrls().forEach(portfolioUrlDto -> {
            PortfolioUrl portfolioUrl = referenceService.getPortfolioUrlById(portfolioUrlDto.getPortfolioUrlId());
            UserPortfolioUrl userPortfolioUrl = UserPortfolioUrl.builder()
                    .portfolioUrl(portfolioUrl)
                    .user(user)
                    .url(portfolioUrlDto.getUrl())
                    .build();
            user.getUserPortfolioUrls().add(userPortfolioUrl);
        });
    }

    private void setIntroduces(UserInputDto userInputDto, User user) {
        userInputDto.getIntroduces().forEach(introduceId -> {
            SubIntroduce introduce = referenceService.getSubIntroduceById(introduceId);
            UserIntroduce userIntroduce = UserIntroduce.builder()
                    .user(user)
                    .subIntroduce(introduce)
                    .build();
            user.getUserIntroduces().add(userIntroduce);
        });
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

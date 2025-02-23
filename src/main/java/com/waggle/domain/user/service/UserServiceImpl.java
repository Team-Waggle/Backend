package com.waggle.domain.user.service;

import com.waggle.domain.project.entity.Project;
import com.waggle.domain.project.entity.ProjectApplicant;
import com.waggle.domain.project.entity.ProjectBookmark;
import com.waggle.domain.project.entity.ProjectMember;
import com.waggle.domain.project.repository.ProjectRepository;
import com.waggle.domain.reference.entity.*;
import com.waggle.domain.reference.service.ReferenceService;
import com.waggle.domain.user.dto.UserInputDto;
import com.waggle.domain.user.entity.*;
import com.waggle.domain.user.repository.UserRepository;
import com.waggle.global.aws.service.S3Service;
import com.waggle.global.exception.JwtTokenException;
import com.waggle.global.exception.ProjectException;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.secure.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

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
    public User updateCurrentUser(MultipartFile profileImage, UserInputDto userInputDto) {
        User user = getCurrentUser();
        user.clearInfo();

        user.setProfileImageUrl(getProfileImageUrl(profileImage, user));
        user.setName(userInputDto.getName());
        user.setUserJobs(getUserJobs(userInputDto, user));
        user.setUserIndustries(getUserIndustries(userInputDto, user));
        user.setUserSkills(getUserSkills(userInputDto, user));
        user.setUserWeekDays(getUserWeekDays(userInputDto, user));
        user.setPreferTow(referenceService.getTimeOfWorkingById(userInputDto.getPreferTowId()));
        user.setPreferWow(referenceService.getWaysOfWorkingById(userInputDto.getPreferWowId()));
        user.setPreferSido(referenceService.getSidoesById(userInputDto.getPreferSidoId()));
        user.setUserIntroduces(getIntroduces(userInputDto, user));
        user.setDetail(userInputDto.getDetail());
        user.setUserPortfolioUrls(getUserPortfolioUrls(userInputDto, user));

        return userRepository.save(user);
    }

    @Override
    public void deleteCurrentUser() {
        User user = getCurrentUser();
        s3Service.deleteFile(user.getProfileImageUrl());
        userRepository.delete(user);
    }

    @Override
    public Set<Project> getCurrentUserProjects() {
        User user = getCurrentUser();
        return user.getProjectMembers().stream()
                .map(ProjectMember::getProject)
                .sorted(Comparator.comparing(Project::getCreatedAt).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public boolean toggleCurrentUserBookmark(String projectId) {
        User user = getCurrentUser();
        Project project = projectRepository.findById(UUID.fromString(projectId))
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        boolean isBookmarked;

        if (user.getProjectBookmarks().stream().anyMatch(projectBookmark -> projectBookmark.getProject().getId().equals(project.getId()))) {
            user.getProjectBookmarks().removeIf(projectBookmark -> projectBookmark.getProject().getId().equals(project.getId()));
            project.setBookmarkCnt(project.getBookmarkCnt() - 1);
            isBookmarked = false;
        } else {
            user.getProjectBookmarks().add(ProjectBookmark.builder()
                    .project(project)
                    .user(user)
                    .build());
            project.setBookmarkCnt(project.getBookmarkCnt() + 1);
            isBookmarked = true;
        }

        userRepository.save(user);
        projectRepository.save(project);

        return isBookmarked;
    }

    @Override
    public Set<Project> getCurrentUserBookmarkProjects() {
        User user = getCurrentUser();
        return user.getProjectBookmarks().stream()
                .map(ProjectBookmark::getProject)
                .sorted(Comparator.comparing(Project::getCreatedAt).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(UUID.fromString(userId))
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    public Set<Project> getUserProjects(String userId) {
        User user = getUserByUserId(userId);
        return user.getProjectMembers().stream()
                .map(ProjectMember::getProject)
                .sorted(Comparator.comparing(Project::getCreatedAt).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public void deleteUserProject(String projectId) {
        User user = getCurrentUser();
        Project project = projectRepository.findById(UUID.fromString(projectId))
                .orElseThrow(() -> new EmptyResultDataAccessException(1));

        if (!project.getProjectMembers().stream().anyMatch(projectMember -> projectMember.getUser().getId().equals(user.getId()))) {
            throw new ProjectException(ApiStatus._NOT_JOINED_PROJECT);
        }

        project.getProjectMembers().removeIf(projectMember -> projectMember.getUser().getId().equals(user.getId()));
        projectRepository.save(project);
    }

    @Override
    public Set<Project> getUserBookmarkProjects(String userId) {
        User user = getUserByUserId(userId);
        return user.getProjectBookmarks().stream()
                .map(ProjectBookmark::getProject)
                .sorted(Comparator.comparing(Project::getCreatedAt).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Project applyProject(String projectId) {
        User user = getCurrentUser();
        Project project = projectRepository.findById(UUID.fromString(projectId))
                .orElseThrow(() -> new EmptyResultDataAccessException(1));

        if (project.getProjectMembers().stream().anyMatch(projectMember -> projectMember.getUser().getId().equals(user.getId()))) {
            throw new ProjectException(ApiStatus._ALREADY_JOINED_PROJECT);
        }

        if (project.getProjectApplicants().stream().anyMatch(projectMember -> projectMember.getUser().getId().equals(user.getId()))) {
            throw new ProjectException(ApiStatus._ALREADY_APPLIED_PROJECT);
        }

        project.getProjectApplicants().add(ProjectApplicant.builder()
                .project(project)
                .user(user)
                .build());

        projectRepository.save(project);
        return project;
    }

    @Override
    public void cancelApplyProject(String projectId) {
        User user = getCurrentUser();
        Project project = projectRepository.findById(UUID.fromString(projectId))
                .orElseThrow(() -> new EmptyResultDataAccessException(1));

        project.getProjectApplicants().removeIf(projectApplicant -> projectApplicant.getUser().getId().equals(user.getId()));
        projectRepository.save(project);
    }

    @Override
    public Set<Project> getAppliedProjects() {
        User user = getCurrentUser();
        return user.getProjectApplicants().stream()
                .sorted(Comparator.comparing(ProjectApplicant::getAppliedAt).reversed())
                .map(ProjectApplicant::getProject)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<UserJob> getUserJobs(UserInputDto userInputDto, User user) {
        Set<UserJob> userJobs = new HashSet<>();
        userInputDto.getJobs().forEach(userJobDto -> {
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

    private Set<UserIndustry> getUserIndustries(UserInputDto userInputDto, User user) {
        Set<UserIndustry> userIndustries = new HashSet<>();
        userInputDto.getIndustries().forEach(industryId -> {
            Industry industry = referenceService.getIndustryById(industryId);
            UserIndustry userIndustry = UserIndustry.builder()
                    .industry(industry)
                    .user(user)
                    .build();
            userIndustries.add(userIndustry);
        });
        return userIndustries;
    }

    private Set<UserSkill> getUserSkills(UserInputDto userInputDto, User user) {
        Set<UserSkill> userSkills = new HashSet<>();
        userInputDto.getSkills().forEach(skillId -> {
            Skill skill = referenceService.getSkillById(skillId);
            UserSkill userSkill = UserSkill.builder()
                    .skill(skill)
                    .user(user)
                    .build();
            userSkills.add(userSkill);
        });
        return userSkills;
    }

    private Set<UserWeekDays> getUserWeekDays(UserInputDto userInputDto, User user) {
        Set<UserWeekDays> userWeekDays = new HashSet<>();
        userInputDto.getPreferWeekDays().forEach(userWeekDayId -> {
            WeekDays weekDays = referenceService.getWeekDaysById(userWeekDayId);
            UserWeekDays userWeekDay = UserWeekDays.builder()
                    .user(user)
                    .weekDays(weekDays)
                    .build();
            userWeekDays.add(userWeekDay);
        });
        return userWeekDays;
    }

    private Set<UserPortfolioUrl> getUserPortfolioUrls(UserInputDto userInputDto, User user) {
        Set<UserPortfolioUrl> userPortfolioUrls = new HashSet<>();
        userInputDto.getPortfolioUrls().forEach(portfolioUrlDto -> {
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

    private Set<UserIntroduce> getIntroduces(UserInputDto userInputDto, User user) {
        Set<UserIntroduce> introduces = new HashSet<>();
        userInputDto.getIntroduces().forEach(introduceId -> {
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

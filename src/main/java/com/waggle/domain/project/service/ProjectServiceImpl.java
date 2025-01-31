package com.waggle.domain.project.service;

import com.waggle.domain.project.dto.ProjectDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.project.entity.ProjectJob;
import com.waggle.domain.project.entity.ProjectSkill;
import com.waggle.domain.project.entity.ProjectUser;
import com.waggle.domain.project.repository.ProjectRepository;
import com.waggle.domain.reference.service.ReferenceService;
import com.waggle.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final ReferenceService referenceService;

    @Override
    public Project create(ProjectDto projectDto) {
        Project newProject = Project.builder()
                .title(projectDto.getTitle())
                .industry(referenceService.getIndustryById(projectDto.getIndustryId()))
                .waysOfWorking(referenceService.getWaysOfWorkingById(projectDto.getWayOfWorkingId()))
                .recruitmentDate(projectDto.getRecruitmentDate())
                .durationOfWorking(referenceService.getDurationOfWorkingById(projectDto.getDurationOfWorkingId()))
                .detail(projectDto.getDetail())
                .connectUrl(projectDto.getConnectUrl())
                .referenceUrl(projectDto.getReferenceUrl())
                .bookmarkCnt(0)
                .build();
        newProject = projectRepository.save(newProject);

        Set<ProjectUser> projectUsers = new HashSet<>();
        projectUsers.add(ProjectUser.builder()
                .project(newProject)
                .user(userService.getCurrentUser())
                .build());
        newProject.setProjectUsers(projectUsers);
        newProject.setProjectJobs(getProjectJobs(projectDto, newProject));
        newProject.setProjectSkills(getProjectSkills(projectDto, newProject));
        projectRepository.save(newProject);

        return newProject;
    }

    @Override
    public Project update(UUID id, ProjectDto projectDto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        project.setTitle(projectDto.getTitle());
        project.setIndustry(referenceService.getIndustryById(projectDto.getIndustryId()));
        project.setWaysOfWorking(referenceService.getWaysOfWorkingById(projectDto.getWayOfWorkingId()));
        project.setRecruitmentDate(projectDto.getRecruitmentDate());
        project.setDurationOfWorking(referenceService.getDurationOfWorkingById(projectDto.getDurationOfWorkingId()));
        project.setProjectJobs(getProjectJobs(projectDto, project));
        project.setProjectSkills(getProjectSkills(projectDto, project));
        project.setDetail(projectDto.getDetail());
        project.setConnectUrl(projectDto.getConnectUrl());
        project.setReferenceUrl(projectDto.getReferenceUrl());
        projectRepository.save(project);
        return project;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        //id가 아닌 다른 기준으로 값을 삭제할 때
        /*Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        projectRepository.delete(project);*/

        //id 기준
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        projectRepository.delete(project);
    }

    @Override
    public Project findById(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    private Set<ProjectJob> getProjectJobs(ProjectDto projectDto, Project project) {
        Set<ProjectJob> projectJobs = new HashSet<>();
        projectDto.getJobs().forEach(jobDto -> {
            ProjectJob projectJob = ProjectJob.builder()
                    .project(project)
                    .job(referenceService.getJobById(jobDto.getJobId()))
                    .recruitmentCnt(jobDto.getRecruitmentCnt())
                    .build();
            projectJobs.add(projectJob);
        });
        return projectJobs;
    }

    private Set<ProjectSkill> getProjectSkills(ProjectDto projectDto, Project project) {
        Set<ProjectSkill> projectSkills = new HashSet<>();
        projectDto.getSkillIds().forEach(skillId -> {
            ProjectSkill projectSkill = ProjectSkill.builder()
                    .project(project)
                    .skill(referenceService.getSkillById(skillId))
                    .build();
            projectSkills.add(projectSkill);
        });
        return projectSkills;
    }
}

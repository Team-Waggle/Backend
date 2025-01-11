package com.waggle.domain.project.service;

import com.waggle.domain.project.dto.CreateProjectDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;

    @Override
    public Project create(CreateProjectDto createProjectDto) {
        Project newProject = Project.builder()
                .title(createProjectDto.getTitle())
                .recruitmentDate(createProjectDto.getRecruitmentDate())
                .detail(createProjectDto.getDetail())
                .connectUrl(createProjectDto.getConnectUrl())
                .referenceUrl(createProjectDto.getReferenceUrl())
                .bookmarkCnt(0)
                .build();
        projectRepository.save(newProject);
        return newProject;
    }
}

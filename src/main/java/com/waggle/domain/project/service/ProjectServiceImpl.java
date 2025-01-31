package com.waggle.domain.project.service;

import com.waggle.domain.project.dto.ProjectDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;

    @Override
    public Project create(ProjectDto projectDto) {
        Project newProject = Project.builder()
                .title(projectDto.getTitle())
                .recruitmentDate(projectDto.getRecruitmentDate())
                .detail(projectDto.getDetail())
                .connectUrl(projectDto.getConnectUrl())
                .referenceUrl(projectDto.getReferenceUrl())
                .bookmarkCnt(0)
                .build();
        projectRepository.save(newProject);
        return newProject;
    }

    @Override
    public Project update(UUID id, ProjectDto projectDto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        project.setTitle(projectDto.getTitle());
        project.setDetail(projectDto.getDetail());
        project.setConnectUrl(projectDto.getConnectUrl());
        project.setReferenceUrl(projectDto.getReferenceUrl());
        project.setRecruitmentDate(projectDto.getRecruitmentDate());
        projectRepository.save(project);
        return project;
    }

    @Override
    public void delete(UUID id) {
        //id가 아닌 다른 기준으로 값을 삭제할 때
        /*Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        projectRepository.delete(project);*/

        //id 기준
        projectRepository.deleteById(id);
    }

    @Override
    public Project findById(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }
}

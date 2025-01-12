package com.waggle.domain.project.controller;

import com.waggle.domain.project.dto.CreateProjectDto;
import com.waggle.domain.project.dto.UpdateProjectDto;
import com.waggle.domain.project.entity.Project;
import com.waggle.domain.project.service.ProjectService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

//get: 조회, post: 생성, patch: 수정, delete: 삭제
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/post") //경로에 있는 post는 post 방식이 아니라 게시글을 영어로 한거임
    //이 경로는 /project/post
    public ResponseEntity<BaseResponse<Project>> createProject(@RequestBody CreateProjectDto createProjectDto) {
        Project newProject = projectService.create(createProjectDto);
        return SuccessResponse.of(ApiStatus._CREATED, newProject);
    }
    @PatchMapping("/post/{id}")
    public ResponseEntity<BaseResponse<Project>> updateProject(@PathVariable String id, @RequestBody UpdateProjectDto updateProjectDto) {
        Project updateProject = projectService.update(UUID.fromString(id), updateProjectDto);
        return SuccessResponse.of(ApiStatus._OK, updateProject);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<BaseResponse<Project>> deleteProject(@PathVariable String id) {
        projectService.delete(UUID.fromString(id));
        return SuccessResponse.of(ApiStatus._NO_CONTENT, null);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<BaseResponse<Project>> fetchProject(@PathVariable String id) {
        Project fetchProject = projectService.findById(UUID.fromString(id));
        return SuccessResponse.of(ApiStatus._OK, fetchProject);
    }
}

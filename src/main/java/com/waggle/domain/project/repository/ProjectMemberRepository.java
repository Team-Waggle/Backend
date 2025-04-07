package com.waggle.domain.project.repository;

import com.waggle.domain.project.entity.ProjectMember;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {

}

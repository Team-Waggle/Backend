package com.waggle.domain.projectV2.repository;

import com.waggle.domain.projectV2.ProjectV2;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectV2Repository extends JpaRepository<ProjectV2, UUID> {

}

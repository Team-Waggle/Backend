package com.waggle.domain.recruitment.service;

import com.waggle.domain.recruitment.Recruitment;
import com.waggle.domain.recruitment.repository.RecruitmentRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;

    @Transactional(readOnly = true)
    public List<Recruitment> getProjectRecruitments(UUID projectId) {
        return recruitmentRepository.findByProjectIdOrderByPosition(projectId);
    }
}

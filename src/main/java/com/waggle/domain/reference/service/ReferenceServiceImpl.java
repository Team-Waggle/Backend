package com.waggle.domain.reference.service;

import com.waggle.domain.reference.entity.*;
import com.waggle.domain.reference.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReferenceServiceImpl implements ReferenceService {

    private final DurationOfWorkingRepository durationOfWorkingRepository;
    private final PortfolioUrlRepository portfolioUrlRepository;
    private final IndustrialRepository industrialRepository;
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final WaysOfWorkingRepository waysOfWorkingRepository;

    @Override
    public List<PortfolioUrl> getPortfolioUrls() {
        return portfolioUrlRepository.findAll();
    }

    @Override
    public List<Industrial> getIndustrials() {
        return industrialRepository.findAll();
    }

    @Override
    public List<Job> getJobs() {
        return jobRepository.findAll();
    }

    @Override
    public List<Skill> getSkills() {
        return skillRepository.findAll();
    }

    @Override
    public List<DurationOfWorking> getDurationOfWorkings() {
        return durationOfWorkingRepository.findAll();
    }

    @Override
    public List<WaysOfWorking> getWaysOfWorkings() {
        return waysOfWorkingRepository.findAll();
    }
}

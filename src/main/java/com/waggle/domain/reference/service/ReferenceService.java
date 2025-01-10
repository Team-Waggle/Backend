package com.waggle.domain.reference.service;

import com.waggle.domain.reference.entity.*;

import java.util.List;

public interface ReferenceService {

    List<PortfolioUrl> getPortfolioUrls();
    List<Industrial> getIndustrials();
    List<Job> getJobs();
    List<Skill> getSkills();
    List<DurationOfWorking> getDurationOfWorkings();
    List<WaysOfWorking> getWaysOfWorkings();
}

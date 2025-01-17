package com.waggle.domain.reference.service;

import com.waggle.domain.reference.entity.*;

import java.util.List;

public interface ReferenceService {

    List<PortfolioUrl> getPortfolioUrls();
    PortfolioUrl getPortfolioUrlById(Long id);
    List<Industry> getIndustrials();
    Industry getIndustryById(Long id);
    List<Job> getJobs();
    Job getJobById(Long id);
    List<Skill> getSkills();
    Skill getSkillById(Long id);
    List<DurationOfWorking> getDurationOfWorkings();
    DurationOfWorking getDurationOfWorkingById(Long id);
    List<WaysOfWorking> getWaysOfWorkings();
    WaysOfWorking getWaysOfWorkingById(Long id);
    List<WeekDays> getWeekDays();
    WeekDays getWeekDaysById(Long id);
    List<TimeOfWorking> getTimeOfWorkings();
    TimeOfWorking getTimeOfWorkingById(Long id);
    List<Sido> getSidoes();
    Sido getSidoesById(String id);
    List<MainIntroduce> getMainIntroduces();
    MainIntroduce getMainIntroduceById(Long id);
    List<SubIntroduce> getSubIntroduces();
    SubIntroduce getSubIntroduceById(Long id);
}

package com.waggle.domain.reference.service;

import com.waggle.domain.reference.entity.DurationOfWorking;
import com.waggle.domain.reference.entity.Industry;
import com.waggle.domain.reference.entity.MainIntroduce;
import com.waggle.domain.reference.entity.PortfolioUrl;
import com.waggle.domain.reference.entity.Sido;
import com.waggle.domain.reference.entity.Skill;
import com.waggle.domain.reference.entity.SubIntroduce;
import com.waggle.domain.reference.entity.TimeOfWorking;
import com.waggle.domain.reference.entity.WaysOfWorking;
import com.waggle.domain.reference.entity.WeekDays;
import com.waggle.domain.reference.enums.JobRole;
import java.util.List;

public interface ReferenceService {

    List<PortfolioUrl> getPortfolioUrls();

    PortfolioUrl getPortfolioUrlById(Long id);

    List<Industry> getIndustrials();

    Industry getIndustryById(Long id);

    List<JobRole> getJobs();

    JobRole getJobById(Long id);

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

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
import com.waggle.domain.reference.repository.DurationOfWorkingRepository;
import com.waggle.domain.reference.repository.IndustryRepository;
import com.waggle.domain.reference.repository.JobRepository;
import com.waggle.domain.reference.repository.MainIntroduceRepository;
import com.waggle.domain.reference.repository.PortfolioUrlRepository;
import com.waggle.domain.reference.repository.SidoRepository;
import com.waggle.domain.reference.repository.SkillRepository;
import com.waggle.domain.reference.repository.SubIntroduceRepository;
import com.waggle.domain.reference.repository.TimeOfWorkingRepository;
import com.waggle.domain.reference.repository.WaysOfWorkingRepository;
import com.waggle.domain.reference.repository.WeekDaysRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReferenceServiceImpl implements ReferenceService {

    private final DurationOfWorkingRepository durationOfWorkingRepository;
    private final PortfolioUrlRepository portfolioUrlRepository;
    private final IndustryRepository industryRepository;
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final WaysOfWorkingRepository waysOfWorkingRepository;
    private final WeekDaysRepository weekDaysRepository;
    private final TimeOfWorkingRepository timeOfWorkingRepository;
    private final SidoRepository sidoRepository;
    private final MainIntroduceRepository mainIntroduceRepository;
    private final SubIntroduceRepository subIntroduceRepository;

    @Override
    public List<PortfolioUrl> getPortfolioUrls() {
        List<PortfolioUrl> portfolioUrls = portfolioUrlRepository.findAll();
        if (portfolioUrls.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return portfolioUrls;
    }

    @Override
    public PortfolioUrl getPortfolioUrlById(Long id) {
        return portfolioUrlRepository.findById(id)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    public List<Industry> getIndustrials() {
        List<Industry> industries = industryRepository.findAll();
        if (industries.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return industries;
    }

    @Override
    public Industry getIndustryById(Long id) {
        return industryRepository.findById(id)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    public List<JobRole> getJobs() {
//        List<Job> jobs = jobRepository.findAll();
//        if (jobs.isEmpty()) {
//            throw new EmptyResultDataAccessException(1);
//        }
//        return jobs;
        return null;
    }

    @Override
    public JobRole getJobById(Long id) {
//        return jobRepository.findById(id)
//                .orElseThrow(() -> new EmptyResultDataAccessException(1));
        return null;
    }

    @Override
    public List<Skill> getSkills() {
        List<Skill> skills = skillRepository.findAll();
        if (skills.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return skills;
    }

    @Override
    public Skill getSkillById(Long id) {
        return skillRepository.findById(id)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    public List<DurationOfWorking> getDurationOfWorkings() {
        List<DurationOfWorking> durationOfWorkings = durationOfWorkingRepository.findAll();
        if (durationOfWorkings.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return durationOfWorkings;
    }

    @Override
    public DurationOfWorking getDurationOfWorkingById(Long id) {
        return durationOfWorkingRepository.findById(id)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    public List<WaysOfWorking> getWaysOfWorkings() {
        List<WaysOfWorking> waysOfWorkings = waysOfWorkingRepository.findAll();
        if (waysOfWorkings.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return waysOfWorkings;
    }

    @Override
    public WaysOfWorking getWaysOfWorkingById(Long id) {
        return waysOfWorkingRepository.findById(id)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    public List<WeekDays> getWeekDays() {
        List<WeekDays> weekDays = weekDaysRepository.findAll();
        if (weekDays.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return weekDays;
    }

    @Override
    public WeekDays getWeekDaysById(Long id) {
        return weekDaysRepository.findById(id)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    public List<TimeOfWorking> getTimeOfWorkings() {
        List<TimeOfWorking> timeOfWorkings = timeOfWorkingRepository.findAll();
        if (timeOfWorkings.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return timeOfWorkings;
    }

    @Override
    public TimeOfWorking getTimeOfWorkingById(Long id) {
        return timeOfWorkingRepository.findById(id)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    public List<Sido> getSidoes() {
        List<Sido> sidoes = sidoRepository.findAll();
        if (sidoes.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return sidoes;
    }

    @Override
    public Sido getSidoesById(String id) {
        return sidoRepository.findById(id)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    public List<MainIntroduce> getMainIntroduces() {
        List<MainIntroduce> mainIntroduces = mainIntroduceRepository.findAll();
        if (mainIntroduces.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return mainIntroduces;
    }

    @Override
    public MainIntroduce getMainIntroduceById(Long id) {
        return mainIntroduceRepository.findById(id)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    public List<SubIntroduce> getSubIntroduces() {
        List<SubIntroduce> subIntroduces = subIntroduceRepository.findAll();
        if (subIntroduces.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return subIntroduces;
    }

    @Override
    public SubIntroduce getSubIntroduceById(Long id) {
        return subIntroduceRepository.findById(id)
            .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }
}

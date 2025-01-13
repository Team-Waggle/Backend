package com.waggle.domain.reference.service;

import com.waggle.domain.reference.entity.*;
import com.waggle.domain.reference.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<PortfolioUrl> getPortfolioUrls() {
        List<PortfolioUrl> portfolioUrls = portfolioUrlRepository.findAll();
        if (portfolioUrls.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return portfolioUrls;
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
    public List<Job> getJobs() {
        List<Job> jobs = jobRepository.findAll();
        if (jobs.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return jobs;
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
    public List<DurationOfWorking> getDurationOfWorkings() {
        List<DurationOfWorking> durationOfWorkings = durationOfWorkingRepository.findAll();
        if (durationOfWorkings.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return durationOfWorkings;
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
    public List<WeekDays> getWeekDays() {
        List<WeekDays> weekDays = weekDaysRepository.findAll();
        if (weekDays.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return weekDays;
    }

    @Override
    public List<TimeOfWorking> getTows() {
        List<TimeOfWorking> timeOfWorkings = timeOfWorkingRepository.findAll();
        if (timeOfWorkings.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return timeOfWorkings;
    }

    @Override
    public List<Sido> getSidoes() {
        List<Sido> sidoes = sidoRepository.findAll();
        if (sidoes.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        }
        return sidoes;
    }
}

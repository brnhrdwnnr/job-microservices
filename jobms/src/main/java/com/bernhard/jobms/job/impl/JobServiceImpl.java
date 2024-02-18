package com.bernhard.jobms.job.impl;

import com.bernhard.jobms.job.Job;
import com.bernhard.jobms.job.JobRepository;
import com.bernhard.jobms.job.JobService;
import com.bernhard.jobms.job.dto.JobWithCompanyDTO;
import com.bernhard.jobms.job.external.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = { @Autowired})
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    @Override
    public List<JobWithCompanyDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        List<JobWithCompanyDTO> jobWithCompanyDTOS = new ArrayList<>();
        return jobs
                .stream()
                .map(this::converToDto)
                .toList();
    }

    private JobWithCompanyDTO converToDto (Job job) {
        JobWithCompanyDTO jobWithCompanyDTO = new JobWithCompanyDTO();
        jobWithCompanyDTO.setJob(job);
        RestTemplate restTemplate = new RestTemplate();
        Company company = restTemplate.getForObject(
                "http://localhost:8081/companies/" + job.getCompanyId(), Company.class);
        jobWithCompanyDTO.setCompany(company);
        return jobWithCompanyDTO;
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);

    }

    @Override
    public boolean deleteJobById(Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;

    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setLocation(updatedJob.getLocation());
            job.setMinSalary(updatedJob.getMinSalary());
            job.setMaxSalary(updatedJob.getMaxSalary());
            jobRepository.save(job);
            return true;
            }
        return false;
    }


}

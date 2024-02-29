package com.bernhard.jobms.job.impl;

import com.bernhard.jobms.job.Job;
import com.bernhard.jobms.job.JobRepository;
import com.bernhard.jobms.job.JobService;
import com.bernhard.jobms.job.dto.JobDTO;
import com.bernhard.jobms.job.external.Company;
import com.bernhard.jobms.job.external.Review;
import com.bernhard.jobms.job.mapper.JobMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final RestTemplate restTemplate;

    @Override
    public List<JobDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        return jobs
                .stream()
                .map(this::converToDto)
                .toList();
    }

    private JobDTO converToDto (Job job) {
        Company company = restTemplate.getForObject(
                "http://COMPANY-SERVICE:8081/companies/" + job.getCompanyId(), Company.class);

        //if list is return as response its better to use exchange, getForObject is most suitable for single response
        ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange(
                "http://REVIEW-SERVICE:8083/reviews?companyId=" + job.getCompanyId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Review>>() {
                });

        List<Review> reviews = reviewResponse.getBody();

        return JobMapper.mapToJobWithCompanyDto(job, company, reviews);
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        return converToDto(job);
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

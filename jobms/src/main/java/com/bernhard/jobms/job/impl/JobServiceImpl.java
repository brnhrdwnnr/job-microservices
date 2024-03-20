package com.bernhard.jobms.job.impl;

import com.bernhard.jobms.job.Job;
import com.bernhard.jobms.job.JobRepository;
import com.bernhard.jobms.job.JobService;
import com.bernhard.jobms.job.client.CompanyClient;
import com.bernhard.jobms.job.client.ReviewClient;
import com.bernhard.jobms.job.dto.JobDTO;
import com.bernhard.jobms.job.external.Company;
import com.bernhard.jobms.job.external.Review;
import com.bernhard.jobms.job.mapper.JobMapper;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
//    private final RestTemplate restTemplate;
    private final CompanyClient companyClient;
    private final ReviewClient reviewClient;

    public int attempt = 0;

    @Override
//    @CircuitBreaker(name = "companyBreaker",
//            fallbackMethod = "companyBreakerFallback"
//    )
//    @Retry(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    @RateLimiter(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    public List<JobDTO> findAll() {
        System.out.println("Attempt: " + ++attempt);
        List<Job> jobs = jobRepository.findAll();
        return jobs
                .stream()
                .map(this::converToDto)
                .toList();
    }
    
    public List<String> companyBreakerFallback(Exception e) {
        List<String> list = new ArrayList<>();
        list.add("Dummy");
        return list;
    }

    private JobDTO converToDto (Job job) {
        Company company = companyClient.getCompany(job.getCompanyId());
        List<Review> reviews = reviewClient.getReviews(job.getCompanyId());

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

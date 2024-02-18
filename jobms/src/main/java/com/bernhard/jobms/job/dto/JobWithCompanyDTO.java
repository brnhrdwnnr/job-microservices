package com.bernhard.jobms.job.dto;

import com.bernhard.jobms.job.external.Company;
import com.bernhard.jobms.job.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobWithCompanyDTO {

    private Job job;
    private Company company;

}

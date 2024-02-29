package com.bernhard.jobms.job.dto;

import com.bernhard.jobms.job.external.Company;
import com.bernhard.jobms.job.external.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {

    private Long id;
    private String title;
    private String description;
    private String minSalary;
    private String maxSalary;
    private String location;

    private Company company;

    private List<Review> reviews;

}

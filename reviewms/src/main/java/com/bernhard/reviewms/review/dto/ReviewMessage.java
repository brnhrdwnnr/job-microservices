package com.bernhard.reviewms.review.dto;

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
public class ReviewMessage {

    private Long id;
    private String title;
    private String description;
    private double rating;
    private Long companyId;
}

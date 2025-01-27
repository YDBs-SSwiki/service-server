package com.sswiki.serviceserver.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateReviewRequestDTO {
    private Integer breadId;
    private Integer userId;

    @Min(value = 1, message = "rating must be at least 1.")
    @Max(value = 5, message = "rating must be no more than 5.")
    private Integer rating;
    private String content;
}

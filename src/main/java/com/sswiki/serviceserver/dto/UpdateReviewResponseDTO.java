package com.sswiki.serviceserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateReviewResponseDTO {

    private Integer reviewId;
    private Integer breadId;
    private Integer userId;
    private Integer rating;
    private String content;
    private String createdAt;
    private String imageUrl;
}

package com.sswiki.serviceserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserReviewsResponseDTO {
    private Integer userId;
    private List<ReviewDTO> reviews;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewDTO {
        private Integer reviewId;
        private Integer breadId;
        private Integer rating;
        private String content;
        private LocalDateTime createdAt;
    }
}

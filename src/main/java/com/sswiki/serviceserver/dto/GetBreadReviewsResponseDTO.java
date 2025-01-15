package com.sswiki.serviceserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetBreadReviewsResponseDTO {
    private Integer breadId;
    private List<ReviewDTO> reviews;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReviewDTO {
        private Integer reviewId;
        private Integer userId;
        private Integer rating;
        private String content;
        private Integer likes;
        private LocalDateTime createdAt;
    }
}

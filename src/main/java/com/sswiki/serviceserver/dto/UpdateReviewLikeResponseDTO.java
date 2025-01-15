package com.sswiki.serviceserver.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UpdateReviewLikeResponseDTO {
    private Long reviewId;
    private Long userId;
    private Boolean liked; // 최종적으로 좋아요가 true인지 false인지
    private Integer totalLikes; // 총 좋아요 개수

    public UpdateReviewLikeResponseDTO(Long reviewId, Long userId, boolean like, int totalLikes) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.liked = like;
        this.totalLikes = totalLikes;
    }
}

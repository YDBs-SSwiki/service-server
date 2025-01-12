package com.sswiki.serviceserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateReviewResponseDTO {
    private int reviewId;
    private int breadId;
    private int userId;
    private int rating;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDate createdAt;

    public CreateReviewResponseDTO(int reviewId, int breadId, int userId, int rating, String title, String content, String imageUrl, LocalDate createdAt) {
        this.reviewId = reviewId;
        this.breadId = breadId;
        this.userId = userId;
        this.rating = rating;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }
}

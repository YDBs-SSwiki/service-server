package com.sswiki.serviceserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewRequestDTO {
    private int breadId;
    private int userId;
    private int rating;
    private String title;
    private String content;
    private String imageUrl;

    public CreateReviewRequestDTO(int breadId, int userId, int rating, String content, String title, String imageUrl) {
        this.breadId = breadId;
        this.userId = userId;
        this.rating = rating;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

}

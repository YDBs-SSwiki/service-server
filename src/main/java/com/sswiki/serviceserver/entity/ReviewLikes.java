package com.sswiki.serviceserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ReviewLikes {
    @EmbeddedId
    private ReviewLikesId id = new ReviewLikesId();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("reviewId")
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // ★ 추가: User, Review 를 받는 생성자
    public ReviewLikes(User user, Review review) {
        this.user = user;
        this.review = review;
        // EmbeddedId 세팅
        this.id.setUserId(user.getUserId());
        this.id.setReviewId(review.getReviewId());
    }
}


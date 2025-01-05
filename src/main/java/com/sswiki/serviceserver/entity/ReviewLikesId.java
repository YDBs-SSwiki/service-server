package com.sswiki.serviceserver.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Embeddable
public class ReviewLikesId implements Serializable {
    // Getter & Setter
    private Integer userId;
    private Integer reviewId;

    // 기본 생성자
    public ReviewLikesId() {}

    // 생성자
    public ReviewLikesId(Integer userId, Integer reviewId) {
        this.userId = userId;
        this.reviewId = reviewId;
    }

    // equals & hashCode (필수)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewLikesId that = (ReviewLikesId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(reviewId, that.reviewId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, reviewId);
    }
}

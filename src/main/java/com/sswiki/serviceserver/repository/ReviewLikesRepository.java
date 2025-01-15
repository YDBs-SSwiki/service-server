package com.sswiki.serviceserver.repository;

import com.sswiki.serviceserver.entity.ReviewLikes;
import com.sswiki.serviceserver.entity.ReviewLikesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewLikesRepository extends JpaRepository<ReviewLikes, ReviewLikesId> {
    boolean existsByReviewReviewIdAndUserUserId(Long reviewId, Long userId);

    void deleteByReviewReviewIdAndUserUserId(Long reviewId, Long userId);
    // 필요하다면 커스텀 쿼리 메서드를 추가할 수 있음
}
package com.sswiki.serviceserver.repository;

import com.sswiki.serviceserver.entity.Review;
import com.sswiki.serviceserver.entity.ReviewLikes;
import com.sswiki.serviceserver.entity.ReviewLikesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewLikesRepository extends JpaRepository<ReviewLikes, ReviewLikesId> {
    boolean existsByReviewReviewIdAndUserUserId(Long reviewId, Long userId);

    void deleteByReviewReviewIdAndUserUserId(Long reviewId, Long userId);

    List<ReviewLikes> findByUserUserId(Integer userId);
}
package com.sswiki.serviceserver.repository;

import com.sswiki.serviceserver.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByUser_UserId(Integer userID);
    List<Review> findByBread_BreadId(Integer breadID);

    // 1 증가
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Review r SET r.likes = r.likes + 1 WHERE r.reviewId = :reviewId")
    void increaseLikes(@Param("reviewId") Long reviewId);

    // 1 감소
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Review r SET r.likes = r.likes - 1 WHERE r.reviewId = :reviewId")
    void decreaseLikes(@Param("reviewId") Long reviewId);
}
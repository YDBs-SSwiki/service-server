package com.sswiki.serviceserver.repository;

import com.sswiki.serviceserver.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByUser_UserId(Integer userID);
    List<Review> findByBread_BreadId(Integer breadID);
}
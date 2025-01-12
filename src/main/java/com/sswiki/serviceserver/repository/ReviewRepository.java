package com.sswiki.serviceserver.repository;

import com.sswiki.serviceserver.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    // 필요하다면 커스텀 쿼리 메서드를 추가할 수 있음
}
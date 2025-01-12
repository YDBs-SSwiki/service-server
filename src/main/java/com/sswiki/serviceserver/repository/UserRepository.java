package com.sswiki.serviceserver.repository;

import com.sswiki.serviceserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // 추가적인 쿼리 메서드가 필요하다면 여기에 정의
}

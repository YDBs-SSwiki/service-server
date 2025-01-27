package com.sswiki.serviceserver.repository;

import com.sswiki.serviceserver.entity.Bread;
import com.sswiki.serviceserver.entity.Favorites;
import com.sswiki.serviceserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorites, Integer> {
    boolean existsByUserAndBread(User user, Bread bread);

    List<Favorites> findByUserUserId(Integer userID);

    Optional<Object> findByUserAndBread(User user, Bread bread);
}
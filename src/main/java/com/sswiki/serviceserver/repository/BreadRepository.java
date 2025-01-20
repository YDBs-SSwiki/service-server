package com.sswiki.serviceserver.repository;

import com.sswiki.serviceserver.entity.Bread;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BreadRepository extends JpaRepository<Bread, Integer> {
    List<Bread> findByNameContainingIgnoreCase(String keyword);
}

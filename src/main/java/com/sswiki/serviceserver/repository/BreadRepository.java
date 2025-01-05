package com.sswiki.serviceserver.repository;

import com.sswiki.serviceserver.entity.Bread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreadRepository extends JpaRepository<Bread, Integer> {
}

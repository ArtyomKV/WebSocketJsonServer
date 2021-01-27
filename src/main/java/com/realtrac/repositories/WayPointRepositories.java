package com.realtrac.repositories;

import com.realtrac.entities.WayPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WayPointRepositories extends JpaRepository<WayPoint, Long> {
}

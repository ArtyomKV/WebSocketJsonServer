package com.realtrac.repositories;

import com.realtrac.entities.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroRepositories extends JpaRepository<Hero, Long> {
}

package com.example.sustainable_lifestyle_tracker.repository;

import com.example.sustainable_lifestyle_tracker.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByUserId(Long userId);
    List<Activity> findByUserIdAndDateBetween(Long userId, String startDate, String endDate);
}

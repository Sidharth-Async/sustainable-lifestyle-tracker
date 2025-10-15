package com.example.sustainable_lifestyle_tracker.controller;

import com.example.sustainable_lifestyle_tracker.dto.ActivityDTO;
import com.example.sustainable_lifestyle_tracker.entity.Activity;
import com.example.sustainable_lifestyle_tracker.service.EmissionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final EmissionService emissionService;

    public ActivityController(EmissionService emissionService) {
        this.emissionService = emissionService;
    }

    /**
     * Create a new activity with CO2e calculation
     */
    @PostMapping
    public ResponseEntity<?> createActivity(@RequestBody ActivityDTO activityDTO) {
        try {
            Activity savedActivity = emissionService.saveActivity(activityDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedActivity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating activity: " + e.getMessage());
        }
    }

    /**
     * Get all activities for the current user
     */
    @GetMapping
    public ResponseEntity<List<Activity>> getUserActivities() {
        try {
            List<Activity> activities = emissionService.getUserActivities();
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get activities for the current user within a date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Activity>> getActivitiesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Activity> activities = emissionService.getActivitiesByDateRange(startDate, endDate);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Calculate total emissions for the current user within a date range
     */
    @GetMapping("/total-emissions")
    public ResponseEntity<Double> calculateTotalEmissions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            Double totalEmissions = emissionService.calculateTotalEmissions(startDate, endDate);
            return ResponseEntity.ok(totalEmissions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get activities for the current user by specific date
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Activity>> getActivitiesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Activity> activities = emissionService.getActivitiesByDateRange(date, date);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get today's activities for the current user
     */
    @GetMapping("/today")
    public ResponseEntity<List<Activity>> getTodayActivities() {
        try {
            LocalDate today = LocalDate.now();
            List<Activity> activities = emissionService.getActivitiesByDateRange(today, today);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get this week's activities for the current user
     */
    @GetMapping("/this-week")
    public ResponseEntity<List<Activity>> getThisWeekActivities() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
            List<Activity> activities = emissionService.getActivitiesByDateRange(startOfWeek, today);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get this month's activities for the current user
     */
    @GetMapping("/this-month")
    public ResponseEntity<List<Activity>> getThisMonthActivities() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate startOfMonth = today.withDayOfMonth(1);
            List<Activity> activities = emissionService.getActivitiesByDateRange(startOfMonth, today);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Calculate today's total emissions
     */
    @GetMapping("/today/total-emissions")
    public ResponseEntity<Double> getTodayTotalEmissions() {
        try {
            LocalDate today = LocalDate.now();
            Double totalEmissions = emissionService.calculateTotalEmissions(today, today);
            return ResponseEntity.ok(totalEmissions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Calculate this week's total emissions
     */
    @GetMapping("/this-week/total-emissions")
    public ResponseEntity<Double> getThisWeekTotalEmissions() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
            Double totalEmissions = emissionService.calculateTotalEmissions(startOfWeek, today);
            return ResponseEntity.ok(totalEmissions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Calculate this month's total emissions
     */
    @GetMapping("/this-month/total-emissions")
    public ResponseEntity<Double> getThisMonthTotalEmissions() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate startOfMonth = today.withDayOfMonth(1);
            Double totalEmissions = emissionService.calculateTotalEmissions(startOfMonth, today);
            return ResponseEntity.ok(totalEmissions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
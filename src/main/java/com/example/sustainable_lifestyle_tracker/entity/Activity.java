package com.example.sustainable_lifestyle_tracker.entity;

import com.example.sustainable_lifestyle_tracker.enums.ActivityType;
import jakarta.persistence.*;

@Entity
@Table(name = "activities")
public class Activity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType type;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String date; // or LocalDate if possible


    private String details;

    @Column(nullable = false)
    private double co2e; // in kg CO2e

    // ✅ Constructors
    public Activity() {}

    public Activity(Long userId, String date, String type, String details, double co2e) {
        this.userId = userId;
        this.date = date;
        this.details = details;
        this.co2e = co2e;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public double getCo2e() { return co2e; }
    public void setCo2e(double co2e) { this.co2e = co2e; }
}

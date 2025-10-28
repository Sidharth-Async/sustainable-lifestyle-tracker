package com.example.sustainable_lifestyle_tracker.dto;

import com.example.sustainable_lifestyle_tracker.enums.ActivityType;

public class ActivityDTO {

    private Long id;
    private Long userId;
    private String date;      // or LocalDate if your entity uses that
    private ActivityType type;
    private String details;
    private double co2e;

    // Transport fields
    private Double distance;
    private String vehicleType;

    // Waste fields
    private String wasteType;
    private Double weight;

    // Energy fields
    private String energySource;
    private Double consumption;

    // Delivery fields
    private String deliveryType;

    // ✅ Constructors
    public ActivityDTO() {}

    public ActivityDTO(Long id, Long userId, String date, String details, double co2e) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.details = details;
        this.co2e = co2e;
    }

    // ✅ Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public ActivityType getType() { return type; }
    public void setType(ActivityType type) { this.type = type; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public double getCo2e() { return co2e; }
    public void setCo2e(double co2e) { this.co2e = co2e; }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getWasteType() {
        return wasteType;
    }

    public void setWasteType(String wasteType) {
        this.wasteType = wasteType;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getEnergySource() {
        return energySource;
    }

    public void setEnergySource(String energySource) {
        this.energySource = energySource;
    }

    public Double getConsumption() {
        return consumption;
    }

    public void setConsumption(Double consumption) {
        this.consumption = consumption;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
}

package com.example.sustainable_lifestyle_tracker.service;

import com.example.sustainable_lifestyle_tracker.dto.ActivityDTO;
import com.example.sustainable_lifestyle_tracker.entity.Activity;
import com.example.sustainable_lifestyle_tracker.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private CO2CalculatorService co2CalculatorService;

    public Activity createActivity(ActivityDTO activityDTO) {
        // Calculate CO2e
        Double co2e = co2CalculatorService.calculateCO2e(activityDTO);

        System.out.println("=== CO2 Calculation Debug ===");
        System.out.println("Activity Type: " + activityDTO.getType());
        System.out.println("Calculated CO2e: " + co2e);

        // Convert DTO to Entity
        Activity activity = new Activity();
        activity.setUserId(activityDTO.getUserId());
        activity.setType(activityDTO.getType());
        activity.setDate(activityDTO.getDate());
        activity.setDetails(activityDTO.getDetails());
        activity.setCo2e(co2e);

        // Copy type-specific fields
        activity.setDistance(activityDTO.getDistance());
        activity.setVehicleType(activityDTO.getVehicleType());
        activity.setWasteType(activityDTO.getWasteType());
        activity.setWeight(activityDTO.getWeight());
        activity.setEnergySource(activityDTO.getEnergySource());
        activity.setConsumption(activityDTO.getConsumption());
        activity.setDeliveryType(activityDTO.getDeliveryType());

        Activity saved = activityRepository.save(activity);
        System.out.println("Saved Activity CO2e: " + saved.getCo2e());

        return saved;
    }

    public List<Activity> getRecentActivities(Long userId) {
        return activityRepository.findTop10ByUserIdOrderByDateDesc(userId);
    }

    public List<Activity> getAllActivities(Long userId) {
        return activityRepository.findByUserIdOrderByDateDesc(userId);
    }

    public boolean deleteActivity(Long id, String username) {
        Optional<Activity> activity = activityRepository.findById(id);
        if (activity.isPresent() && activity.get().getId().equals(username)) {
            activityRepository.delete(activity.get());
            return true;
        }
        return false;
    }
}
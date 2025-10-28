package com.example.sustainable_lifestyle_tracker.controller;

import com.example.sustainable_lifestyle_tracker.dto.ActivityDTO;
import com.example.sustainable_lifestyle_tracker.entity.Activity;
import com.example.sustainable_lifestyle_tracker.entity.User;
import com.example.sustainable_lifestyle_tracker.repository.UserRepository;
import com.example.sustainable_lifestyle_tracker.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserRepository userRepository;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    @PostMapping
    public ResponseEntity<?> createActivity(@RequestBody ActivityDTO activityDTO) {
        try {
            // Attach current user ID
            Long userId = getCurrentUserId();
            activityDTO.setUserId(userId);

            System.out.println("=== Creating Activity ===");
            System.out.println("User ID: " + userId);
            System.out.println("Type: " + activityDTO.getType());
            System.out.println("Distance: " + activityDTO.getDistance());
            System.out.println("Vehicle Type: " + activityDTO.getVehicleType());

            Activity savedActivity = activityService.createActivity(activityDTO);

            return ResponseEntity.ok(savedActivity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error creating activity: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getActivities() {
        try {
            Long userId = getCurrentUserId();
            List<Activity> activities = activityService.getAllActivities(userId);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching activities: " + e.getMessage());
        }
    }
}
package com.example.sustainable_lifestyle_tracker.service;

import com.example.sustainable_lifestyle_tracker.dto.ActivityDTO;
import com.example.sustainable_lifestyle_tracker.entity.Activity;
import com.example.sustainable_lifestyle_tracker.entity.User;
import com.example.sustainable_lifestyle_tracker.enums.ActivityType;
import com.example.sustainable_lifestyle_tracker.repository.ActivityRepository;
import com.example.sustainable_lifestyle_tracker.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmissionService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    // Climatiq API configuration
    private static final String CLIMATIQ_API_URL = "https://api.climatiq.io/v1/estimate";
    private static final String CLIMATIQ_API_KEY = "${climatiq.api.key}"; // Should be in application.properties

    public EmissionService(ActivityRepository activityRepository, UserRepository userRepository, WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.webClientBuilder = webClientBuilder;
        this.objectMapper = objectMapper;
    }

    /**
     * Parse JSON details string to Map
     */
    private Map<String, String> parseDetails(String detailsJson) {
        try {
            if (detailsJson == null || detailsJson.isEmpty()) {
                return new HashMap<>();
            }
            return objectMapper.readValue(detailsJson, new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
             //or("Error parsing details JSON: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Get the currently authenticated user's ID from Spring Security context
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getId();
    }

    /**
     * Attach the logged-in user's ID to the activity
     */
    private ActivityDTO attachUserId(ActivityDTO activityDTO) {
        Long userId = getCurrentUserId();
        activityDTO.setUserId(userId);
         //bug("Attached user ID: {} to activity", userId);
        return activityDTO;
    }

    /**
     * Auto-attach current date to activity
     */
    private ActivityDTO attachCurrentDate(ActivityDTO activityDTO) {
        if (activityDTO.getDate() == null || activityDTO.getDate().isEmpty()) {
            LocalDate currentDate = LocalDate.now();
            activityDTO.setDate(currentDate.toString());
             //bug("Attached current date: {} to activity", currentDate);
        }
        return activityDTO;
    }

    /**
     * Validate that the activity type is allowed
     */
    private void validateActivityType(ActivityDTO activityDTO) {
        try {
            ActivityType.valueOf(activityDTO.getType().name());
             //bug("Activity type validated: {}", activityDTO.getType());
        } catch (IllegalArgumentException e) {
             //or("Invalid activity type: {}", activityDTO.getType());
            throw new IllegalArgumentException(
                    "Invalid activity type. Allowed values: TRANSPORT, FOOD, WASTE, ENERGY, WATER, SHOPPING"
            );
        }
    }

    /**
     * Calculate CO2e emissions using Climatiq API
     * This is a placeholder - implement based on your activity details structure
     */
    private Double calculateCO2e(ActivityDTO activityDTO) {
        try {
            // Route to appropriate calculation method based on activity type
            return switch (activityDTO.getType()) {
                case TRANSPORTATION -> calculateTransportEmissions(activityDTO);
                case ENERGY -> calculateEnergyEmissions(activityDTO);
                case FOOD -> calculateFoodEmissions(activityDTO);
                case WASTE -> calculateWasteEmissions(activityDTO);
                case WATER -> calculateWaterEmissions(activityDTO);
                case SHOPPING -> calculateShoppingEmissions(activityDTO);
                default -> 0.0;
            };

        } catch (Exception e) {
             //or("Error calculating CO2e emissions: {}", e.getMessage());
            throw new RuntimeException("Failed to calculate emissions", e);
        }
    }

    /**
     * Calculate transport emissions using Climatiq API
     */
    private Double calculateTransportEmissions(ActivityDTO activityDTO) {
        Map<String, String> details = parseDetails(activityDTO.getDetails());

        // TODO: Implement actual Climatiq API call
        // This is a simplified example

        WebClient webClient = webClientBuilder.build();

        // Build request body based on activity details
        String requestBody = String.format("""
            {
                "emission_factor": {
                    "activity_id": "passenger_vehicle-vehicle_type_car-fuel_source_na-engine_size_na-vehicle_age_na-vehicle_weight_na"
                },
                "parameters": {
                    "distance": %s,
                    "distance_unit": "km"
                }
            }
            """, details.getOrDefault("distance", "0"));

        try {
            Mono<ClimatiqResponse> response = webClient.post()
                    .uri(CLIMATIQ_API_URL)
                    .header("Authorization", "Bearer " + CLIMATIQ_API_KEY)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(ClimatiqResponse.class);

            ClimatiqResponse climatiqResponse = response.block();
            return climatiqResponse != null ? climatiqResponse.getCo2e() : 0.0;

        } catch (Exception e) {
             //or("Climatiq API call failed: {}", e.getMessage());
            // Return estimated value as fallback
            return estimateCO2eLocally(activityDTO);
        }
    }

    /**
     * Calculate energy emissions
     */
    private Double calculateEnergyEmissions(ActivityDTO activityDTO) {
        // Implement Climatiq API call for energy
        return estimateCO2eLocally(activityDTO);
    }

    /**
     * Calculate food emissions
     */
    private Double calculateFoodEmissions(ActivityDTO activityDTO) {
        // Implement Climatiq API call for food
        return estimateCO2eLocally(activityDTO);
    }

    /**
     * Calculate waste emissions
     */
    private Double calculateWasteEmissions(ActivityDTO activityDTO) {
        // Implement Climatiq API call for waste
        return estimateCO2eLocally(activityDTO);
    }

    /**
     * Calculate water emissions
     */
    private Double calculateWaterEmissions(ActivityDTO activityDTO) {
        // Implement Climatiq API call for water
        return estimateCO2eLocally(activityDTO);
    }

    /**
     * Calculate shopping emissions
     */
    private Double calculateShoppingEmissions(ActivityDTO activityDTO) {
        // Implement Climatiq API call for shopping
        return estimateCO2eLocally(activityDTO);
    }

    /**
     * Local estimation fallback (using average emission factors)
     */
    private Double estimateCO2eLocally(ActivityDTO activityDTO) {
        Map<String, String> details = parseDetails(activityDTO.getDetails());

        // Simple estimation based on activity type
        return switch (activityDTO.getType()) {
            case TRANSPORTATION -> {
                Double distance = Double.parseDouble(details.getOrDefault("distance", "0"));
                String mode = details.getOrDefault("mode", "car");
                // Emission factors per km
                yield distance * switch (mode.toLowerCase()) {
                    case "car" -> 0.21;      // 0.21 kg CO2e per km
                    case "bus" -> 0.10;      // 0.10 kg CO2e per km
                    case "train" -> 0.04;    // 0.04 kg CO2e per km
                    case "bike", "walk" -> 0.0;
                    case "flight" -> 0.25;   // 0.25 kg CO2e per km
                    default -> 0.15;
                };
            }
            case ENERGY -> {
                Double kwh = Double.parseDouble(details.getOrDefault("kwh", "0"));
                yield kwh * 0.5; // Average grid emissions: 0.5 kg CO2e per kWh
            }
            case FOOD -> {
                String mealType = details.getOrDefault("mealType", "vegetarian");
                yield switch (mealType.toLowerCase()) {
                    case "beef" -> 7.0;           // High emissions
                    case "pork", "lamb" -> 4.0;   // Medium-high
                    case "chicken" -> 2.5;        // Medium
                    case "fish" -> 2.0;           // Medium-low
                    case "vegetarian" -> 0.8;     // Low
                    case "vegan" -> 0.5;          // Very low
                    default -> 2.0;
                };
            }
            case WASTE -> {
                Double weight = Double.parseDouble(details.getOrDefault("weight", "0"));
                String wasteType = details.getOrDefault("wasteType", "general");
                yield weight * switch (wasteType.toLowerCase()) {
                    case "plastic" -> 6.0;     // High emissions
                    case "general" -> 2.5;     // Medium
                    case "organic" -> 0.5;     // Low (compostable)
                    case "recycled" -> 0.2;    // Very low
                    default -> 2.0;
                };
            }
            case WATER -> {
                Double liters = Double.parseDouble(details.getOrDefault("liters", "0"));
                yield liters * 0.0003; // 0.3g CO2e per liter
            }
            case SHOPPING -> {
                Double amount = Double.parseDouble(details.getOrDefault("amount", "0"));
                String itemType = details.getOrDefault("itemType", "general");
                yield amount * switch (itemType.toLowerCase()) {
                    case "electronics" -> 50.0;  // High emissions
                    case "clothing" -> 10.0;     // Medium-high
                    case "food" -> 2.0;          // Medium
                    case "books" -> 1.0;         // Low
                    default -> 5.0;
                };
            }
            default -> 0.0;
        };
    }

    /**
     * Save activity with CO2e calculation
     */
    @Transactional
    public Activity saveActivity(ActivityDTO activityDTO) {
         //("Saving new activity of type: {}", activityDTO.getType());

        // Validate activity type
        validateActivityType(activityDTO);

        // Attach user ID and date
        attachUserId(activityDTO);
        attachCurrentDate(activityDTO);

        // Calculate CO2e emissions
        Double co2e = calculateCO2e(activityDTO);
        activityDTO.setCo2e(co2e);

        // Convert DTO to Entity
        Activity activity = new Activity();
        activity.setUserId(activityDTO.getUserId());
        activity.setDate(activityDTO.getDate());
        activity.setType(activityDTO.getType());
        activity.setDetails(activityDTO.getDetails());
        activity.setCo2e(co2e);

        Activity savedActivity = activityRepository.save(activity);
         //("Activity saved successfully with ID: {}", savedActivity.getId());

        return savedActivity;
    }

    /**
     * Get all activities for the current user
     */
    public List<Activity> getUserActivities() {
        Long userId = getCurrentUserId();
        return activityRepository.findByUserId(userId);
    }

    /**
     * Get activities by date range for the current user
     */
    public List<Activity> getActivitiesByDateRange(LocalDate startDate, LocalDate endDate) {
        Long userId = getCurrentUserId();
        return activityRepository.findByUserIdAndDateBetween(
                userId,
                startDate.toString(),
                endDate.toString()
        );
    }

    /**
     * Calculate total emissions for a user in a given period
     */
    public Double calculateTotalEmissions(LocalDate startDate, LocalDate endDate) {
        List<Activity> activities = getActivitiesByDateRange(startDate, endDate);
        return activities.stream()
                .mapToDouble(Activity::getCo2e)
                .sum();
    }

    // Inner class for Climatiq API response
    private static class ClimatiqResponse {
        private Double co2e;
        private String co2e_unit;

        public Double getCo2e() {
            return co2e;
        }

        public void setCo2e(Double co2e) {
            this.co2e = co2e;
        }

        public String getCo2e_unit() {
            return co2e_unit;
        }

        public void setCo2e_unit(String co2e_unit) {
            this.co2e_unit = co2e_unit;
        }
    }
}
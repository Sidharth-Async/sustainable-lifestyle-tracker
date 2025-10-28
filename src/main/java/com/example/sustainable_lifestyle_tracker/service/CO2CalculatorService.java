package com.example.sustainable_lifestyle_tracker.service;

import com.example.sustainable_lifestyle_tracker.dto.ActivityDTO;
import org.springframework.stereotype.Service;

@Service
public class CO2CalculatorService {

    public Double calculateCO2e(ActivityDTO activityDTO) {
        if (activityDTO == null || activityDTO.getType() == null) {
            return 0.0;
        }

        return switch (activityDTO.getType()) {
            case TRANSPORTATION -> calculateTransportEmissions(activityDTO);
            case WASTE -> calculateWasteEmissions(activityDTO);
            case ENERGY -> calculateEnergyEmissions(activityDTO);
            case FOOD, WATER, SHOPPING -> calculateDeliveryEmissions(activityDTO);
        };
    }

    private Double calculateTransportEmissions(ActivityDTO activityDTO) {
        if (activityDTO.getDistance() == null || activityDTO.getVehicleType() == null) {
            return 0.0;
        }

        double distance = activityDTO.getDistance();
        String vehicleType = activityDTO.getVehicleType().toUpperCase();

        // Emission factors in kg CO2e per km
        double emissionFactor = switch (vehicleType) {
            case "CAR" -> 0.192;
            case "MOTORCYCLE" -> 0.103;
            case "BUS" -> 0.089;
            case "TRAIN" -> 0.041;
            case "BICYCLE" -> 0.0;
            default -> 0.15;
        };

        return distance * emissionFactor;
    }

    private Double calculateWasteEmissions(ActivityDTO activityDTO) {
        if (activityDTO.getWeight() == null || activityDTO.getWasteType() == null) {
            return 0.0;
        }

        double weight = activityDTO.getWeight();
        String wasteType = activityDTO.getWasteType().toUpperCase();

        // Emission factors in kg CO2e per kg of waste
        double emissionFactor = switch (wasteType) {
            case "PLASTIC" -> 6.0;
            case "PAPER" -> 1.3;
            case "GLASS" -> 0.5;
            case "METAL" -> 1.5;
            case "ORGANIC" -> 0.2;
            case "ELECTRONIC" -> 8.0;
            default -> 1.0;
        };

        return weight * emissionFactor;
    }

    private Double calculateEnergyEmissions(ActivityDTO activityDTO) {
        if (activityDTO.getConsumption() == null || activityDTO.getEnergySource() == null) {
            return 0.0;
        }

        double consumption = activityDTO.getConsumption();
        String energySource = activityDTO.getEnergySource().toUpperCase();

        // Emission factors in kg CO2e per kWh
        double emissionFactor = switch (energySource) {
            case "ELECTRICITY" -> 0.475;
            case "NATURAL_GAS" -> 0.185;
            case "SOLAR" -> 0.0;
            case "WIND" -> 0.0;
            default -> 0.475;
        };

        return consumption * emissionFactor;
    }

    private Double calculateDeliveryEmissions(ActivityDTO activityDTO) {
        if (activityDTO.getDeliveryType() == null) {
            return 0.0;
        }

        String deliveryType = activityDTO.getDeliveryType().toUpperCase();

        // Emission factors in kg CO2e per delivery
        return switch (deliveryType) {
            case "STANDARD" -> 2.5;
            case "EXPRESS" -> 5.0;
            case "OVERNIGHT" -> 8.0;
            case "LOCAL" -> 1.0;
            default -> 2.5;
        };
    }
}
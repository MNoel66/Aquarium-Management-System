package com.ey.aquarium.controller;

import com.ey.aquarium.model.FeedingSchedule;
import com.ey.aquarium.service.FeedingScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/feeding-schedules")
public class FeedingScheduleController {
	
	  
    @Autowired
    private FeedingScheduleService feedingScheduleService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<?> getAllSchedules() {
        try {
            return ResponseEntity.ok(feedingScheduleService.getAllSchedules());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching feeding schedules: " + e.getMessage());
        }
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<?> createSchedule(@RequestBody Map<String, Object> request) {
        try {
            // Validate required fields
            if (request.get("foodType") == null || request.get("foodType").toString().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Food type is required");
            }
            if (request.get("feedingTime") == null || request.get("feedingTime").toString().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Feeding time is required");
            }
            if (request.get("quantity") == null) {
                return ResponseEntity.badRequest().body("Quantity is required");
            }
            if (request.get("tankId") == null) {
                return ResponseEntity.badRequest().body("Tank ID is required");
            }
            if (request.get("staffId") == null) {
                return ResponseEntity.badRequest().body("Staff ID is required");
            }
            
            FeedingSchedule schedule = new FeedingSchedule();
            schedule.setFoodType(request.get("foodType").toString().trim());
            
            // Parse feeding time - handle both "HH:MM" and "HH:MM:SS" formats
            String feedingTimeStr = request.get("feedingTime").toString().trim();
            java.time.LocalTime feedingTime;
            if (feedingTimeStr.length() == 5) {
                // Format: "HH:MM"
                feedingTime = java.time.LocalTime.parse(feedingTimeStr);
            } else if (feedingTimeStr.length() == 8) {
                // Format: "HH:MM:SS"
                feedingTime = java.time.LocalTime.parse(feedingTimeStr);
            } else {
                return ResponseEntity.badRequest().body("Invalid feeding time format. Use HH:MM or HH:MM:SS");
            }
            schedule.setFeedingTime(feedingTime);
            
            // Parse quantity
            Integer quantity;
            if (request.get("quantity") instanceof Integer) {
                quantity = (Integer) request.get("quantity");
            } else if (request.get("quantity") instanceof Number) {
                quantity = ((Number) request.get("quantity")).intValue();
            } else {
                quantity = Integer.parseInt(request.get("quantity").toString());
            }
            schedule.setQuantity(quantity);
            
            // Notes is optional
            if (request.get("notes") != null) {
                schedule.setNotes(request.get("notes").toString());
            }
            
            // Parse IDs
            Long tankId;
            if (request.get("tankId") instanceof Number) {
                tankId = ((Number) request.get("tankId")).longValue();
            } else {
                tankId = Long.parseLong(request.get("tankId").toString());
            }
            
            Long staffId;
            if (request.get("staffId") instanceof Number) {
                staffId = ((Number) request.get("staffId")).longValue();
            } else {
                staffId = Long.parseLong(request.get("staffId").toString());
            }
            
            return ResponseEntity.ok(feedingScheduleService.createSchedule(schedule, tankId, staffId));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid number format: " + e.getMessage());
        } catch (java.time.format.DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid time format. Use HH:MM or HH:MM:SS");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating schedule: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<FeedingSchedule> completeFeeding(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(feedingScheduleService.completeFeeding(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/tank/{tankId}")
    public ResponseEntity<?> getSchedulesByTank(@PathVariable Long tankId) {
        try {
            return ResponseEntity.ok(feedingScheduleService.getSchedulesByTank(tankId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching feeding schedules by tank: " + e.getMessage());
        }
    }
    
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<?> getSchedulesByStaff(@PathVariable Long staffId) {
        try {
            return ResponseEntity.ok(feedingScheduleService.getSchedulesByStaff(staffId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching feeding schedules by staff: " + e.getMessage());
        }
    }
}




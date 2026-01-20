package com.ey.aquarium.controller;


import com.ey.aquarium.model.Maintenance;
import com.ey.aquarium.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/maintenances")
public class MaintenanceController {
	
	 @Autowired
	    private MaintenanceService maintenanceService;
	    
	    @GetMapping
	    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	    public ResponseEntity<?> getAllMaintenances() {
	        try {
	            return ResponseEntity.ok(maintenanceService.getAllMaintenances());
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).body("Error fetching maintenances: " + e.getMessage());
	        }
	    }
	    
	    @PostMapping
	    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	    public ResponseEntity<?> createMaintenance(@RequestBody Map<String, Object> request) {
	        try {
	            // Validate required fields
	            if (request.get("type") == null || request.get("type").toString().trim().isEmpty()) {
	                return ResponseEntity.badRequest().body("Type is required");
	            }
	            if (request.get("scheduledDate") == null || request.get("scheduledDate").toString().trim().isEmpty()) {
	                return ResponseEntity.badRequest().body("Scheduled date is required");
	            }
	            if (request.get("tankId") == null) {
	                return ResponseEntity.badRequest().body("Tank ID is required");
	            }
	            if (request.get("staffId") == null) {
	                return ResponseEntity.badRequest().body("Staff ID is required");
	            }
	            
	            Maintenance maintenance = new Maintenance();
	            try {
	                maintenance.setType(Maintenance.MaintenanceType.valueOf(request.get("type").toString().toUpperCase()));
	            } catch (IllegalArgumentException e) {
	                return ResponseEntity.badRequest().body("Invalid maintenance type. Valid types: CLEANING, WATER_CHANGE, FILTER_REPLACEMENT, EQUIPMENT_CHECK");
	            }
	            
	            if (request.get("description") != null) {
	                maintenance.setDescription(request.get("description").toString());
	            }
	            
	            try {
	                maintenance.setScheduledDate(java.time.LocalDateTime.parse(request.get("scheduledDate").toString()));
	            } catch (java.time.format.DateTimeParseException e) {
	                return ResponseEntity.badRequest().body("Invalid date format. Use ISO format: yyyy-MM-ddTHH:mm");
	            }
	            
	            if (request.get("notes") != null) {
	                maintenance.setNotes(request.get("notes").toString());
	            }
	            
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
	            
	            return ResponseEntity.ok(maintenanceService.createMaintenance(maintenance, tankId, staffId));
	        } catch (NumberFormatException e) {
	            return ResponseEntity.badRequest().body("Invalid number format: " + e.getMessage());
	        } catch (RuntimeException e) {
	            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().body("Error creating maintenance: " + e.getMessage());
	        }
	    }
	    
	    @PutMapping("/{id}/status")
	    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	    public ResponseEntity<Maintenance> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
	        try {
	            Maintenance.MaintenanceStatus status = Maintenance.MaintenanceStatus.valueOf(
	                    request.get("status").toUpperCase());
	            return ResponseEntity.ok(maintenanceService.updateMaintenanceStatus(id, status));
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().build();
	        }
	    }
	    
	    @GetMapping("/tank/{tankId}")
	    public ResponseEntity<?> getMaintenancesByTank(@PathVariable Long tankId) {
	        try {
	            return ResponseEntity.ok(maintenanceService.getMaintenancesByTank(tankId));
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).body("Error fetching maintenances by tank: " + e.getMessage());
	        }
	    }
	    
	    @GetMapping("/staff/{staffId}")
	    public ResponseEntity<?> getMaintenancesByStaff(@PathVariable Long staffId) {
	        try {
	            return ResponseEntity.ok(maintenanceService.getMaintenancesByStaff(staffId));
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).body("Error fetching maintenances by staff: " + e.getMessage());
	        }
	    }

}

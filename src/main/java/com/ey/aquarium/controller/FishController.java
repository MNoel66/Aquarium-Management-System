package com.ey.aquarium.controller;

import com.ey.aquarium.model.Fish;
import com.ey.aquarium.service.FishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fishes")
public class FishController {
	
	   @Autowired
	    private FishService fishService;
	    
	    @GetMapping
	    public ResponseEntity<?> getAllFishes() {
	        try {
	            return ResponseEntity.ok(fishService.getAllFishes());
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).body("Error fetching fishes: " + e.getMessage());
	        }
	    }
	    
	    @GetMapping("/{id}")
	    public ResponseEntity<Fish> getFishById(@PathVariable Long id) {
	        return fishService.getFishById(id)
	                .map(ResponseEntity::ok)
	                .orElse(ResponseEntity.notFound().build());
	    }
	    
	    @PostMapping
	    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	    public ResponseEntity<?> createFish(@RequestBody Map<String, Object> request) {
	        try {
	            // Validate required fields
	            if (request.get("speciesName") == null || request.get("speciesName").toString().trim().isEmpty()) {
	                return ResponseEntity.badRequest().body("Species name is required");
	            }
	            if (request.get("commonName") == null || request.get("commonName").toString().trim().isEmpty()) {
	                return ResponseEntity.badRequest().body("Common name is required");
	            }
	            if (request.get("count") == null) {
	                return ResponseEntity.badRequest().body("Count is required");
	            }
	            if (request.get("healthCondition") == null || request.get("healthCondition").toString().trim().isEmpty()) {
	                return ResponseEntity.badRequest().body("Health condition is required. Use: HEALTHY, SICK, RECOVERING, or CRITICAL");
	            }
	            if (request.get("tankId") == null) {
	                return ResponseEntity.badRequest().body("Tank ID is required");
	            }
	            
	            Fish fish = new Fish();
	            fish.setSpeciesName(request.get("speciesName").toString().trim());
	            fish.setCommonName(request.get("commonName").toString().trim());
	            
	            // Parse count
	            Integer count;
	            if (request.get("count") instanceof Integer) {
	                count = (Integer) request.get("count");
	            } else if (request.get("count") instanceof Number) {
	                count = ((Number) request.get("count")).intValue();
	            } else {
	                count = Integer.parseInt(request.get("count").toString());
	            }
	            fish.setCount(count);
	            
	            // Parse health condition
	            try {
	                String healthConditionStr = request.get("healthCondition").toString().toUpperCase().trim();
	                fish.setHealthCondition(Fish.HealthCondition.valueOf(healthConditionStr));
	            } catch (IllegalArgumentException e) {
	                return ResponseEntity.badRequest().body("Invalid health condition. Valid values: HEALTHY, SICK, RECOVERING, CRITICAL");
	            }
	            
	            // Optional fields
	            if (request.get("habitatRequirements") != null) {
	                fish.setHabitatRequirements(request.get("habitatRequirements").toString());
	            }
	            if (request.get("notes") != null) {
	                fish.setNotes(request.get("notes").toString());
	            }
	            
	            // Parse tank ID
	            Long tankId;
	            if (request.get("tankId") instanceof Number) {
	                tankId = ((Number) request.get("tankId")).longValue();
	            } else {
	                tankId = Long.parseLong(request.get("tankId").toString());
	            }
	            
	            return ResponseEntity.ok(fishService.createFish(fish, tankId));
	        } catch (NumberFormatException e) {
	            return ResponseEntity.badRequest().body("Invalid number format: " + e.getMessage());
	        } catch (RuntimeException e) {
	            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().body("Error creating fish: " + e.getMessage());
	        }
	    }
	    
	    @PutMapping("/{id}")
	    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	    public ResponseEntity<Fish> updateFish(@PathVariable Long id, @RequestBody Fish fish) {
	        try {
	            return ResponseEntity.ok(fishService.updateFish(id, fish));
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    
	    @PutMapping("/{id}/move")
	    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	    public ResponseEntity<Fish> moveFish(@PathVariable Long id, @RequestBody Map<String, Long> request) {
	        try {
	            Long newTankId = request.get("tankId");
	            return ResponseEntity.ok(fishService.moveFish(id, newTankId));
	        } catch (RuntimeException e) {
	            return ResponseEntity.badRequest().build();
	        }
	    }
	    
	    @DeleteMapping("/{id}")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<?> deleteFish(@PathVariable Long id) {
	        try {
	            fishService.deleteFish(id);
	            return ResponseEntity.ok().build();
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    
	    @GetMapping("/tank/{tankId}")
	    public ResponseEntity<List<Fish>> getFishesByTank(@PathVariable Long tankId) {
	        return ResponseEntity.ok(fishService.getFishesByTank(tankId));
	    }
	    
	    @GetMapping("/health/{condition}")
	    public ResponseEntity<List<Fish>> getFishesByHealthCondition(@PathVariable String condition) {
	        return ResponseEntity.ok(fishService.getFishesByHealthCondition(
	                Fish.HealthCondition.valueOf(condition.toUpperCase())));
	    }
}




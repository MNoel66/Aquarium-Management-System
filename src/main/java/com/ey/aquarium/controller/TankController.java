package com.ey.aquarium.controller;

import com.ey.aquarium.model.Tank;
import com.ey.aquarium.service.TankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tanks")
public class TankController {
	
	  
    @Autowired
    private TankService tankService;
    
    @GetMapping
    public ResponseEntity<List<Tank>> getAllTanks() {
        return ResponseEntity.ok(tankService.getAllTanks());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Tank> getTankById(@PathVariable Long id) {
        return tankService.getTankById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTank(@RequestBody Tank tank) {
        try {
            // Validate required fields
            if (tank.getName() == null || tank.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Tank name is required");
            }
            if (tank.getTankNumber() == null || tank.getTankNumber().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Tank number is required");
            }
            if (tank.getSize() == null || tank.getSize() <= 0) {
                return ResponseEntity.badRequest().body("Tank size must be greater than 0");
            }
            if (tank.getWaterType() == null) {
                return ResponseEntity.badRequest().body("Water type is required. Use 'FRESH' or 'SALT'");
            }
            if (tank.getCapacity() == null || tank.getCapacity() <= 0) {
                return ResponseEntity.badRequest().body("Tank capacity must be greater than 0");
            }
            
            // Trim name and tankNumber
            tank.setName(tank.getName().trim());
            tank.setTankNumber(tank.getTankNumber().trim());
            
            return ResponseEntity.ok(tankService.createTank(tank));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating tank: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tank> updateTank(@PathVariable Long id, @RequestBody Tank tank) {
        try {
            return ResponseEntity.ok(tankService.updateTank(id, tank));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTank(@PathVariable Long id) {
        try {
            tankService.deleteTank(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Tank>> getTanksByStatus(@PathVariable String status) {
        return ResponseEntity.ok(tankService.getTanksByStatus(Tank.TankStatus.valueOf(status.toUpperCase())));
    }
    
    @GetMapping("/water-type/{waterType}")
    public ResponseEntity<List<Tank>> getTanksByWaterType(@PathVariable String waterType) {
        return ResponseEntity.ok(tankService.getTanksByWaterType(Tank.WaterType.valueOf(waterType.toUpperCase())));
    }

}

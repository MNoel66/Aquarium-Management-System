package com.ey.aquarium.controller;

import com.ey.aquarium.model.WaterQuality;
import com.ey.aquarium.service.WaterQualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/water-quality")
public class WaterQualityController {
	
	 
    @Autowired
    private WaterQualityService waterQualityService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<WaterQuality>> getAllRecords() {
        return ResponseEntity.ok(waterQualityService.getAllRecords());
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<?> recordWaterQuality(@RequestBody Map<String, Object> request) {
        try {
            // Validate required fields
            if (request.get("temperature") == null) {
                return ResponseEntity.badRequest().body("Temperature is required");
            }
            if (request.get("pHLevel") == null) {
                return ResponseEntity.badRequest().body("pH Level is required");
            }
            if (request.get("tankId") == null) {
                return ResponseEntity.badRequest().body("Tank ID is required");
            }
            
            WaterQuality waterQuality = new WaterQuality();
            
            Double temperature;
            if (request.get("temperature") instanceof Number) {
                temperature = ((Number) request.get("temperature")).doubleValue();
            } else {
                temperature = Double.parseDouble(request.get("temperature").toString());
            }
            waterQuality.setTemperature(temperature);
            
            Double pHLevel;
            if (request.get("pHLevel") instanceof Number) {
                pHLevel = ((Number) request.get("pHLevel")).doubleValue();
            } else {
                pHLevel = Double.parseDouble(request.get("pHLevel").toString());
            }
            waterQuality.setpHLevel(pHLevel);
            
            if (request.get("ammoniaLevel") != null) {
                Double ammoniaLevel;
                if (request.get("ammoniaLevel") instanceof Number) {
                    ammoniaLevel = ((Number) request.get("ammoniaLevel")).doubleValue();
                } else {
                    ammoniaLevel = Double.parseDouble(request.get("ammoniaLevel").toString());
                }
                waterQuality.setAmmoniaLevel(ammoniaLevel);
            }
            if (request.get("nitriteLevel") != null) {
                Double nitriteLevel;
                if (request.get("nitriteLevel") instanceof Number) {
                    nitriteLevel = ((Number) request.get("nitriteLevel")).doubleValue();
                } else {
                    nitriteLevel = Double.parseDouble(request.get("nitriteLevel").toString());
                }
                waterQuality.setNitriteLevel(nitriteLevel);
            }
            if (request.get("nitrateLevel") != null) {
                Double nitrateLevel;
                if (request.get("nitrateLevel") instanceof Number) {
                    nitrateLevel = ((Number) request.get("nitrateLevel")).doubleValue();
                } else {
                    nitrateLevel = Double.parseDouble(request.get("nitrateLevel").toString());
                }
                waterQuality.setNitrateLevel(nitrateLevel);
            }
            if (request.get("notes") != null) {
                waterQuality.setNotes(request.get("notes").toString());
            }
            
            Long tankId;
            if (request.get("tankId") instanceof Number) {
                tankId = ((Number) request.get("tankId")).longValue();
            } else {
                tankId = Long.parseLong(request.get("tankId").toString());
            }
            
            return ResponseEntity.ok(waterQualityService.recordWaterQuality(waterQuality, tankId));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid number format: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error recording water quality: " + e.getMessage());
        }
    }
    
    @GetMapping("/tank/{tankId}")
    public ResponseEntity<List<WaterQuality>> getWaterQualityByTank(@PathVariable Long tankId) {
        return ResponseEntity.ok(waterQualityService.getWaterQualityByTank(tankId));
    }
    
    @GetMapping("/critical")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<WaterQuality>> getCriticalWaterQuality() {
        return ResponseEntity.ok(waterQualityService.getCriticalWaterQuality());
    }
	
}

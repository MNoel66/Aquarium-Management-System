package com.ey.aquarium.service;

import com.ey.aquarium.model.Tank;
import com.ey.aquarium.model.WaterQuality;
import com.ey.aquarium.repository.TankRepository;
import com.ey.aquarium.repository.WaterQualityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WaterQualityService {
	   
    @Autowired
    private WaterQualityRepository waterQualityRepository;
    
    @Autowired
    private TankRepository tankRepository;
    
    public WaterQuality recordWaterQuality(WaterQuality waterQuality, Long tankId) {
        Tank tank = tankRepository.findById(tankId)
                .orElseThrow(() -> new RuntimeException("Tank not found"));
        waterQuality.setTank(tank);
        
        // Determine status based on pH and temperature
        if (waterQuality.getpHLevel() < 6.5 || waterQuality.getpHLevel() > 8.5 ||
            waterQuality.getTemperature() < 20 || waterQuality.getTemperature() > 30) {
            waterQuality.setStatus(WaterQuality.QualityStatus.CRITICAL);
        } else if (waterQuality.getpHLevel() < 7.0 || waterQuality.getpHLevel() > 8.0 ||
                   waterQuality.getTemperature() < 22 || waterQuality.getTemperature() > 28) {
            waterQuality.setStatus(WaterQuality.QualityStatus.WARNING);
        } else {
            waterQuality.setStatus(WaterQuality.QualityStatus.NORMAL);
        }
        
        return waterQualityRepository.save(waterQuality);
    }
    
    @Transactional(readOnly = true)
    public List<WaterQuality> getWaterQualityByTank(Long tankId) {
        return waterQualityRepository.findByTankIdOrderByRecordedAtDesc(tankId);
    }
    
    @Transactional(readOnly = true)
    public List<WaterQuality> getCriticalWaterQuality() {
        return waterQualityRepository.findByStatus(WaterQuality.QualityStatus.CRITICAL);
    }
    
    @Transactional(readOnly = true)
    public List<WaterQuality> getAllRecords() {
        return waterQualityRepository.findAllWithTank();
    }

}

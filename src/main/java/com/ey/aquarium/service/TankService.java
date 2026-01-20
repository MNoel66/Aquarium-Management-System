package com.ey.aquarium.service;

import com.ey.aquarium.model.Tank;
import com.ey.aquarium.repository.TankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TankService {
    
    @Autowired
    private TankRepository tankRepository;
    
    public List<Tank> getAllTanks() {
        return tankRepository.findAll();
    }
    
    public Optional<Tank> getTankById(Long id) {
        return tankRepository.findById(id);
    }
    
    public Tank createTank(Tank tank) {
        // Check if tank number already exists
        if (tankRepository.existsByTankNumber(tank.getTankNumber())) {
            throw new RuntimeException("Tank number already exists: " + tank.getTankNumber());
        }
        return tankRepository.save(tank);
    }
    
    public Tank updateTank(Long id, Tank tankDetails) {
        Tank tank = tankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tank not found with id: " + id));
        
        tank.setName(tankDetails.getName());
        tank.setTankNumber(tankDetails.getTankNumber());
        tank.setSize(tankDetails.getSize());
        tank.setWaterType(tankDetails.getWaterType());
        tank.setCapacity(tankDetails.getCapacity());
        tank.setDescription(tankDetails.getDescription());
        tank.setStatus(tankDetails.getStatus());
        
        return tankRepository.save(tank);
    }
    
    public void deleteTank(Long id) {
        Tank tank = tankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tank not found with id: " + id));
        tankRepository.delete(tank);
    }
    
    public List<Tank> getTanksByStatus(Tank.TankStatus status) {
        return tankRepository.findByStatus(status);
    }
    
    public List<Tank> getTanksByWaterType(Tank.WaterType waterType) {
        return tankRepository.findByWaterType(waterType);
    }

}

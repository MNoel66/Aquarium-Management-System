package com.ey.aquarium.service;

import com.ey.aquarium.model.Fish;
import com.ey.aquarium.model.Tank;
import com.ey.aquarium.repository.FishRepository;
import com.ey.aquarium.repository.TankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FishService {
    
    @Autowired
    private FishRepository fishRepository;
    
    @Autowired
    private TankRepository tankRepository;
    
    @Transactional(readOnly = true)
    public List<Fish> getAllFishes() {
        return fishRepository.findAllWithTank();
    }
    
    public Optional<Fish> getFishById(Long id) {
        return fishRepository.findById(id);
    }
    
    public Fish createFish(Fish fish, Long tankId) {
        Tank tank = tankRepository.findById(tankId)
                .orElseThrow(() -> new RuntimeException("Tank not found with id: " + tankId));
        fish.setTank(tank);
        return fishRepository.save(fish);
    }
    
    public Fish updateFish(Long id, Fish fishDetails) {
        Fish fish = fishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fish not found with id: " + id));
        
        fish.setSpeciesName(fishDetails.getSpeciesName());
        fish.setCommonName(fishDetails.getCommonName());
        fish.setCount(fishDetails.getCount());
        fish.setHealthCondition(fishDetails.getHealthCondition());
        fish.setHabitatRequirements(fishDetails.getHabitatRequirements());
        fish.setNotes(fishDetails.getNotes());
        
        return fishRepository.save(fish);
    }
    
    public Fish moveFish(Long fishId, Long newTankId) {
        Fish fish = fishRepository.findById(fishId)
                .orElseThrow(() -> new RuntimeException("Fish not found with id: " + fishId));
        
        Tank newTank = tankRepository.findById(newTankId)
                .orElseThrow(() -> new RuntimeException("Tank not found with id: " + newTankId));
        
        fish.setTank(newTank);
        return fishRepository.save(fish);
    }
    
    public void removeFish(Long id) {
        Fish fish = fishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fish not found with id: " + id));
        fish.setRemovedAt(java.time.LocalDateTime.now());
        fishRepository.save(fish);
    }
    
    public void deleteFish(Long id) {
        fishRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Fish> getFishesByTank(Long tankId) {
        return fishRepository.findByTankId(tankId);
    }
    
    @Transactional(readOnly = true)
    public List<Fish> getFishesByHealthCondition(Fish.HealthCondition condition) {
        return fishRepository.findByHealthCondition(condition);
    }

}

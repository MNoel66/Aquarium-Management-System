package com.ey.aquarium.service;

import com.ey.aquarium.model.Maintenance;
import com.ey.aquarium.model.Tank;
import com.ey.aquarium.model.User;
import com.ey.aquarium.repository.MaintenanceRepository;
import com.ey.aquarium.repository.TankRepository;
import com.ey.aquarium.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MaintenanceService {
    
    @Autowired
    private MaintenanceRepository maintenanceRepository;
    
    @Autowired
    private TankRepository tankRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public List<Maintenance> getAllMaintenances() {
        return maintenanceRepository.findAllWithRelations();
    }
    
    public Maintenance createMaintenance(Maintenance maintenance, Long tankId, Long staffId) {
        Tank tank = tankRepository.findById(tankId)
                .orElseThrow(() -> new RuntimeException("Tank not found"));
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        maintenance.setTank(tank);
        maintenance.setStaff(staff);
        return maintenanceRepository.save(maintenance);
    }
    
    public Maintenance updateMaintenanceStatus(Long id, Maintenance.MaintenanceStatus status) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance not found"));
        maintenance.setStatus(status);
        if (status == Maintenance.MaintenanceStatus.COMPLETED) {
            maintenance.setCompletedDate(LocalDateTime.now());
        }
        return maintenanceRepository.save(maintenance);
    }
    
    @Transactional(readOnly = true)
    public List<Maintenance> getMaintenancesByTank(Long tankId) {
        return maintenanceRepository.findByTankId(tankId);
    }
    
    @Transactional(readOnly = true)
    public List<Maintenance> getMaintenancesByStaff(Long staffId) {
        return maintenanceRepository.findByStaffId(staffId);
    }
    
    @Transactional(readOnly = true)
    public List<Maintenance> getMaintenancesByStatus(Maintenance.MaintenanceStatus status) {
        return maintenanceRepository.findByStatus(status);
    }

}

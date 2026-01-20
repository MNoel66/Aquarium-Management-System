package com.ey.aquarium.repository;

import com.ey.aquarium.model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    @Query("SELECT m FROM Maintenance m LEFT JOIN FETCH m.tank LEFT JOIN FETCH m.staff")
    List<Maintenance> findAllWithRelations();
    
    @Query("SELECT m FROM Maintenance m LEFT JOIN FETCH m.tank LEFT JOIN FETCH m.staff WHERE m.tank.id = :tankId")
    List<Maintenance> findByTankId(Long tankId);
    
    @Query("SELECT m FROM Maintenance m LEFT JOIN FETCH m.tank LEFT JOIN FETCH m.staff WHERE m.staff.id = :staffId")
    List<Maintenance> findByStaffId(Long staffId);
    
    @Query("SELECT m FROM Maintenance m LEFT JOIN FETCH m.tank LEFT JOIN FETCH m.staff WHERE m.status = :status")
    List<Maintenance> findByStatus(Maintenance.MaintenanceStatus status);

}

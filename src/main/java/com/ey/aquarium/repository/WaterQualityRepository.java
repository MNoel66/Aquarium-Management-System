package com.ey.aquarium.repository;

import com.ey.aquarium.model.WaterQuality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WaterQualityRepository extends JpaRepository<WaterQuality, Long> {
    @Query("SELECT wq FROM WaterQuality wq LEFT JOIN FETCH wq.tank")
    List<WaterQuality> findAllWithTank();
    
    @Query("SELECT wq FROM WaterQuality wq LEFT JOIN FETCH wq.tank WHERE wq.tank.id = :tankId")
    List<WaterQuality> findByTankId(Long tankId);
    
    @Query("SELECT wq FROM WaterQuality wq LEFT JOIN FETCH wq.tank WHERE wq.status = :status")
    List<WaterQuality> findByStatus(WaterQuality.QualityStatus status);
    
    @Query("SELECT wq FROM WaterQuality wq LEFT JOIN FETCH wq.tank WHERE wq.tank.id = :tankId ORDER BY wq.recordedAt DESC")
    List<WaterQuality> findByTankIdOrderByRecordedAtDesc(Long tankId);

}

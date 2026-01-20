package com.ey.aquarium.repository;

import com.ey.aquarium.model.Fish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FishRepository extends JpaRepository<Fish, Long> {
    @Query("SELECT f FROM Fish f LEFT JOIN FETCH f.tank")
    List<Fish> findAllWithTank();
    
    @Query("SELECT f FROM Fish f LEFT JOIN FETCH f.tank WHERE f.tank.id = :tankId")
    List<Fish> findByTankId(Long tankId);
    
    @Query("SELECT f FROM Fish f LEFT JOIN FETCH f.tank WHERE f.healthCondition = :healthCondition")
    List<Fish> findByHealthCondition(Fish.HealthCondition healthCondition);
}

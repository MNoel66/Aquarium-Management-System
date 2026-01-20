package com.ey.aquarium.repository;

import com.ey.aquarium.model.Tank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TankRepository extends JpaRepository<Tank, Long> {
    List<Tank> findByStatus(Tank.TankStatus status);
    List<Tank> findByWaterType(Tank.WaterType waterType);
    boolean existsByTankNumber(String tankNumber);
}

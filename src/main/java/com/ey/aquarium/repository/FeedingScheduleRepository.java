package com.ey.aquarium.repository;

import com.ey.aquarium.model.FeedingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FeedingScheduleRepository extends JpaRepository<FeedingSchedule, Long> {
    @Query("SELECT fs FROM FeedingSchedule fs LEFT JOIN FETCH fs.tank LEFT JOIN FETCH fs.assignedStaff")
    List<FeedingSchedule> findAllWithRelations();
    
    @Query("SELECT fs FROM FeedingSchedule fs LEFT JOIN FETCH fs.tank LEFT JOIN FETCH fs.assignedStaff WHERE fs.tank.id = :tankId")
    List<FeedingSchedule> findByTankId(Long tankId);
    
    @Query("SELECT fs FROM FeedingSchedule fs LEFT JOIN FETCH fs.tank LEFT JOIN FETCH fs.assignedStaff WHERE fs.assignedStaff.id = :staffId")
    List<FeedingSchedule> findByAssignedStaffId(Long staffId);
    
    @Query("SELECT fs FROM FeedingSchedule fs LEFT JOIN FETCH fs.tank LEFT JOIN FETCH fs.assignedStaff WHERE fs.status = :status")
    List<FeedingSchedule> findByStatus(FeedingSchedule.FeedingStatus status);

}

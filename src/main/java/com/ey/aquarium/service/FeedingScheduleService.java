package com.ey.aquarium.service;
import com.ey.aquarium.model.FeedingSchedule;
import com.ey.aquarium.model.Tank;
import com.ey.aquarium.model.User;
import com.ey.aquarium.repository.FeedingScheduleRepository;
import com.ey.aquarium.repository.TankRepository;
import com.ey.aquarium.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FeedingScheduleService {
	   
    @Autowired
    private FeedingScheduleRepository feedingScheduleRepository;
    
    @Autowired
    private TankRepository tankRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Transactional(readOnly = true)
    public List<FeedingSchedule> getAllSchedules() {
        return feedingScheduleRepository.findAllWithRelations();
    }
    
    public FeedingSchedule createSchedule(FeedingSchedule schedule, Long tankId, Long staffId) {
        Tank tank = tankRepository.findById(tankId)
                .orElseThrow(() -> new RuntimeException("Tank not found"));
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        
        schedule.setTank(tank);
        schedule.setAssignedStaff(staff);
        return feedingScheduleRepository.save(schedule);
    }
    
    public FeedingSchedule completeFeeding(Long id) {
        FeedingSchedule schedule = feedingScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        schedule.setStatus(FeedingSchedule.FeedingStatus.COMPLETED);
        schedule.setCompletedAt(LocalDateTime.now());
        return feedingScheduleRepository.save(schedule);
    }
    
    @Transactional(readOnly = true)
    public List<FeedingSchedule> getSchedulesByTank(Long tankId) {
        return feedingScheduleRepository.findByTankId(tankId);
    }
    
    @Transactional(readOnly = true)
    public List<FeedingSchedule> getSchedulesByStaff(Long staffId) {
        return feedingScheduleRepository.findByAssignedStaffId(staffId);
    }
    
    @Transactional(readOnly = true)
    public List<FeedingSchedule> getSchedulesByStatus(FeedingSchedule.FeedingStatus status) {
        return feedingScheduleRepository.findByStatus(status);
    }

}

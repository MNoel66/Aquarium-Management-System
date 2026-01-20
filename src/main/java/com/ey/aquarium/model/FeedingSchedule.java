package com.ey.aquarium.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "feeding_schedules")
public class FeedingSchedule {
	 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tank_id", nullable = false)
    private Tank tank;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private User assignedStaff;
    
    @NotBlank
    private String foodType;
    
    @NotNull
    private LocalTime feedingTime;
    
    @NotNull
    private Integer quantity; // in grams
    
    private String notes;
    
    @Enumerated(EnumType.STRING)
    private FeedingStatus status = FeedingStatus.PENDING;
    
    private LocalDateTime completedAt;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Tank getTank() {
        return tank;
    }
    
    public void setTank(Tank tank) {
        this.tank = tank;
    }
    
    public User getAssignedStaff() {
        return assignedStaff;
    }
    
    public void setAssignedStaff(User assignedStaff) {
        this.assignedStaff = assignedStaff;
    }
    
    public String getFoodType() {
        return foodType;
    }
    
    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
    
    public LocalTime getFeedingTime() {
        return feedingTime;
    }
    
    public void setFeedingTime(LocalTime feedingTime) {
        this.feedingTime = feedingTime;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public FeedingStatus getStatus() {
        return status;
    }
    
    public void setStatus(FeedingStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public enum FeedingStatus {
        PENDING, COMPLETED, MISSED, CANCELLED
    }

}

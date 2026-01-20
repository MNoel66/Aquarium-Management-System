package com.ey.aquarium.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tanks")
public class Tank {
	  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    @NotBlank
    @Column(unique = true)
    private String tankNumber;
    
    @NotNull
    @Positive
    private Double size; // in gallons
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private WaterType waterType;
    
    @NotNull
    @Positive
    private Integer capacity; // maximum fish capacity
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    private TankStatus status = TankStatus.ACTIVE;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "tank", cascade = CascadeType.ALL)
    private Set<Fish> fishes;
    
    @OneToMany(mappedBy = "tank", cascade = CascadeType.ALL)
    private Set<FeedingSchedule> feedingSchedules;
    
    @OneToMany(mappedBy = "tank", cascade = CascadeType.ALL)
    private Set<Maintenance> maintenances;
    
    @OneToMany(mappedBy = "tank", cascade = CascadeType.ALL)
    private Set<WaterQuality> waterQualityRecords;
    
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getTankNumber() {
        return tankNumber;
    }
    
    public void setTankNumber(String tankNumber) {
        this.tankNumber = tankNumber;
    }
    
    public Double getSize() {
        return size;
    }
    
    public void setSize(Double size) {
        this.size = size;
    }
    
    public WaterType getWaterType() {
        return waterType;
    }
    
    public void setWaterType(WaterType waterType) {
        this.waterType = waterType;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public TankStatus getStatus() {
        return status;
    }
    
    public void setStatus(TankStatus status) {
        this.status = status;
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
    
    public enum WaterType {
        FRESH, SALT
    }
    
    public enum TankStatus {
        ACTIVE, INACTIVE, MAINTENANCE
    }

}

package com.ey.aquarium.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Table(name = "fishes")
public class Fish {
	  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String speciesName;
    
    @NotBlank
    private String commonName;
    
    @NotNull
    @Positive
    private Integer count;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private HealthCondition healthCondition;
    
    private String habitatRequirements;
    
    private String notes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tank_id")
    private Tank tank;
    
    private LocalDateTime addedAt;
    
    private LocalDateTime removedAt;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (addedAt == null) {
            addedAt = LocalDateTime.now();
        }
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
    
    public String getSpeciesName() {
        return speciesName;
    }
    
    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }
    
    public String getCommonName() {
        return commonName;
    }
    
    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }
    
    public Integer getCount() {
        return count;
    }
    
    public void setCount(Integer count) {
        this.count = count;
    }
    
    public HealthCondition getHealthCondition() {
        return healthCondition;
    }
    
    public void setHealthCondition(HealthCondition healthCondition) {
        this.healthCondition = healthCondition;
    }
    
    public String getHabitatRequirements() {
        return habitatRequirements;
    }
    
    public void setHabitatRequirements(String habitatRequirements) {
        this.habitatRequirements = habitatRequirements;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Tank getTank() {
        return tank;
    }
    
    public void setTank(Tank tank) {
        this.tank = tank;
    }
    
    public LocalDateTime getAddedAt() {
        return addedAt;
    }
    
    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
    
    public LocalDateTime getRemovedAt() {
        return removedAt;
    }
    
    public void setRemovedAt(LocalDateTime removedAt) {
        this.removedAt = removedAt;
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
    
    public enum HealthCondition {
        HEALTHY, SICK, RECOVERING, CRITICAL
    }

}

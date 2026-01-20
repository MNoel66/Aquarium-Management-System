package com.ey.aquarium.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "water_quality")
public class WaterQuality {
	  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tank_id", nullable = false)
    private Tank tank;
    
    @NotNull
    private Double temperature; // in Celsius
    
    @NotNull
    private Double pHLevel;
    
    private Double ammoniaLevel;
    
    private Double nitriteLevel;
    
    private Double nitrateLevel;
    
    @Enumerated(EnumType.STRING)
    private QualityStatus status = QualityStatus.NORMAL;
    
    private String notes;
    
    private LocalDateTime recordedAt;
    
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (recordedAt == null) {
            recordedAt = LocalDateTime.now();
        }
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
    
    public Double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    
    public Double getpHLevel() {
        return pHLevel;
    }
    
    public void setpHLevel(Double pHLevel) {
        this.pHLevel = pHLevel;
    }
    
    public Double getAmmoniaLevel() {
        return ammoniaLevel;
    }
    
    public void setAmmoniaLevel(Double ammoniaLevel) {
        this.ammoniaLevel = ammoniaLevel;
    }
    
    public Double getNitriteLevel() {
        return nitriteLevel;
    }
    
    public void setNitriteLevel(Double nitriteLevel) {
        this.nitriteLevel = nitriteLevel;
    }
    
    public Double getNitrateLevel() {
        return nitrateLevel;
    }
    
    public void setNitrateLevel(Double nitrateLevel) {
        this.nitrateLevel = nitrateLevel;
    }
    
    public QualityStatus getStatus() {
        return status;
    }
    
    public void setStatus(QualityStatus status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }
    
    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public enum QualityStatus {
        NORMAL, WARNING, CRITICAL
    }

}

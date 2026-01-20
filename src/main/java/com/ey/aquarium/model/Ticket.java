package com.ey.aquarium.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {
	  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visitor_id", nullable = false)
    private User visitor;
    
    @NotBlank
    private String ticketNumber;
    
    @NotNull
    private LocalDateTime visitDate;
    
    @NotNull
    @Positive
    private Double price;
    
    @NotNull
    @Positive
    private Integer numberOfVisitors;
    
    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.PENDING;
    
    private LocalDateTime bookedAt;
    
    private LocalDateTime checkedInAt;
    
    private String notes;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (bookedAt == null) {
            bookedAt = LocalDateTime.now();
        }
        if (ticketNumber == null || ticketNumber.isEmpty()) {
            ticketNumber = "TKT-" + System.currentTimeMillis();
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
    
    public User getVisitor() {
        return visitor;
    }
    
    public void setVisitor(User visitor) {
        this.visitor = visitor;
    }
    
    public String getTicketNumber() {
        return ticketNumber;
    }
    
    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }
    
    public LocalDateTime getVisitDate() {
        return visitDate;
    }
    
    public void setVisitDate(LocalDateTime visitDate) {
        this.visitDate = visitDate;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public Integer getNumberOfVisitors() {
        return numberOfVisitors;
    }
    
    public void setNumberOfVisitors(Integer numberOfVisitors) {
        this.numberOfVisitors = numberOfVisitors;
    }
    
    public TicketStatus getStatus() {
        return status;
    }
    
    public void setStatus(TicketStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getBookedAt() {
        return bookedAt;
    }
    
    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }
    
    public LocalDateTime getCheckedInAt() {
        return checkedInAt;
    }
    
    public void setCheckedInAt(LocalDateTime checkedInAt) {
        this.checkedInAt = checkedInAt;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
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
    
    public enum TicketStatus {
        PENDING, CONFIRMED, CHECKED_IN, CANCELLED, EXPIRED
    }

}

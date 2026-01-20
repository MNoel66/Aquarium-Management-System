package com.ey.aquarium.repository;

import com.ey.aquarium.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t LEFT JOIN FETCH t.visitor WHERE t.visitor.id = :visitorId")
    List<Ticket> findByVisitorId(Long visitorId);
    
    @Query("SELECT t FROM Ticket t LEFT JOIN FETCH t.visitor WHERE t.ticketNumber = :ticketNumber")
    Optional<Ticket> findByTicketNumber(String ticketNumber);
    
    @Query("SELECT t FROM Ticket t LEFT JOIN FETCH t.visitor WHERE t.status = :status")
    List<Ticket> findByStatus(Ticket.TicketStatus status);
    
    @Query("SELECT t FROM Ticket t LEFT JOIN FETCH t.visitor WHERE t.visitDate BETWEEN :start AND :end")
    List<Ticket> findByVisitDateBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT t FROM Ticket t LEFT JOIN FETCH t.visitor")
    List<Ticket> findAllWithVisitor();
}

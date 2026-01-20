package com.ey.aquarium.service;

import com.ey.aquarium.model.Ticket;
import com.ey.aquarium.model.User;
import com.ey.aquarium.repository.TicketRepository;
import com.ey.aquarium.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public Ticket bookTicket(Ticket ticket, Long visitorId) {
        User visitor = userRepository.findById(visitorId)
                .orElseThrow(() -> new RuntimeException("Visitor not found"));
        ticket.setVisitor(visitor);
        ticket.setStatus(Ticket.TicketStatus.CONFIRMED);
        return ticketRepository.save(ticket);
    }
    
    public Ticket checkIn(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setStatus(Ticket.TicketStatus.CHECKED_IN);
        ticket.setCheckedInAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }
    
    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByVisitor(Long visitorId) {
        return ticketRepository.findByVisitorId(visitorId);
    }
    
    @Transactional(readOnly = true)
    public Optional<Ticket> getTicketByNumber(String ticketNumber) {
        return ticketRepository.findByTicketNumber(ticketNumber);
    }
    
    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAllWithVisitor();
    }
    
    @Transactional(readOnly = true)
    public List<Ticket> getTicketsByDateRange(LocalDateTime start, LocalDateTime end) {
        return ticketRepository.findByVisitDateBetween(start, end);
    }

}

package com.ey.aquarium.controller;

import com.ey.aquarium.model.Ticket;
import com.ey.aquarium.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tickets")
public class TicketController {
	
	 @Autowired
	    private TicketService ticketService;
	    
	    @GetMapping
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<?> getAllTickets() {
	        try {
	            return ResponseEntity.ok(ticketService.getAllTickets());
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).body("Error fetching tickets: " + e.getMessage());
	        }
	    }
	    
	    @PostMapping("/book")
	    public ResponseEntity<?> bookTicket(@RequestBody Map<String, Object> request) {
	        try {
	            // Validate required fields
	            if (request.get("visitDate") == null || request.get("visitDate").toString().trim().isEmpty()) {
	                return ResponseEntity.badRequest().body("Error: Visit date is required.");
	            }
	            if (request.get("numberOfVisitors") == null) {
	                return ResponseEntity.badRequest().body("Error: Number of visitors is required.");
	            }
	            if (request.get("visitorId") == null) {
	                return ResponseEntity.badRequest().body("Error: Visitor ID is required.");
	            }
	            
	            Ticket ticket = new Ticket();
	            
	            // Parse visit date - handle ISO format (YYYY-MM-DDTHH:MM:SS) or date only (YYYY-MM-DD)
	            String visitDateStr = request.get("visitDate").toString().trim();
	            java.time.LocalDateTime visitDate;
	            try {
	                if (visitDateStr.contains("T")) {
	                    // ISO format with time: "2026-01-15T10:00:00" or "2026-01-15T10:00:00.000Z"
	                    visitDateStr = visitDateStr.replace("Z", ""); // Remove Z if present
	                    if (visitDateStr.length() == 19) {
	                        // Format: "YYYY-MM-DDTHH:MM:SS"
	                        visitDate = java.time.LocalDateTime.parse(visitDateStr);
	                    } else {
	                        // Format: "YYYY-MM-DDTHH:MM:SS.SSS" - parse and truncate to seconds
	                        visitDate = java.time.LocalDateTime.parse(visitDateStr.substring(0, 19));
	                    }
	                } else {
	                    // Date only format: "YYYY-MM-DD" - set time to 00:00:00
	                    visitDate = java.time.LocalDate.parse(visitDateStr).atStartOfDay();
	                }
	            } catch (java.time.format.DateTimeParseException e) {
	                return ResponseEntity.badRequest().body("Error: Invalid visit date format. Use YYYY-MM-DD or YYYY-MM-DDTHH:MM:SS. " + e.getMessage());
	            }
	            ticket.setVisitDate(visitDate);
	            
	            // Parse price - optional, default to calculated price
	            if (request.get("price") != null) {
	                Double price;
	                if (request.get("price") instanceof Number) {
	                    price = ((Number) request.get("price")).doubleValue();
	                } else {
	                    price = Double.parseDouble(request.get("price").toString());
	                }
	                if (price <= 0) {
	                    return ResponseEntity.badRequest().body("Error: Price must be a positive number.");
	                }
	                ticket.setPrice(price);
	            } else {
	                // Default price calculation: $25 per visitor
	                Integer numberOfVisitors = (Integer) request.get("numberOfVisitors");
	                ticket.setPrice(numberOfVisitors * 25.0);
	            }
	            
	            // Parse number of visitors
	            Integer numberOfVisitors;
	            if (request.get("numberOfVisitors") instanceof Integer) {
	                numberOfVisitors = (Integer) request.get("numberOfVisitors");
	            } else if (request.get("numberOfVisitors") instanceof Number) {
	                numberOfVisitors = ((Number) request.get("numberOfVisitors")).intValue();
	            } else {
	                numberOfVisitors = Integer.parseInt(request.get("numberOfVisitors").toString());
	            }
	            if (numberOfVisitors <= 0) {
	                return ResponseEntity.badRequest().body("Error: Number of visitors must be a positive number.");
	            }
	            ticket.setNumberOfVisitors(numberOfVisitors);
	            
	            // Notes is optional
	            if (request.get("notes") != null) {
	                ticket.setNotes(request.get("notes").toString());
	            }
	            
	            // Parse visitor ID
	            Long visitorId;
	            if (request.get("visitorId") instanceof Number) {
	                visitorId = ((Number) request.get("visitorId")).longValue();
	            } else {
	                visitorId = Long.parseLong(request.get("visitorId").toString());
	            }
	            
	            return ResponseEntity.ok(ticketService.bookTicket(ticket, visitorId));
	        } catch (NumberFormatException e) {
	            return ResponseEntity.badRequest().body("Error: Invalid number format for price, numberOfVisitors, or visitorId. " + e.getMessage());
	        } catch (RuntimeException e) {
	            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.badRequest().body("Error booking ticket: " + e.getMessage());
	        }
	    }
	    
	    @PutMapping("/{id}/check-in")
	    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
	    public ResponseEntity<Ticket> checkIn(@PathVariable Long id) {
	        try {
	            return ResponseEntity.ok(ticketService.checkIn(id));
	        } catch (RuntimeException e) {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    
	    @GetMapping("/visitor/{visitorId}")
	    public ResponseEntity<?> getTicketsByVisitor(@PathVariable Long visitorId) {
	        try {
	            return ResponseEntity.ok(ticketService.getTicketsByVisitor(visitorId));
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).body("Error fetching tickets by visitor: " + e.getMessage());
	        }
	    }
	    
	    @GetMapping("/number/{ticketNumber}")
	    public ResponseEntity<?> getTicketByNumber(@PathVariable String ticketNumber) {
	        try {
	            return ticketService.getTicketByNumber(ticketNumber)
	                    .map(ResponseEntity::ok)
	                    .orElse(ResponseEntity.notFound().build());
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).body("Error fetching ticket by number: " + e.getMessage());
	        }
	    }

}

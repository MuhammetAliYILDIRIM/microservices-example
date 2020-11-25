package com.may.ticketservice.repository;

import com.may.ticketservice.repository.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, String> {
}

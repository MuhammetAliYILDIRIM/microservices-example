package com.may.ticketservice.service;

import com.may.ticketservice.dto.TicketRequest;
import com.may.ticketservice.dto.TicketResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {

    TicketResponse save(TicketRequest ticketRequest);

    TicketResponse update(String ticketId, TicketRequest ticketRequest);

    TicketResponse getById(String ticketId);

    Page<TicketResponse> getPagination(Pageable pageable);
}

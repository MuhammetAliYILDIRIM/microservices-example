package com.may.ticketservice.controller;

import com.may.ticketservice.dto.TicketRequest;
import com.may.ticketservice.dto.TicketResponse;
import com.may.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.may.ticketservice.constants.RestApiUrls.TICKET;
import static com.may.ticketservice.constants.RestApiUrls.TICKET_ID;

@RestController
@RequestMapping(TICKET)
@RequiredArgsConstructor
public class TicketController {


    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<String> createTicket(@RequestBody TicketRequest ticketRequest) {
        ticketService.save(ticketRequest);
        return new ResponseEntity<>("The ticket has been created successfully", HttpStatus.CREATED);
    }

    @PutMapping(TICKET_ID)
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable(value = TICKET_ID) String ticketId,
                                                       @RequestBody TicketRequest ticketRequest) {
        return ResponseEntity.ok(ticketService.update(ticketId, ticketRequest));
    }

    @GetMapping
    public ResponseEntity<Page<TicketResponse>> getAllTickets(Pageable pageable) {

        return ResponseEntity.ok(ticketService.getPagination(pageable));
    }

    @GetMapping(TICKET_ID)
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable(value = TICKET_ID) String ticketId) {

        return ResponseEntity.ok(ticketService.getById(ticketId));
    }


}

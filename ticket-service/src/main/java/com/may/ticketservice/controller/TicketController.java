package com.may.ticketservice.controller;

import com.may.ticketservice.dto.TicketDto;
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
    public ResponseEntity<String> createTicket(@RequestBody TicketDto ticketDto) {
        ticketService.save(ticketDto);
        return new ResponseEntity<String>("The ticket has been created successfully", HttpStatus.CREATED);
    }

    @PutMapping(TICKET_ID)
    public ResponseEntity<TicketDto> updateTicket(@PathVariable(value = TICKET_ID) String ticketId,
                                                  @RequestBody TicketDto ticketDto) {
        return ResponseEntity.ok(ticketService.update(ticketId, ticketDto));
    }

    @GetMapping
    public ResponseEntity<Page<TicketDto>> getAllTickets(Pageable pageable) {

        return ResponseEntity.ok(ticketService.getPagination(pageable));
    }

    @GetMapping(TICKET_ID)
    public ResponseEntity<TicketDto> getTicketById(@PathVariable(value = TICKET_ID) String ticketId) {

        return ResponseEntity.ok(ticketService.getById(ticketId));
    }


}

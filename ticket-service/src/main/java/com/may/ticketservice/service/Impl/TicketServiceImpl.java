package com.may.ticketservice.service.Impl;

import com.may.client.AccountServiceClient;
import com.may.client.contract.AccountEventDto;
import com.may.ticketservice.TicketEvent;
import com.may.ticketservice.dto.TicketRequest;
import com.may.ticketservice.dto.TicketResponse;
import com.may.ticketservice.enums.PriorityType;
import com.may.ticketservice.enums.TicketStatus;
import com.may.ticketservice.exception.TicketServiceException;
import com.may.ticketservice.repository.TicketRepository;
import com.may.ticketservice.repository.entity.Ticket;
import com.may.ticketservice.repository.entity.elasticsearch.TicketElasticRepository;
import com.may.ticketservice.repository.entity.elasticsearch.TicketModel;
import com.may.ticketservice.service.TicketService;
import com.may.ticketservice.service.producer.TicketEventProducer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.may.ticketservice.exception.ErrorType.*;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketElasticRepository ticketElasticRepository;
    private final AccountServiceClient accountServiceClient;
    private final ModelMapper modelMapper;
    private final TicketEventProducer ticketEventProducer;

    @Override
    @Transactional
    public TicketResponse save(TicketRequest ticketRequest) {
        Ticket ticket = new Ticket();
        ResponseEntity<AccountEventDto> account = accountServiceClient.getUserByUserId(ticketRequest.getAssignee());
        if (account.getBody() == null) {
            throw new TicketServiceException(ASSIGNEE_NOT_FOUND);
        }

        if (account.getStatusCode() != HttpStatus.OK) {
            throw new TicketServiceException(INTERNAL_ERROR);
        }

        if (ticketRequest.getDescription() == null) {
            throw new TicketServiceException(DESCRIPTION_CAN_NOT_BE_EMPTY);
        }
        ticket.setDescription(ticketRequest.getDescription());
        ticket.setNotes(ticketRequest.getNotes());
        ticket.setTicketDate(ticketRequest.getTicketDate());
        ticket.setTicketStatus(TicketStatus.valueOf(ticketRequest.getTicketStatus()));
        ticket.setPriorityType(PriorityType.valueOf(ticketRequest.getPriorityType()));
        ticket.setAssignee(account.getBody().getFullName());
        ticket = ticketRepository.save(ticket);

        TicketModel model = TicketModel.builder()
                .assignee(account.getBody().getFullName())
                .description(ticket.getDescription())
                .notes(ticket.getNotes())
                .id(ticket.getId())
                .priorityType(ticket.getPriorityType().toString())
                .ticketStatus(ticket.getTicketStatus().toString())
                .ticketDate(ticket.getTicketDate())
                .build();

        ticketElasticRepository.save(model);

        ticketEventProducer.publish(TicketEvent.newBuilder()
                .setId(ticket.getId())
                .setDescription(ticket.getDescription())
                .setNote(ticket.getNotes())
                .build());


        return modelMapper.map(ticket, TicketResponse.class);
    }

    @Override
    @Transactional
    public TicketResponse update(String ticketId, TicketRequest ticketRequest) {
        Ticket ticket =
                ticketRepository.findById(ticketId).orElseThrow(() -> new TicketServiceException(TICKET_NOT_FOUND));

        ResponseEntity<AccountEventDto> account = accountServiceClient.getUserByUserId(ticketRequest.getAssignee());
        if (account.getBody() == null) {
            throw new TicketServiceException(ASSIGNEE_NOT_FOUND);
        }

        if (account.getStatusCode() != HttpStatus.OK) {
            throw new TicketServiceException(INTERNAL_ERROR);
        }
        ticket.setTicketStatus(TicketStatus.valueOf(ticketRequest.getTicketStatus()));
        ticket.setPriorityType(PriorityType.valueOf(ticketRequest.getPriorityType()));
        ticket.setDescription(ticketRequest.getDescription());
        ticket.setNotes(ticketRequest.getNotes());
        ticket.setTicketDate(ticketRequest.getTicketDate());
        ticket.setAssignee(account.getBody().getFullName());

        ticket = ticketRepository.save(ticket);

        TicketModel model = TicketModel.builder()
                .assignee(account.getBody().getFullName())
                .description(ticket.getDescription())
                .notes(ticket.getNotes())
                .id(ticket.getId())
                .priorityType(ticket.getPriorityType().toString())
                .ticketStatus(ticket.getTicketStatus().toString())
                .ticketDate(ticket.getTicketDate())
                .build();

        ticketElasticRepository.save(model);
        return modelMapper.map(ticket, TicketResponse.class);
    }

    @Override
    public TicketResponse getById(String ticketId) {
        Ticket ticket =
                ticketRepository.findById(ticketId).orElseThrow(() -> new TicketServiceException(TICKET_NOT_FOUND));


        return modelMapper.map(ticket, TicketResponse.class);
    }

    @Override
    public Page<TicketResponse> getPagination(Pageable pageable) {

        return new PageImpl<>(ticketRepository.findAll(pageable).stream().map(ticket -> modelMapper.map(ticket,
                TicketResponse.class)).collect(Collectors.toList()));
    }
}

package com.may.ticketservice.service.Impl;

import com.may.client.AccountServiceClient;
import com.may.client.contract.AccountEventDto;
import com.may.ticketservice.dto.TicketDto;
import com.may.ticketservice.enums.PriorityType;
import com.may.ticketservice.enums.TicketStatus;
import com.may.ticketservice.exception.TicketServiceException;
import com.may.ticketservice.repository.TicketRepository;
import com.may.ticketservice.repository.entity.Ticket;
import com.may.ticketservice.repository.entity.elasticsearch.TicketElasticRepository;
import com.may.ticketservice.repository.entity.elasticsearch.TicketModel;
import com.may.ticketservice.service.TicketService;
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

    @Override
    @Transactional
    public TicketDto save(TicketDto ticketDto) {
        Ticket ticket = new Ticket();
        ResponseEntity<AccountEventDto> account = accountServiceClient.getUserByUserId(ticketDto.getAssignee());
        if (account.getBody() == null) {
            throw new TicketServiceException(ASSIGNEE_NOT_FOUND);
        }

        if (account.getStatusCode() != HttpStatus.OK) {
            throw new TicketServiceException(INTERNAL_ERROR);
        }

        if (ticketDto.getDescription() == null) {
            throw new TicketServiceException(DESCRIPTION_CAN_NOT_BE_EMPTY);
        }
        ticket.setDescription(ticketDto.getDescription());
        ticket.setNotes(ticketDto.getNotes());
        ticket.setTicketDate(ticketDto.getTicketDate());
        ticket.setTicketStatus(TicketStatus.valueOf(ticketDto.getTicketStatus()));
        ticket.setPriorityType(PriorityType.valueOf(ticketDto.getPriorityType()));
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

        ticketDto.setId(ticket.getId());
        return ticketDto;
    }

    @Override
    @Transactional
    public TicketDto update(String ticketId, TicketDto ticketDto) {
        Ticket ticket =
                ticketRepository.findById(ticketId).orElseThrow(() -> new TicketServiceException(TICKET_NOT_FOUND));

        ResponseEntity<AccountEventDto> account = accountServiceClient.getUserByUserId(ticketDto.getAssignee());
        if (account.getBody() == null) {
            throw new TicketServiceException(ASSIGNEE_NOT_FOUND);
        }

        if (account.getStatusCode() != HttpStatus.OK) {
            throw new TicketServiceException(INTERNAL_ERROR);
        }
        ticket.setTicketStatus(TicketStatus.valueOf(ticketDto.getTicketStatus()));
        ticket.setPriorityType(PriorityType.valueOf(ticketDto.getPriorityType()));
        ticket.setDescription(ticketDto.getDescription());
        ticket.setNotes(ticketDto.getNotes());
        ticket.setTicketDate(ticketDto.getTicketDate());
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
        return ticketDto;
    }

    @Override
    public TicketDto getById(String ticketId) {
        Ticket ticket =
                ticketRepository.findById(ticketId).orElseThrow(() -> new TicketServiceException(TICKET_NOT_FOUND));


        return modelMapper.map(ticket, TicketDto.class);
    }

    @Override
    public Page<TicketDto> getPagination(Pageable pageable) {

        return new PageImpl<>(ticketRepository.findAll(pageable).stream().map(ticket -> modelMapper.map(ticket,
                TicketDto.class)).collect(Collectors.toList()));
    }
}

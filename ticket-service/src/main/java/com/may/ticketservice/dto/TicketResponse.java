package com.may.ticketservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TicketResponse {
    private String id;

    private String description;

    private String notes;

    private String assignee;

    private Date ticketDate;

    private String priorityType;

    private String ticketStatus;
}

package com.may.ticketservice.repository.entity;

import com.may.ticketservice.enums.PriorityType;
import com.may.ticketservice.enums.TicketStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private String id;

    @Column(name = "description", length = 600)
    private String description;

    @Column(name = "notes", length = 4000)
    private String notes;

    @Column(name = "assignee", length = 50)
    private String assignee;

    private Date ticketDate;

    @Enumerated(EnumType.STRING)
    private PriorityType priorityType;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;


}

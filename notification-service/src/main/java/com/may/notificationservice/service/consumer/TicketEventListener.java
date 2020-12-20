package com.may.notificationservice.service.consumer;


import com.may.ticketservice.TicketEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TicketEventListener {

    @KafkaListener(topics = "${kafka.ticket-event-consumer.topic}",
            autoStartup = "${kafka.enabled}",
            containerFactory = "ticketEventConcurrentKafkaListenerContainerFactory")
    public void onMessage(@Payload final List<TicketEvent> ticketEvents) {

        try {
            log.info("Ticket event message received. message {}", ticketEvents);

        } catch (Exception e) {
            log.error("An error has occurred while processing message {}", ticketEvents);
            log.error("Exception received", e);
        }
    }
}

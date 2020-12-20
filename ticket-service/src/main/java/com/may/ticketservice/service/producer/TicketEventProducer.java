package com.may.ticketservice.service.producer;

import com.may.ticketservice.TicketEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class TicketEventProducer {

    private final KafkaTemplate<String, TicketEvent> kafkaTemplate;
    private final String topic;

    @Autowired
    public TicketEventProducer(KafkaTemplate<String, TicketEvent> kafkaTemplate,
                               @Value("${kafka.ticket-event-producer.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(TicketEvent ticketEvent) {
        try {
            log.info(
                    "Trying to publish new ticket event message, ticket id : {}", ticketEvent.getId()
            );
            kafkaTemplate.send(topic, ticketEvent).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("An Error has been occurred when sending ticket event message, ticket id : {}  ",
                    ticketEvent.getId());
        }
    }
}

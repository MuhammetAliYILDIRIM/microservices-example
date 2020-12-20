package com.may.notificationservice.config;


import com.may.ticketservice.TicketEvent;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

import static io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static io.confluent.kafka.serializers.KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;


@EnableKafka
@Configuration
public class TicketEventConsumerKafkaConfig {


    private final String schemaUrl;
    private final String clientId;
    private final String groupId;
    private final BaseKafkaConfig baseKafkaConfig;

    @Autowired
    public TicketEventConsumerKafkaConfig(@Value("${kafka.ticket-event-consumer.clientId}") final String clientId,
                                          @Value("${kafka.ticket-event-consumer.groupId}") final String groupId,
                                          @Value("${kafka.schema.registry.url}") String schemaUrl,
                                          final BaseKafkaConfig baseKafkaConfig) {
        this.schemaUrl = schemaUrl;
        this.clientId = clientId;
        this.groupId = groupId;
        this.baseKafkaConfig = baseKafkaConfig;
    }

    public ConsumerFactory<String, TicketEvent> ticketEventConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>(baseKafkaConfig.defaultConfig());
        configProps.put(CLIENT_ID_CONFIG, clientId);
        configProps.put(GROUP_ID_CONFIG, groupId);
        configProps.put(ENABLE_AUTO_COMMIT_CONFIG, false);
        configProps.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        configProps.put(SCHEMA_REGISTRY_URL_CONFIG, schemaUrl);
        configProps.put(SPECIFIC_AVRO_READER_CONFIG, true);

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, TicketEvent> ticketEventConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TicketEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(ticketEventConsumerFactory());
        return factory;
    }


}

package com.may.ticketservice.config;

import com.may.ticketservice.TicketEvent;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

import static io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static java.lang.Integer.MAX_VALUE;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

@EnableKafka
@Configuration
public class TicketEventProducerKafkaConfig {
    private final String schemaUrl;
    private final BaseKafkaConfig baseKafkaConfig;

    @Autowired
    public TicketEventProducerKafkaConfig(@Value("${kafka.schema.registry.url}") String schemaUrl,
                                          final BaseKafkaConfig baseKafkaConfig) {
        this.schemaUrl = schemaUrl;
        this.baseKafkaConfig = baseKafkaConfig;
    }

    @Bean
    public ProducerFactory<String, TicketEvent> ticketEventProducerFactory() {
        Map<String, Object> configProps = new HashMap<>(baseKafkaConfig.defaultConfig());
        configProps.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        configProps.put(SCHEMA_REGISTRY_URL_CONFIG, schemaUrl);
        configProps.put(RETRIES_CONFIG, MAX_VALUE);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, TicketEvent> ticketEventKafkaTemplate(@Autowired ProducerFactory<String,
            TicketEvent> factory) {

        return new KafkaTemplate<>(factory);
    }
}

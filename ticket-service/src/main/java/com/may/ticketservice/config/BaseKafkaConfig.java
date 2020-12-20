package com.may.ticketservice.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static org.apache.kafka.clients.CommonClientConfigs.REQUEST_TIMEOUT_MS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG;

@Configuration
public class BaseKafkaConfig {

    private static final String SASL_JAAS_CONFIG_TEMPLATE = "org.apache.kafka.common.security.plain.PlainLoginModule " +
            "required username=\"%s\" password=\"%s\";";
    private final String bootstrapServers;
    private final String requestTimeOut;
    private final String apiKey;
    private final String apiSecret;

    public BaseKafkaConfig(@Value("${kafka.bootstrap.servers}") String bootstrapServers,
                           @Value("${kafka.request.timeout.ms}") String requestTimeOut,
                           @Value("${kafka.api.key}") String apiKey,
                           @Value("${kafka.api.secret}") String apiSecret) {
        this.bootstrapServers = bootstrapServers;
        this.requestTimeOut = requestTimeOut;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public Map<String, Object> defaultConfig() {
        Map<String, Object> defaultConfigProps = new HashMap<>();
        final String jaasConfig = format(SASL_JAAS_CONFIG_TEMPLATE, apiKey, apiSecret);
        defaultConfigProps.put(SASL_JAAS_CONFIG_TEMPLATE, jaasConfig);
        defaultConfigProps.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        defaultConfigProps.put(REQUEST_TIMEOUT_MS_CONFIG, requestTimeOut);
        defaultConfigProps.put(RETRIES_CONFIG, 3);
        defaultConfigProps.put(RECONNECT_BACKOFF_MS_CONFIG, "5000");
        return defaultConfigProps;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>(defaultConfig());

        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}

package com.barco.common.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * @author Nabeel Ahmed
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    public Logger logger = LogManager.getLogger(WebSocketBrokerConfig.class);

    public String WS_ENDPOINT_PREFIX = "/app";
    public String WS_TOPIC_DESTINATION_PREFIX = "/topic";

    public String SAMPLE_ENDPOINT_MESSAGE_MAPPING = "/sampleEndpoint";
    public String SAMPLE_ENDPOINT_WITHOUT_RESPONSE_MESSAGE_MAPPING = "/sampleEndpointWithoutResponse";

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(WS_TOPIC_DESTINATION_PREFIX);
        config.setApplicationDestinationPrefixes(WS_ENDPOINT_PREFIX);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(SAMPLE_ENDPOINT_MESSAGE_MAPPING).withSockJS();
        registry.addEndpoint(SAMPLE_ENDPOINT_WITHOUT_RESPONSE_MESSAGE_MAPPING).withSockJS();
    }

}

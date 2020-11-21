package com.barco.common.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * @author Nabeel Ahmed
 */
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    public Logger logger = LogManager.getLogger(WebSocketSecurityConfig.class);

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.anyMessage().authenticated();
    }

    @Override
    protected boolean sameOriginDisabled() { return true; }

}
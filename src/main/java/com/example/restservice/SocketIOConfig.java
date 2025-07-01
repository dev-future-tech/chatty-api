package com.example.restservice;

import com.corundumstudio.socketio.AuthorizationResult;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component()
public class SocketIOConfig {

    private final Logger log = LoggerFactory.getLogger(SocketIOConfig.class);

    // I have set the configuration values in application.yaml file
    @Value("${socket.host}")
    private String socketHost;
    @Value("${socket.port}")
    private int socketPort;

    // SocketIOServer class is used to create a socket server
    private SocketIOServer server;

    @Bean
    public SocketIOServer socketIOServer() {
        // Configuration object holds the server settings
        Configuration config = new Configuration();

        config.setHostname(socketHost);
        config.setPort(socketPort);

        server = new SocketIOServer(config);
        server.start();

        server.addConnectListener(client -> log.info("Client connected: {}", client.getSessionId()));
        server.addDisconnectListener(client -> log.info("Client disconnected: {}", client.getSessionId()));


        return server;
    }

    @PreDestroy
    public void stopSocketServer() {
        log.info("Shutting down SocketIO Server...");
        this.server.stop();
        log.info("SocketIO Server shut down...");
    }
}
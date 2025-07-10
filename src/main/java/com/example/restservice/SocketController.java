package com.example.restservice;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SocketController {
    private SocketIOServer socketIOServer;

    private Logger log = LoggerFactory.getLogger(SocketController.class);
    protected final SocketIOServer socketServer;


    public SocketController(SocketIOServer socketServer) {
        this.socketServer = socketServer;
        this.socketServer.addEventListener("demoEvent", SocketDetail.class, demoEvent);
        this.socketServer.addEventListener("chatEvent", ChatDetail.class, chatEvent);
    }

    public DataListener<SocketDetail> demoEvent = (client, socketDetail, ackRequest) -> {
        log.info("Demo event received: {}", socketDetail.getName());

        log.info("Prompting chat completions...");

        log.info("Chat completions complete!");

        SocketResponse response = new SocketResponse();

        response.setPrevMessage(socketDetail.getName());
        ackRequest.sendAckData(response);
    };

    public DataListener<ChatDetail> chatEvent = (client, chatDetail, ackRequest) -> {
        log.info("Chat event received: {}, {}", chatDetail.getName(), chatDetail.getMessage());
        SocketResponse response = new SocketResponse();
        response.setPrevMessage(chatDetail.getName());
        ackRequest.sendAckData(response);
    };
}

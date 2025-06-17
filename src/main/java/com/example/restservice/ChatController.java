package com.example.restservice;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ChatController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    protected final SocketIOServer socketServer;

    public ChatController(SocketIOServer socketServer) {
        this.socketServer = socketServer;
        this.socketServer.addEventListener("demoEvent", SocketDetail.class, demoEvent);
    }

    public DataListener<SocketDetail> demoEvent = new DataListener<>() {
        @Override
        public void onData(SocketIOClient client, SocketDetail socketDetail, AckRequest ackRequest) {
            log.info("Demo event received: {}", socketDetail.getName());
            SocketResponse response = new SocketResponse();
            response.setMessage("Hello from Java!");
            response.setPrevMessage(socketDetail.getName());
            ackRequest.sendAckData(response);
        }
    };
}

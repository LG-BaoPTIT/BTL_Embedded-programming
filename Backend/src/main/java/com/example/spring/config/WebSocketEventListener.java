package com.example.spring.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashSet;
import java.util.Set;

public class WebSocketEventListener {

    private boolean isConnected = false;

//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        // Xử lý khi có kết nối WebSocket
//        isConnected = true;
//    }

//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        // Xử lý khi kết nối WebSocket bị đóng
//        isConnected = false;
//    }

    public boolean isConnected() {
        return isConnected;
    }

    private final Set<String> connectedSessions = new HashSet<>();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        connectedSessions.add(sessionId);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        connectedSessions.remove(sessionId);
    }

    public boolean hasSubscribers(String topic) {
        return connectedSessions.stream()
                .anyMatch(sessionId -> topic.equals("/topic/" + sessionId));
    }
}

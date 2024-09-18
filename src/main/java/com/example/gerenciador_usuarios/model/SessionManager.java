package com.example.gerenciador_usuarios.model;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SessionManager {
    private Map<String, Usuarios> sessions = new HashMap<>();

    public String createSession(Usuarios user) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, user);
        return token;
    }

    public Usuarios getUser(String token) {
        return sessions.get(token);
    }

    public void removeSession(String token) {
        sessions.remove(token);
    }
}

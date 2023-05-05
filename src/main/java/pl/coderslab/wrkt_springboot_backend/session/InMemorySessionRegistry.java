package pl.coderslab.wrkt_springboot_backend.session;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

@Component
public class InMemorySessionRegistry {

    private static final HashMap<String,String> SESSIONS = new HashMap<>();

    public String registerSession(final String username){
        if(username == null){
            throw new RuntimeException("Username needs to be provided");
        }

        final String sessionId = generateSessionId();
        SESSIONS.put(sessionId,username);
        return sessionId;
    }

    public String getUserNameForSession(final String sessionId){
        return SESSIONS.get(sessionId);
    }

    public void deleteSessionForUser(String sessionId, String username){
        SESSIONS.remove(sessionId,username);
    }
    private String generateSessionId(){
        return new String(
                Base64.getEncoder().encode(
                        UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8))
        );
    }
}

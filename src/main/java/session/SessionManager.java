package session;

import java.util.UUID;

public class SessionManager {

    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}

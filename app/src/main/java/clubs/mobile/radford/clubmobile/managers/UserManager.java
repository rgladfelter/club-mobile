package clubs.mobile.radford.clubmobile.managers;

public class UserManager {
    private static String sessionId;

    public static String getSessionId() {
        return sessionId;
    }

    public static void setSessionId(String sessionId) {
        UserManager.sessionId = sessionId;
    }
}

package clubs.mobile.radford.clubmobile.managers;

public class UserManager {
    private static String sessionId;
    private static String userName;

    public static String getSessionId() {
        return sessionId;
    }

    public static void setSessionId(String sessionId) {
        UserManager.sessionId = sessionId;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserManager.userName = userName;
    }
}

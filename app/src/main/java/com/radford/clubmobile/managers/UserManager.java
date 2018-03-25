package com.radford.clubmobile.managers;

import com.radford.clubmobile.models.User;

public class UserManager {
    private static String sessionId;
    private static User user;

    public static String getSessionId() {
        return sessionId;
    }

    public static void setSessionId(String sessionId) {
        UserManager.sessionId = sessionId;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserManager.user = user;
    }
}

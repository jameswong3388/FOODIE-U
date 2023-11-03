package com.oodwj_assignment.states;

import java.util.UUID;

public final class AppState {
    private static UUID sessionToken;

    public AppState() {
    }

    public static UUID getSessionToken() {
        return sessionToken;
    }

    public static void setSessionToken(UUID sessionToken) {
        AppState.sessionToken = sessionToken;
    }
}

package com.yatty.sevenatenine.client.auth;

import com.yatty.sevenatenine.api.commands_with_data.PrivateLobbyInfo;
import com.yatty.sevenatenine.api.commands_with_data.PublicLobbyInfo;

public class SessionInfo {
    private static final char DIVIDER = '|';

    private static String userId;
    private static int userRating;
    private static String authToken;
    private static String gameId;
    private static PublicLobbyInfo sPublicLobbyInfo;
    private static PrivateLobbyInfo sPrivateLobbyInfo;

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        SessionInfo.userId = userId;
    }

    public static String getUserName() {
        return userId.substring(0, userId.indexOf(DIVIDER));
    }

    public static int getUserRating() {
        return userRating;
    }

    public static void setUserRating(int userRating) {
        SessionInfo.userRating = userRating;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        SessionInfo.authToken = authToken;
    }

    public static PublicLobbyInfo getPublicLobbyInfo() {
        return sPublicLobbyInfo;
    }

    public static void setPublicLobbyInfo(PublicLobbyInfo publicLobbyInfo) {
        sPublicLobbyInfo = publicLobbyInfo;
    }

    public static String getGameId() {
        return getPublicLobbyInfo().getLobbyId();
    }

    public static PublicLobbyInfo getsPublicLobbyInfo() {
        return sPublicLobbyInfo;
    }

    public static void setsPublicLobbyInfo(PublicLobbyInfo sPublicLobbyInfo) {
        SessionInfo.sPublicLobbyInfo = sPublicLobbyInfo;
    }

    public static PrivateLobbyInfo getPrivateLobbyInfo() {
        return sPrivateLobbyInfo;
    }

    public static void setPrivateLobbyInfo(PrivateLobbyInfo privateLobbyInfo) {
        sPrivateLobbyInfo = privateLobbyInfo;
    }
}

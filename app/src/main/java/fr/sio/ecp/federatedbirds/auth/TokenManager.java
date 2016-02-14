package fr.sio.ecp.federatedbirds.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by Michaël on 25/11/2015.
 */
public class TokenManager {
    private static final String AUTH_PREFERENCES = "auth";
    private static final String TOKEN_KEY = "token";
    private static final String LOGGED_IN_USER_LOGIN_KEY = "loggedInUserLogin";
    private static User mLoggedInUser;

    public static String getUserToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(TOKEN_KEY, null);
    }

    public static String getLoggedInUserLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(LOGGED_IN_USER_LOGIN_KEY, null);
    }

    public static void setUserToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        sp.edit().putString(TOKEN_KEY, token).apply();
        Log.i(TokenManager.class.getSimpleName(), "User token saved: " + token);
    }

    public static void setLoggedInUserLogin(Context context, String login) {
        SharedPreferences sp = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        sp.edit().putString(LOGGED_IN_USER_LOGIN_KEY, login).apply();
        Log.i(TokenManager.class.getSimpleName(), "Logged in user login saved: " + login);
    }

    public static void setLoggedInUser(User user) {
        mLoggedInUser = user;
    }

    public static User getLoggedInUser() {
        return mLoggedInUser;
    }

    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
        Log.i(TokenManager.class.getSimpleName(), "Auth preferences cleared");
    }

}

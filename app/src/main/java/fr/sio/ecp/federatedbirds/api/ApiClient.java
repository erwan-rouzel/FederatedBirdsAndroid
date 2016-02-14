package fr.sio.ecp.federatedbirds.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import fr.sio.ecp.federatedbirds.auth.TokenManager;
import fr.sio.ecp.federatedbirds.model.Avatar;
import fr.sio.ecp.federatedbirds.model.Message;
import fr.sio.ecp.federatedbirds.model.User;
import fr.sio.ecp.federatedbirds.model.Error;
import fr.sio.ecp.federatedbirds.utils.ImageUtils;

/**
 * Created by Michaël on 24/11/2015.
 */
public class ApiClient {

    private static final String API_BASE = "https://federatedbirds.appspot.com/";
    //private static final String API_BASE = "http://10.0.2.2:8080/";

    private static ApiClient mInstance;

    public static synchronized ApiClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiClient(context);
        }
        return mInstance;
    }

    private Context mContext;

    private ApiClient(Context context) {
        mContext = context.getApplicationContext();
    }

    private <T> T get(String path, Type type) throws IOException, ApiException {
        return method("GET", path, null, type, "application/json");
    }

    private <T> T post(String path, Object body, Type type) throws IOException, ApiException {
        return method("POST", path, body, type, "application/json");
    }

    private <T> T put(String path, Object body, Type type, String mimeType) throws IOException, ApiException {
        return method("PUT", path, body, type, mimeType);
    }

    private <T> T method(String method, String path, Object body, Type type, String mimeType) throws IOException, ApiException {
        String url = API_BASE + path;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        String token = TokenManager.getUserToken(mContext);
        if (token != null) {
            connection.addRequestProperty("Authorization", "Bearer " + token);
        }

        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Content-Type", mimeType);

        if (body != null) {
            if(type == Avatar.class) {
                byte[] fileContent = (byte[])body;
                DataOutputStream request = new DataOutputStream(
                        connection.getOutputStream());

                request.write(fileContent, 0, fileContent.length);

                request.flush();
                request.close();
            } else {
                Writer writer = new OutputStreamWriter(connection.getOutputStream());

                try {
                    new Gson().toJson(body, writer);
                } finally {
                    writer.close();
                }
            }
        }

        int responseCode = connection.getResponseCode();

        if(responseCode == 200) {
            Reader reader = new InputStreamReader(connection.getInputStream());
            try {
                return new Gson().fromJson(reader, type);
            } finally {
                reader.close();
            }
        } else {
            Reader reader = new InputStreamReader(connection.getErrorStream());
            try {
                Error error = new Gson().fromJson(reader, Error.class);
                throw new ApiException(error.status, error.code, error.message);
            } finally {
                reader.close();
            }
        }
    }

    public List<Message> getMessages(Long userId) throws IOException, ApiException {
        TypeToken<List<Message>> type = new TypeToken<List<Message>>() {};
        String path = userId == null ? "messages" : "messages/" + userId;
        return get(path, type.getType());
    }

    public User getUser(Long userId) throws IOException, ApiException {
        String id = userId != null ? Long.toString(userId) : "me";
        return get("user/" + id, User.class);
    }

    public List<User> getUserFollowed(Long userId) throws IOException, ApiException {
        String id = userId != null ? Long.toString(userId) : "me";
        TypeToken<List<User>> type = new TypeToken<List<User>>() {};
        return get("users/" + id + "/followed", type.getType());
    }

    public String login(String login, String password) throws IOException, ApiException {
        JsonObject body = new JsonObject();
        body.addProperty("login", login);
        body.addProperty("password", password);
        return post("auth/token", body, String.class);
    }

    public Message postMessage(String text) throws IOException, ApiException {
        Message message = new Message();
        message.text = text;
        return post("messages", message, Message.class);
    }

    public String createUser(String login, String password, String email) throws IOException, ApiException {
        User user = new User();
        user.login = login;
        user.password = password;
        user.email = email;
        user.avatar = "";
        user.coverPicture = "";

        return post("users", user, String.class);
    }

    public List<User> getUserFollower(Long userId) throws IOException, ApiException {
        String id = userId != null ? Long.toString(userId) : "me";
        TypeToken<List<User>> type = new TypeToken<List<User>>() {};
        return get("users/" + id + "/followers", type.getType());
    }

    public List<User> getUserAll() throws IOException, ApiException {
        TypeToken<List<User>> type = new TypeToken<List<User>>() {};
        return get("users", type.getType());
    }

    public User setUserFollow(Long userId, boolean follow) throws IOException, ApiException {
        return post("user/" + userId + "?followed=" + follow, null, User.class);
    }

    public Avatar postAvatar(Bitmap bitmap, String mimeType) throws IOException, ApiException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // We resize the bitmap to 512x512 (keeping aspect ratio) to avoid sending big 4MB files from phones
        // Then we compress in JPEG so assure uniform compression (although PNG files are also supported by API)
        Bitmap resizedBitmap = ImageUtils.resizeBitmap(bitmap, 512, 512);
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] byteArray = stream.toByteArray();

        return put("user/avatar", byteArray, Avatar.class, mimeType);
    }
}

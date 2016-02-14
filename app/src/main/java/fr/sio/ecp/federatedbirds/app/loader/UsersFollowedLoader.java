package fr.sio.ecp.federatedbirds.app.loader;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import fr.sio.ecp.federatedbirds.api.ApiClient;
import fr.sio.ecp.federatedbirds.api.ApiException;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by Michaël on 30/11/2015.
 */
public class UsersFollowedLoader extends UsersLoader {

    public UsersFollowedLoader(Context context, Long userId) {
        super(context, userId);
    }

    @Override
    protected List<User> getUsers(Long userId) throws IOException, ApiException {
        return ApiClient.getInstance(getContext()).getUserFollowed(userId);
    }

}
package fr.sio.ecp.federatedbirds.app.task;

import android.content.Context;

import java.io.IOException;

import fr.sio.ecp.federatedbirds.api.ApiAsyncTask;
import fr.sio.ecp.federatedbirds.api.ApiClient;
import fr.sio.ecp.federatedbirds.api.ApiException;
import fr.sio.ecp.federatedbirds.auth.TokenManager;

/**
 * Created by erwanrouzel on 14/02/16.
 */
public class SetLoggedInUserTask extends ApiAsyncTask<Void, Void, Void> {
    public SetLoggedInUserTask(Context context, Class callerClass) {
        super(context, callerClass);
    }

    @Override
    protected Void apiCall(Void... params) throws IOException, ApiException {
        TokenManager.setLoggedInUser(ApiClient.getInstance(mContext).getUser(null));
        return null;
    }

    @Override
    protected void actionOnSuccess(Void nothing) {
        // Do nothing
    }

    @Override
    protected void cleanPostExectute() {
        // Do nothing
    }
}

package fr.sio.ecp.federatedbirds.app.task;

import android.content.Context;

import java.io.IOException;

import fr.sio.ecp.federatedbirds.api.ApiAsyncTask;
import fr.sio.ecp.federatedbirds.api.ApiException;
import fr.sio.ecp.federatedbirds.app.activity.MainActivity;
import fr.sio.ecp.federatedbirds.app.fragment.LoginTaskFragment;
import fr.sio.ecp.federatedbirds.auth.TokenManager;

/**
 * Created by erwanrouzel on 14/02/16.
 */
public class LoginTask extends ApiAsyncTask<Void, Void, String> {
    private LoginTaskFragment loginTaskFragment;
    private String login;
    private String password;

    public LoginTask(LoginTaskFragment loginTaskFragment, Context context, Class callerClass) {
        super(context, callerClass);
        this.loginTaskFragment = loginTaskFragment;
    }

    @Override
    protected String apiCall(Void... params) throws IOException, ApiException {
        login = loginTaskFragment.getArguments().getString("login");
        password = loginTaskFragment.getArguments().getString("password");
        String token = mApiClient.login(login, password);
        return token;
    }

    @Override
    protected void actionOnSuccess(String token) {
        TokenManager.setUserToken(mContext, token);
        TokenManager.setLoggedInUserLogin(mContext, login);
        loginTaskFragment.startActivity(MainActivity.newIntent(mContext));
    }

    @Override
    protected void cleanPostExectute() {
        loginTaskFragment.dismiss();
    }
}

package fr.sio.ecp.federatedbirds.app.task;

import android.content.Context;

import java.io.IOException;

import fr.sio.ecp.federatedbirds.api.ApiAsyncTask;
import fr.sio.ecp.federatedbirds.api.ApiException;
import fr.sio.ecp.federatedbirds.app.activity.MainActivity;
import fr.sio.ecp.federatedbirds.app.fragment.CreateAccountTaskFragment;
import fr.sio.ecp.federatedbirds.auth.TokenManager;

/**
 * Created by erwanrouzel on 14/02/16.
 */
public class CreateAccountTask extends ApiAsyncTask<Void, Void, String> {
    private CreateAccountTaskFragment createAccountTaskFragment;
    private String login;
    private String password;
    private String email;

    public CreateAccountTask(CreateAccountTaskFragment createAccountTaskFragment, Context context, Class callerClass) {
        super(context, callerClass);
        this.createAccountTaskFragment = createAccountTaskFragment;
    }

    @Override
    protected String apiCall(Void... params) throws IOException, ApiException {
        login = createAccountTaskFragment.getArguments().getString("login");
        password = createAccountTaskFragment.getArguments().getString("password");
        email = createAccountTaskFragment.getArguments().getString("email");

        mApiClient.createUser(login, password, email);

        return mApiClient.login(login, password);
    }

    @Override
    protected void actionOnSuccess(String token) {
        TokenManager.setUserToken(mContext, token);
        TokenManager.setLoggedInUserLogin(mContext, login);
        createAccountTaskFragment.getActivity().finish();
        createAccountTaskFragment.startActivity(MainActivity.newIntent(mContext));
    }

    @Override
    protected void cleanPostExectute() {
        createAccountTaskFragment.dismiss();
    }
}

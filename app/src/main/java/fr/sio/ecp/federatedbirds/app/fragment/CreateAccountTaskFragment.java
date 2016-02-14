package fr.sio.ecp.federatedbirds.app.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.os.AsyncTaskCompat;


import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.app.activity.LoginActivity;
import fr.sio.ecp.federatedbirds.app.task.CreateAccountTask;

/**
 * Created by Michaël on 30/11/2015.
 */
public class CreateAccountTaskFragment extends DialogFragment {

    private static final String ARG_LOGIN = "login";
    private static final String ARG_PASSWORD = "password";
    private static final String ARG_EMAIL = "email";

    public void setArguments(String login, String password, String email) {
        Bundle args = new Bundle();
        args.putString(CreateAccountTaskFragment.ARG_LOGIN, login);
        args.putString(CreateAccountTaskFragment.ARG_PASSWORD, password);
        args.putString(CreateAccountTaskFragment.ARG_EMAIL, email);
        setArguments(args);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        AsyncTaskCompat.executeParallel(
                new CreateAccountTask(this,
                        getContext(),
                        LoginActivity.class
                )
        );
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setIndeterminate(true);
        dialog.setMessage(getString(R.string.create_account_progress));
        return dialog;
    }
}

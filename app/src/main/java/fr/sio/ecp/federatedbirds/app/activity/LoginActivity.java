package fr.sio.ecp.federatedbirds.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.app.fragment.CreateAccountFragment;
import fr.sio.ecp.federatedbirds.app.fragment.CreateAccountTaskFragment;
import fr.sio.ecp.federatedbirds.app.fragment.LoginFragment;
import fr.sio.ecp.federatedbirds.app.fragment.LoginTaskFragment;
import fr.sio.ecp.federatedbirds.utils.ValidationUtils;

public class LoginActivity extends AppCompatActivity
implements CreateAccountFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        showLogin();
    }

    private void showLogin() {
        LoginFragment loginFragment = new LoginFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_container, loginFragment)
                .commit();
    }

    @Override
    public void onDoCreateAccountClicked() {
        EditText loginText = (EditText) findViewById(R.id.login);
        EditText passwordText = (EditText) findViewById(R.id.password);
        EditText emailText = (EditText) findViewById(R.id.email);

        String login = loginText.getText().toString();
        String password = passwordText.getText().toString();
        String email = emailText.getText().toString();

        if (!ValidationUtils.validateLogin(login)) {
            loginText.setError(getString(R.string.invalid_format));
            loginText.requestFocus();
            return;
        }

        if (!ValidationUtils.validatePassword(password)) {
            passwordText.setError(getString(R.string.invalid_format));
            passwordText.requestFocus();
            return;
        }

        if (!ValidationUtils.validateEmail(email)) {
            emailText.setError(getString(R.string.invalid_format));
            emailText.requestFocus();
            return;
        }

        // Some trick to hide the keyboard
        InputMethodManager inputManager =
                (InputMethodManager) getApplicationContext().
                        getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        CreateAccountTaskFragment taskFragment = new CreateAccountTaskFragment();
        taskFragment.setArguments(login, password, email);

        taskFragment.show(getSupportFragmentManager(), "create_account_task");
    }

    @Override
    public void onCreateAccountClicked() {
        CreateAccountFragment createAccountFragment = new CreateAccountFragment();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(R.id.login_container, createAccountFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLoginClicked() {
        // Get form views
        EditText usernameText = (EditText) findViewById(R.id.username);
        EditText passwordText = (EditText) findViewById(R.id.password);

        String login = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if (!ValidationUtils.validateLogin(login)) {
            usernameText.setError(getString(R.string.invalid_format));
            usernameText.requestFocus();
            return;
        }

        if (!ValidationUtils.validatePassword(password)) {
            passwordText.setError(getString(R.string.invalid_format));
            passwordText.requestFocus();
            return;
        }

        LoginTaskFragment taskFragment = new LoginTaskFragment();
        taskFragment.setArguments(login, password);

        taskFragment.show(getSupportFragmentManager(), "login_task");
    }
}

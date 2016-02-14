package fr.sio.ecp.federatedbirds.app.task;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.api.ApiAsyncTask;
import fr.sio.ecp.federatedbirds.api.ApiClient;
import fr.sio.ecp.federatedbirds.api.ApiException;
import fr.sio.ecp.federatedbirds.app.fragment.UserFragment;
import fr.sio.ecp.federatedbirds.auth.TokenManager;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by erwanrouzel on 14/02/16.
 */
public class LoadUserTask extends ApiAsyncTask<Long, Void, User> {
    private UserFragment fragment;
    private User mUser;

    public LoadUserTask(UserFragment fragment, Context context, Class callerClass) {
        super(context, callerClass);
        this.fragment = fragment;
    }

    @Override
    protected User apiCall(Long... params) throws IOException, ApiException {
        mUser = ApiClient.getInstance(mContext).getUser(params[0]);
        return mUser;
    }

    @Override
    protected void actionOnSuccess(User user) {
        View view = ((AppCompatActivity) fragment.getContext()).getWindow().getDecorView();
        FloatingActionButton editButton = (FloatingActionButton) view.findViewById(R.id.edit_button);

        String loggedInUser = TokenManager.getLoggedInUserLogin(fragment.getContext());

        if (user.login.compareTo(loggedInUser) == 0) {
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.openGallery(1);
                }
            });
        } else {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) editButton.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            editButton.setLayoutParams(p);
            editButton.setVisibility(View.GONE);
        }

        fragment.updateAvatar(user.avatar);
    }

    @Override
    protected void cleanPostExectute() {
        // nothing to do here
    }
}

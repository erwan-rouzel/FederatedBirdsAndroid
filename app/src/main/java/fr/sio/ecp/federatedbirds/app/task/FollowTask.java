package fr.sio.ecp.federatedbirds.app.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.api.ApiAsyncTask;
import fr.sio.ecp.federatedbirds.api.ApiClient;
import fr.sio.ecp.federatedbirds.api.ApiException;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by erwanrouzel on 14/02/16.
 */
public class FollowTask extends ApiAsyncTask<Boolean, Void, User> {
    private Context mContext;
    private User mUser;
    private Button mFollowButton;
    private boolean mFollowFlag;
    private ProgressDialog mProgressDialog;

    public FollowTask(Context context, Class callerClass, User user, ProgressDialog progressDialog, Button followButton) throws IOException {
        mContext = context;
        mCallerClass = callerClass;
        mUser = user;
        mFollowButton = followButton;
        mProgressDialog = progressDialog;
    }

    @Override
    protected User apiCall(Boolean... params) throws IOException, ApiException {
        mFollowFlag = (boolean) params[0];
        User refreshedUser = ApiClient.getInstance(mContext)
                .setUserFollow(
                        mUser.id,
                        mFollowFlag);

        return refreshedUser;
    }

    @Override
    protected void actionOnSuccess(User user) {
        mProgressDialog.cancel();
        if (user != null) {
            // We refresh the button text
            if (mFollowFlag) {
                mFollowButton.setText(
                        mContext.getString(R.string.unfollow)
                );
            } else {
                mFollowButton.setText(
                        mContext.getString(R.string.follow)
                );
            }
        } else {
            Toast.makeText(mContext, R.string.follow_failed, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void cleanPostExectute() {
        // Do nothing
    }
}

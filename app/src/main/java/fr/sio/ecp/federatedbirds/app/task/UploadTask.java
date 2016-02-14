package fr.sio.ecp.federatedbirds.app.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

import fr.sio.ecp.federatedbirds.api.ApiAsyncTask;
import fr.sio.ecp.federatedbirds.api.ApiException;
import fr.sio.ecp.federatedbirds.app.activity.UserActivity;
import fr.sio.ecp.federatedbirds.app.fragment.UserFragment;
import fr.sio.ecp.federatedbirds.model.Avatar;

/**
 * Created by erwanrouzel on 14/02/16.
 */
public class UploadTask extends ApiAsyncTask<Void, Void, Avatar> {
    private UserActivity userActivity;
    private Bitmap bitmap;
    private String mimeType;
    private UserFragment mUserFragment;

    public UploadTask(UserActivity userActivity, Context context, Class callerClass, Uri image, UserFragment userFragment) throws IOException {
        super(context, callerClass);
        this.userActivity = userActivity;
        bitmap = MediaStore.Images.Media.getBitmap(userActivity.getContentResolver(), image);
        mimeType = userActivity.getContentResolver().getType(image);
        mUserFragment = userFragment;
    }

    @Override
    protected Avatar apiCall(Void... params) throws IOException, ApiException {
        return mApiClient.postAvatar(bitmap, mimeType);
    }

    @Override
    protected void actionOnSuccess(Avatar avatar) {
        mUserFragment.updateAvatar(avatar.servingUrl);
    }

    @Override
    protected void cleanPostExectute() {
        // do nothing here
    }
}

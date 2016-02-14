package fr.sio.ecp.federatedbirds.app.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.api.ApiClient;
import fr.sio.ecp.federatedbirds.api.ApiException;
import fr.sio.ecp.federatedbirds.model.Message;

/**
 * Created by Michaël on 24/11/2015.
 */
public class MessagesLoader extends AsyncTaskLoader<List<Message>> {

    private Long mUserId;
    private List<Message> mResult;

    public MessagesLoader(Context context, Long userId) {
        super(context);
        mUserId = userId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mResult != null) {
            deliverResult(mResult);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<Message> loadInBackground() {
        try {
            return ApiClient.getInstance(getContext()).getMessages(mUserId);
        } catch (IOException|ApiException e) {
            // Most likely, if we are here it means we are not authorized to see the messages
            // (we can see only our messages or those of followed users)
            Log.i(MessagesLoader.class.getSimpleName(), e.getMessage());
            return null;
        }
    }

    @Override
    public void deliverResult(List<Message> data) {
        mResult = data;
        super.deliverResult(data);
    }
}
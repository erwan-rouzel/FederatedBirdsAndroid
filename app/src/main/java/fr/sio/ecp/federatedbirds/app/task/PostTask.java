package fr.sio.ecp.federatedbirds.app.task;

import android.app.Activity;
import android.content.Context;

import java.io.IOException;

import fr.sio.ecp.federatedbirds.api.ApiAsyncTask;
import fr.sio.ecp.federatedbirds.api.ApiClient;
import fr.sio.ecp.federatedbirds.api.ApiException;
import fr.sio.ecp.federatedbirds.app.fragment.PostMessageFragment;
import fr.sio.ecp.federatedbirds.model.Message;

/**
 * Created by erwanrouzel on 14/02/16.
 */
public class PostTask extends ApiAsyncTask<Void, Void, Message> {

    private PostMessageFragment.PostTaskFragment postTaskFragment;

    public PostTask(PostMessageFragment.PostTaskFragment postTaskFragment, Context context, Class callerClass) {
        super(context, callerClass);
        this.postTaskFragment = postTaskFragment;
    }

    @Override
    protected Message apiCall(Void... params) throws IOException, ApiException {
        String message = postTaskFragment.getArguments().getString("message");
        return ApiClient.getInstance(postTaskFragment.getContext()).postMessage(message);
    }

    @Override
    protected void actionOnSuccess(Message message) {
        postTaskFragment.getTargetFragment().onActivityResult(
                postTaskFragment.getTargetRequestCode(),
                Activity.RESULT_OK,
                null
        );
    }

    @Override
    protected void cleanPostExectute() {
        postTaskFragment.dismiss();
    }
}

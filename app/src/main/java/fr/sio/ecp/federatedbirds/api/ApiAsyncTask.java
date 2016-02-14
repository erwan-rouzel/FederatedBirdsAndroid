package fr.sio.ecp.federatedbirds.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.api.ApiClient;
import fr.sio.ecp.federatedbirds.api.ApiException;
import fr.sio.ecp.federatedbirds.auth.TokenManager;

/**
 * Created by erwanrouzel on 23/01/16.
 *
 * This class is the root for all AsyncTask making calls to API
 * This prevent from duplicating code and keep uniform handling of errors
 */
public abstract class ApiAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    protected Context mContext;
    protected ApiClient mApiClient;
    protected Class mCallerClass;
    protected String mErrorMessage;

    protected abstract Result apiCall(Params... params) throws IOException, ApiException;
    protected abstract void actionOnSuccess(Result result);
    protected abstract void cleanPostExectute();

    public ApiAsyncTask() {
        mContext = null;
        mCallerClass = null;
        mApiClient = null;
    }

    public ApiAsyncTask(Context context, Class callerClass) {
        mContext = context;
        mCallerClass = callerClass;
        mApiClient = ApiClient.getInstance(context);
    }

    @Override
    protected Result doInBackground(Params... params) {
        try {
            return apiCall(params);
        } catch (IOException e) {
            mErrorMessage = mContext.getResources().getString(R.string.asynctask_io_error) + " - " + e.getMessage();
            Log.e(mCallerClass.getSimpleName(), mErrorMessage, e);
            return null;
        } catch (ApiException e) {
            mErrorMessage = mContext.getResources().getString(R.string.asynctask_api_error) + " - " + e.toString();
            Log.e(mCallerClass.getSimpleName(), mErrorMessage, e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        if (result != null) {
            actionOnSuccess(result);
        } else if(mErrorMessage != null ){
            Toast.makeText(mContext, mErrorMessage, Toast.LENGTH_SHORT).show();
        }
        cleanPostExectute();
    }
}

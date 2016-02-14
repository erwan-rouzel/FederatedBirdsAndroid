package fr.sio.ecp.federatedbirds.app.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.app.activity.UserActivity;
import fr.sio.ecp.federatedbirds.app.task.PostTask;

/**
 * Created by admin on 02/12/15.
 */
public class PostMessageFragment extends DialogFragment {

    private EditText mMessageText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getContext()).inflate(R.layout.post_message, null);
        mMessageText = (EditText) v.findViewById(R.id.message);

        Dialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.new_message)
                .setView(v)
                .setPositiveButton(R.string.post, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String message = mMessageText.getText().toString();
                        if (TextUtils.isEmpty(message)) {
                            Toast.makeText(getContext(), R.string.empty_message_error, Toast.LENGTH_LONG).show();
                            return;
                        }

                        PostTaskFragment taskFragment = new PostTaskFragment();
                        taskFragment.setArguments(message);
                        taskFragment.setTargetFragment(
                                getTargetFragment(),
                                getTargetRequestCode()
                        );
                        taskFragment.show(getFragmentManager(), "post_progress");

                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        return dialog;
    }

    /**
     * Created by Michaël on 30/11/2015.
     */
    public static class PostTaskFragment extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public void setArguments(String message) {
            Bundle args = new Bundle();
            args.putString(PostTaskFragment.ARG_MESSAGE, message);
            setArguments(args);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            AsyncTaskCompat.executeParallel(
                    new PostTask(this, getContext(), UserActivity.class)
            );
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setIndeterminate(true);
            dialog.setMessage(getString(R.string.progress));
            return dialog;
        }
    }
}

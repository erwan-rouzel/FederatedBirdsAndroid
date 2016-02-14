package fr.sio.ecp.federatedbirds.app.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.app.adapter.MessagesAdapter;
import fr.sio.ecp.federatedbirds.app.loader.MessagesLoader;
import fr.sio.ecp.federatedbirds.model.Message;

/**
 * Created by Michaël on 26/11/2015.
 */
public class MessageListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Message>> {

    private static final int LOADER_MESSAGES = 0;
    private static final int REQUEST_POST_MESSAGE = 0;

    private MessagesAdapter mMessagesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.messages_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView listView = (RecyclerView) view.findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMessagesAdapter = new MessagesAdapter();
        listView.setAdapter(mMessagesAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_POST_MESSAGE:
                if (resultCode == Activity.RESULT_OK) {
                    getLoaderManager().restartLoader(LOADER_MESSAGES, null, this);
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(
                LOADER_MESSAGES,
                null,
                this
        );
    }

    @Override
    public Loader<List<Message>> onCreateLoader(int id, Bundle args) {
        Long userId = null;
        if(getArguments() != null && getArguments().get("userId") != null) {
            userId = getArguments().getLong("userId");
        }
        return new MessagesLoader(getContext(), userId);
    }

    @Override
    public void onLoadFinished(Loader<List<Message>> loader, List<Message> messages) {
        mMessagesAdapter.setMessages(messages);
    }

    @Override
    public void onLoaderReset(Loader<List<Message>> loader) { }

}

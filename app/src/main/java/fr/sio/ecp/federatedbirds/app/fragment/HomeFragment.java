package fr.sio.ecp.federatedbirds.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.app.adapter.MessagesAdapter;

/**
 * Created by Michaël on 26/11/2015.
 */
public class HomeFragment extends Fragment {
    private static final int REQUEST_POST_MESSAGE = 0;
    private MessagesAdapter mMessagesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setHomeButtonEnabled(false);

        Fragment messagesFragment = new MessageListFragment();
        ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction()
                .add(R.id.list_messages, messagesFragment)
                .commit();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PostMessageFragment postFragment = new PostMessageFragment();
                postFragment.setTargetFragment(HomeFragment.this, REQUEST_POST_MESSAGE);
                postFragment.show(getFragmentManager(), "post_dialog");
            }

        });
    }
}

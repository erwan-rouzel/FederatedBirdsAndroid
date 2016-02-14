package fr.sio.ecp.federatedbirds.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.app.activity.UserActivity;
import fr.sio.ecp.federatedbirds.app.task.LoadUserTask;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER_ID = "userId";

    // TODO: Rename and change types of parameters
    private Long mUserId;

    private OnFragmentInteractionListener mListener;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId User ID.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(Long userId) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getLong(ARG_USER_ID);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = new User();
        LoadUserTask userTask = new LoadUserTask(this, getContext(), UserActivity.class);
        try {
            user = userTask.execute(mUserId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();

        Toolbar toolbar = (Toolbar) parentActivity.findViewById(R.id.toolbar);
        parentActivity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        ActionBar actionBar = parentActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) parentActivity.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(user.login);
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorAccent));

        TextView emailTextView = (TextView) parentActivity.findViewById(R.id.email);
        emailTextView.setText(user.email);

        updateAvatar(user.avatar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.user_fragment, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // This avatar method is used to avoid duplicating code during first display
    // and further update of the user details view. When we upload a new avatar image
    // we need to update the user details view.
    public void updateAvatar(String avatarServingUrl) {
        View view = ((AppCompatActivity)getContext()).getWindow().getDecorView();
        ImageView avatarView = (ImageView) view.findViewById(R.id.avatar_image);

        Picasso.with(getContext()).invalidate(avatarServingUrl);

        Picasso.with(getContext())
                .load(avatarServingUrl)
                .into(avatarView);

        MessageListFragment messageListFragment = new MessageListFragment();

        Bundle bundle = new Bundle();
        bundle.putLong("userId", mUserId);
        messageListFragment.setArguments(bundle);

        ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.list_messages, messageListFragment)
                .commit();
    }

    public void openGallery(int req_code){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((AppCompatActivity) getContext()).startActivityForResult(
                Intent.createChooser(intent,"Select file to upload "), req_code
        );
    }

}

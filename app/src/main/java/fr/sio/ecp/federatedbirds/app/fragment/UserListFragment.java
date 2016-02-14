package fr.sio.ecp.federatedbirds.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.List;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.app.adapter.UsersAdapter;
import fr.sio.ecp.federatedbirds.app.loader.UsersLoader;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by Michaël on 30/11/2015.
 */
public class UserListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<User>> {
    public static final String USERS_LOADER_KEY = "UsersLoaderClassName";
    private static final int LOADER_USERS = 0;
    private UsersAdapter mUsersAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.users_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        RecyclerView listView = (RecyclerView) view.findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsersAdapter = new UsersAdapter();
        listView.setAdapter(mUsersAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(
                LOADER_USERS,
                null,
                this
        );
    }

    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        Class<?> loaderClass = null;
        UsersLoader usersLoader = null;
        Constructor<?> loaderConstructor = null;

        try {
            loaderClass = Class.forName(this.getArguments().getString(USERS_LOADER_KEY));
            loaderConstructor = loaderClass.getConstructor(Context.class, Long.class);
            usersLoader = (UsersLoader) loaderConstructor.newInstance(new Object[] {getContext(), null });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return usersLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> users) {
        mUsersAdapter.setUsers(users);
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) { }

}

package fr.sio.ecp.federatedbirds.app.adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import fr.sio.ecp.federatedbirds.R;
import fr.sio.ecp.federatedbirds.app.activity.UserActivity;
import fr.sio.ecp.federatedbirds.app.task.FollowTask;
import fr.sio.ecp.federatedbirds.auth.TokenManager;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by Michaël on 24/11/2015.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MessageViewHolder> {

    private static List<User> mUsers;
    private static User mLoggedInUser;
    private static ProgressDialog mProgressDialog;

    public void setUsers(List<User> users) {
        mUsers = users;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mUsers != null ? mUsers.size() : 0;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        mProgressDialog = new ProgressDialog(v.getContext());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Update...");
        mLoggedInUser = TokenManager.getLoggedInUser();

        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        User user = mUsers.get(position);

        Picasso.with(holder.mAvatarView.getContext())
                .load(user.avatar)
                .into(holder.mAvatarView);

        holder.mUsernameView.setText(user.login);

        // We hide the button in case of currently logged in user
        // Otherwise we setup the text of button accordingly (follow or unfollow)
        if(mLoggedInUser.id != user.id) {
            if (mLoggedInUser.followerOf.contains(user.id)) {
                holder.mFollowButton.setText(
                        holder.mFollowButton.getContext().getString(R.string.unfollow)
                );
            } else {
                holder.mFollowButton.setText(
                        holder.mFollowButton.getContext().getString(R.string.follow)
                );
            }
            holder.mFollowButton.setVisibility(View.VISIBLE);
        } else {
            holder.mFollowButton.setVisibility(View.GONE);
        }
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mAvatarView;
        private TextView mUsernameView;
        private Button mFollowButton;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mAvatarView = (ImageView) itemView.findViewById(R.id.avatar);
            mUsernameView = (TextView) itemView.findViewById(R.id.username);
            mFollowButton = (Button) itemView.findViewById(R.id.button_follow);

            itemView.findViewById(R.id.avatar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), UserActivity.class);

                    User user = mUsers.get(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putLong("userId", user.id);
                    intent.putExtras(bundle);

                    intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);

                    v.getContext().startActivity(intent);
                }
            });

            itemView.findViewById(R.id.button_follow).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mProgressDialog.show();

                    User user = mUsers.get(getAdapterPosition());

                    boolean followFlag = !mLoggedInUser.followerOf.contains(user.id);
                    FollowTask followTask = null;

                    try {
                        followTask = new FollowTask(v.getContext(), UsersAdapter.class, user, mProgressDialog, mFollowButton);
                        mLoggedInUser = followTask.execute(followFlag).get();
                        TokenManager.setLoggedInUser(mLoggedInUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

}
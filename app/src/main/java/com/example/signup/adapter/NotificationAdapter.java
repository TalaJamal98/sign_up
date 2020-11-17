package com.example.signup.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.signup.Fragment.PostDetailesFragment;
import com.example.signup.Fragment.ProfileFragment;
import com.example.signup.Model.Notification;
import com.example.signup.Model.Post;
import com.example.signup.Model.User;
import com.example.signup.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context mContext;
    private List<Notification> mNotifications;

    public NotificationAdapter(Context mContext, List<Notification> mNotifications) {
        this.mContext = mContext;
        this.mNotifications = mNotifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);

        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Notification notification = mNotifications.get(position);

        getUser(holder.imageProfile, holder.username, notification.getUserid());
        holder.comment.setText(notification.getText());

        final FirebaseUser fUser;
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        final boolean[] flag = {false};
        FirebaseDatabase.getInstance().getReference().child("Notifications").child(fUser.getUid()).child(notification.getNotid()).addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Notification n=snapshot.getValue(Notification.class);
                if(n!=null) {
                    if (n.getSeen().equals("yes")) flag[0] = true;
                    else flag[0] = false;

                    if (flag[0] == true) {
                        holder.row_linearlayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    } else if (flag[0] == false) {
                        holder.row_linearlayout.setBackgroundColor(Color.parseColor("#f0f0f4"));

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.e("not", flag[0]+"" );



        if (notification.isIsPost()) {
            holder.postImage.setVisibility(View.VISIBLE);
            getPostImage(holder.postImage, notification.getPostid());
        } else {
            holder.postImage.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("Seen","yes");

                FirebaseDatabase.getInstance().getReference().child("Notifications").child(fUser.getUid()).child(notification.getNotid()).updateChildren(map);


                if (notification.isIsPost()) {
                    mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
                            .edit().putString("postid", notification.getPostid()).apply();

                    ((FragmentActivity)mContext).getSupportFragmentManager()
                            .beginTransaction().replace(R.id.fragment_container, new PostDetailesFragment()).commit();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("profileid",notification.getUserid());
// set Fragmentclass Arguments
                    ProfileFragment fragobj = new ProfileFragment();
                    fragobj.setArguments(bundle);
                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , fragobj).commit();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imageProfile;
        public ImageView postImage;
        public TextView username;
        public TextView comment;
        LinearLayout row_linearlayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            row_linearlayout=(LinearLayout)itemView.findViewById(R.id.ll);

            imageProfile = itemView.findViewById(R.id.pic);
            postImage = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.the_not);
        }
    }

    private void getPostImage(final ImageView imageView, String postId) {
        FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                Picasso.get().load(post.getImageurl()).placeholder(R.mipmap.ic_launcher).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUser(final ImageView imageView, final TextView textView, String userId) {
        FirebaseDatabase.getInstance().getReference().child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getProfilepic()==null||user.getProfilepic().equals("")) {
                    imageView.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getProfilepic()).into(imageView);
                }
                textView.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
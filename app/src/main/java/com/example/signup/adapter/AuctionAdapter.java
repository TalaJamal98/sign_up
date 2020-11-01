package com.example.signup.adapter;


import android.content.Context;
import android.content.Intent;
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

import com.example.signup.CommentActivity;
import com.example.signup.FollowerActivity;
import com.example.signup.Fragment.PostDetailesFragment;
import com.example.signup.Fragment.ProfileFragment;
import com.example.signup.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;

import com.example.signup.Model.Post;
import com.example.signup.Model.User;
import com.example.signup.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.Viewholder> {

    private Context mContext;
    private List<Post> mPosts;

    private FirebaseUser firebaseUser;

    public AuctionAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.auction_item, parent, false);
        return new AuctionAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, int position) {

        final Post post = mPosts.get(position);
        Picasso.get().load(post.getImageurl()).into(holder.postImage);
        // holder.description.setText(post.getDescription());

        FirebaseDatabase.getInstance().getReference().child("users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if(user!=null) {
                    Log.e("err",user.getId());

                    if (user.getProfilepic()==null || user.getProfilepic().equals("")) {
                        // Log.e("err",user.getProfile_pic());

                        holder.postImage.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Picasso.get().load(user.getProfilepic()).into(holder.postImage);

                    }

                    holder.author.setText(user.getFirstname());
                    holder.prodName.setText(post.getName());


                }
                else{
                    Log.v("no",post.getPublisher());

                    Log.v("no","no user");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





   /*     holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.save.getTag().equals("save")) {
                    FirebaseDatabase.getInstance().getReference().child("Saves")
                            .child(firebaseUser.getUid()).child(post.getPostid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Saves")
                            .child(firebaseUser.getUid()).child(post.getPostid()).removeValue();
                }
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public ImageView postImage;
        public TextView author;
        public TextView prodName;





        public Viewholder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.auction_image);
            author = itemView.findViewById(R.id.name);
            prodName = itemView.findViewById(R.id.prod);


        }
    }





}
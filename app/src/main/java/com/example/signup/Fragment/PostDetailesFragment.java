package com.example.signup.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.signup.CommentActivity;
import com.example.signup.FollowerActivity;
import com.example.signup.MessageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.signup.adapter.PostAdapter;
import com.example.signup.Model.Post;
import com.example.signup.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PostDetailesFragment extends Fragment {
    private String postId;
    private List<Post> postList;
private Post post;
    private FirebaseUser firebaseUser;

private TextView name;
    private TextView price;
    private TextView size;
    private TextView color;
    private TextView category;
    private TextView description;
    private ImageView img;
    public ImageView like;
    public ImageView comment;

    public TextView noOfLikes;
    public TextView noOfComments;
    public TextView likeT;
    public LinearLayout comLay;
    private Button b;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detailes, container, false);
        name=view.findViewById(R.id.name);
        price=view.findViewById(R.id.price);
        size=view.findViewById(R.id.size);
        color=view.findViewById(R.id.color);
        category=view.findViewById(R.id.cat);
        description=view.findViewById(R.id.description);
        img=view.findViewById(R.id.product);
b=view.findViewById(R.id.contact);
        noOfLikes = view.findViewById(R.id.post_likes_no);
        //    author = itemView.findViewById(R.id.author);
        noOfComments = view.findViewById(R.id.post_comments_no);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        like = view.findViewById(R.id.like);
        comment = view.findViewById(R.id.comment);
        comLay = view.findViewById(R.id.comments);
        likeT = view.findViewById(R.id.liketext);


        postId = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("postid", "none");



        FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              post=dataSnapshot.getValue(Post.class);
                Log.e("nnn000", post.getImageurl() );
                Picasso.get().load(post.getImageurl()).into(img);
                category.setText(post.getCategory());
                price.setText(post.getPrice());
                name.setText(post.getName());
                description.setText(post.getDescription());
                size.setText(post.getSize());
                color.setText(post.getColor());
                isLiked(post.getPostid(), like);
                NoOfLikes(post.getPostid(), noOfLikes);
                getComments(post.getPostid(), noOfComments);
                if(!firebaseUser.getUid().equals(post.getPublisher())){
                    b.setVisibility(View.VISIBLE);
                }
                else
                    b.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


b.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.putExtra("userid", post.getPublisher());
        startActivity(intent);

    }
});

       like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);

                    addNotification(post.getPostid(), post.getPublisher());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });


        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                intent.putExtra("postId", post.getPostid());
                intent.putExtra("authorId", post.getPublisher());
                startActivity(intent);
            }
        });

        noOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ninino", post.getPublisher()+"hi");

                Intent intent = new Intent(getActivity(), CommentActivity.class);
                intent.putExtra("postId", post.getPostid());
                intent.putExtra("authorId", post.getPublisher());
               startActivity(intent);
            }
        });

        comLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                intent.putExtra("postId", post.getPostid());
                intent.putExtra("authorId", post.getPublisher());
               startActivity(intent);
            }
        });

noOfLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FollowerActivity.class);
                intent.putExtra("id", post.getPublisher());
                intent.putExtra("title", "likes");
                intent.putExtra("postid", post.getPostid());
                Log.e("hi", post.getPostid());


                startActivity(intent);
            }
        });

        likeT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FollowerActivity.class);
                intent.putExtra("id", post.getPublisher());
                intent.putExtra("title", "likes");
                intent.putExtra("postid", post.getPostid());

                //Log.e("hi", post.getPostid());

                startActivity(intent);
            }
        });

        return view;
    }



    private void isLiked(String postId, final ImageView imageView) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.heart);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.heart1);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void NoOfLikes(String postId, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getComments(String postId, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addNotification(String postId, String publisherId) {
        HashMap<String, Object> map = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notifications");
        String notId = ref.push().getKey();

        if (!publisherId.equals(firebaseUser.getUid())) {
            Log.d("fede", "" + publisherId);
            Log.d("fede", "" + firebaseUser.getUid());

            map.put("userid", firebaseUser.getUid());
            map.put("text", "liked your post.");
            map.put("postid", postId);
            map.put("isPost", true);
            map.put("Seen", "no");
            map.put("notid", notId);


            FirebaseDatabase.getInstance().getReference().child("Notifications").child(publisherId).child(notId).setValue(map);
        }
    }
}
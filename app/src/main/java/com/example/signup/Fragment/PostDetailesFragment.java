package com.example.signup.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.signup.adapter.PostAdapter;
import com.example.signup.Model.Post;
import com.example.signup.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class PostDetailesFragment extends Fragment {
    private String postId;
    private List<Post> postList;
private Post post;

private TextView name;
    private TextView price;
    private TextView size;
    private TextView color;
    private TextView category;
    private TextView description;
    private ImageView img;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detailes, container, false);
        name=view.findViewById(R.id.name);
        price=view.findViewById(R.id.price);
        size=view.findViewById(R.id.size);
        color=view.findViewById(R.id.color);
        category=view.findViewById(R.id.cat);
        description=view.findViewById(R.id.des);
        img=view.findViewById(R.id.product);

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
package com.example.signup.Fragment;
import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.signup.ChatAndUserActivity;

import com.example.signup.MainActivity;
import com.example.signup.Model.Chat;
import com.example.signup.Model.User;
import com.example.signup.Model.auction;
import com.example.signup.adapter.AuctionAdapter;
import com.example.signup.createEvent;
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

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPosts;

    FirebaseUser firebaseUser ;
    DatabaseReference databaseReference ;
    private PostAdapter postAdapter;
    private AuctionAdapter auctionAdapter;

    private List<Post> postList;
    private List<auction> auctionList;
    private RecyclerView recyclerViewAuction;
ProgressBar bar;
    TextView b;

    private List<String> followingList;
    ImageView chat;
ImageButton a;
    private List<String> sendlist ;
    private List<Chat> chatlist ;
    private List<String> senderlist ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
chat=view.findViewById(R.id.chat);
        b=view.findViewById(R.id.chat_badge);

        a=view.findViewById(R.id.create);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        recyclerViewAuction = view.findViewById(R.id.recycler_view_Auctions);
        bar=view.findViewById(R.id.pregress);
        chatlist = new ArrayList<>();
        senderlist = new ArrayList<>();
        sendlist = new ArrayList<>();
        recyclerViewPosts.setHasFixedSize(true);
        recyclerViewAuction.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        recyclerViewPosts.setLayoutManager(linearLayoutManager);
        recyclerViewAuction.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));



        postList = new ArrayList<>();
        auctionList=new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        auctionAdapter = new AuctionAdapter(getContext(), auctionList);

        recyclerViewPosts.setAdapter(postAdapter);
        recyclerViewAuction.setAdapter(auctionAdapter);

        followingList = new ArrayList<>();

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChatAndUserActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);

            }
        });


        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), createEvent.class);
                startActivity(i);
            }
        });


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatlist.clear();
                sendlist.clear();
                senderlist.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getReceiver().equals(firebaseUser.getUid()))
                    {
                        //if(!chatlist.contains(chat.getSender()))
                        sendlist.remove(chat.getSender());
                        sendlist.add(chat.getSender());

                        chatlist.remove(chat);
                        chatlist.add(chat);

                    }
                }
              //  Log.e("chatlist size", chatlist.size()+"");
              for (int j =chatlist.size()-1; j >=0; j--) {
                //    Log.e("chatlist1", chatlist.size()+"");

                    if (chatlist.get(j).getSeenstatus().equals("seen")) {
                  //      Log.e("chatlist", chatlist.get(j).getMessage());
                    //    Log.e("J", j+"");

                        chatlist.remove(j);
                    }
                }
              //  Log.e("chatlist size2", chatlist.size()+"");

               for(int i=0;i<sendlist.size();i++) {
                    for (int j = 0; j < chatlist.size(); j++) {

                        if (chatlist.get(j).getSender().equals(sendlist.get(i))) {
                            senderlist.remove(sendlist.get(i));
                            senderlist.add(sendlist.get(i));
                        }
                    }
                }
                for(int i=0;i<chatlist.size();i++){
                    Log.e("chat", chatlist.get(i).getMessage()+"" );
                }
               /* for(int i=0;i<sendlist.size();i++){
                    Log.e("send", sendlist.get(i) );
                }
*/
                b.setText(senderlist.size()+"");
                if(senderlist.size()>0)
                    b.setVisibility(View.VISIBLE);
                else
                    b.setVisibility(View.GONE);
          //      Log.e("see", senderlist.size()+"" );



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        checkFollowingUsers();

        return view;
    }

    private void checkFollowingUsers() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    followingList.add(snapshot.getKey());
                }
                followingList.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                readPosts();
                readAuction();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void readPosts() {

        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    for (String id : followingList) {
                        if (post.getPublisher().equals(id)) {
                            postList.add(post);
                        }
                    }
                }
                postAdapter.notifyDataSetChanged();
                bar.setVisibility(View.GONE);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readAuction() {

        FirebaseDatabase.getInstance().getReference().child("Auctions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                auctionList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    auction auction1 = snapshot.getValue(auction.class);

                    for (String id : followingList) {
                        if (auction1.getPublisher().equals(id)) {
                            auctionList.add(auction1);
                        }
                    }
                }
                auctionAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}

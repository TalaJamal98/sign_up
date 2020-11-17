package com.example.signup.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signup.ChatAndUserActivity;
import com.example.signup.EditProfileActivity;
import com.example.signup.FollowerActivity;
import com.example.signup.MessageActivity;
import com.example.signup.Model.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.example.signup.adapter.PhotoAdapter;
import com.example.signup.adapter.PostAdapter;
//import com.example.signup.EditProfileActivity;
//import com.example.signup.FollowersActivity;
import com.example.signup.Model.Post;
import com.example.signup.Model.User;
//import com.example.signup.OptionsActivity;
import com.example.signup.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class ProfileFragment extends Fragment {
    private TextView followers;
    private TextView following;
   private TextView posts;
    private TextView followersno;
    private TextView followingno;
    private TextView postsno;
    private CircleImageView imageProfile;
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;
    DatabaseReference databaseReference ;
    private List<String> sendlist ;
    private List<Chat> chatlist ;
    private List<String> senderlist ;
    ProgressBar bar;
    TextView b;
    ImageView back1;
    RatingBar ratingBar;
    // private TextView fullname;
    //private TextView bio;
    private TextView city;

    private TextView username;
    private TabLayout tabLayout ;
   private  ViewPager2 viewPager2 ;

    private Button editProfile;
    private ImageButton send;
    private ImageButton rate;

    private FirebaseUser fUser;
    ImageView chat;

    String profileId;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        chatlist = new ArrayList<>();
        senderlist = new ArrayList<>();
        sendlist = new ArrayList<>();
        b=view.findViewById(R.id.chat_badge);
        chat=view.findViewById(R.id.chat);
        back1=view.findViewById(R.id.back);
        ratingBar=view.findViewById(R.id.ratingBar1);

  //  Intent intent = getIntent();
        SharedPreferences prefs=getContext().getSharedPreferences("PREFS",Context.MODE_PRIVATE);


      //  profileId=intent.getStringExtra("publisherId");
      //  Bundle bundle = this.getArguments();

        Bundle bundle = getArguments();
        profileId =bundle.getString("profileid");


      // profileId =prefs.getString("publisher", "none");
//       profileId = getArguments().getString("publisher");

        imageProfile = view.findViewById(R.id.profile_image);
        //options = view.findViewById(R.id.options);
        followers = view.findViewById(R.id.followersT);
        following = view.findViewById(R.id.followingT);
        posts = view.findViewById(R.id.postsT);
        followersno = view.findViewById(R.id.followersNum);
        followingno = view.findViewById(R.id.followingNum);
        postsno = view.findViewById(R.id.postsNum);
        //fullname = view.findViewById(R.id.fullname);
        //bio = view.findViewById(R.id.bio);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewpagerid);
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#f2c40d"));
        username = view.findViewById(R.id.city);

        username = view.findViewById(R.id.user_name);
      //  myPictures = view.findViewById(R.id.my_pictures);
       // savedPictures = view.findViewById(R.id.saved_pictures);
        editProfile = view.findViewById(R.id.edit_profile);
        send = view.findViewById(R.id.send);
        rate = view.findViewById(R.id.rate);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtra("userid", profileId);
                startActivity(intent);

            }
        });


        fUser = FirebaseAuth.getInstance().getCurrentUser();

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
                    if (chat.getReceiver().equals(fUser.getUid()))
                    {
                        //if(!chatlist.contains(chat.getSender()))
                        sendlist.remove(chat.getSender());
                        sendlist.add(chat.getSender());

                        chatlist.remove(chat);
                        chatlist.add(chat);

                    }
                }
                for (int j =chatlist.size()-1; j >=0; j--) {

                    if (chatlist.get(j).getSeenstatus().equals("seen")) {

                        chatlist.remove(j);
                    }
                }

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

                b.setText(senderlist.size()+"");
                if(senderlist.size()>0)
                    b.setVisibility(View.VISIBLE);
                else
                    b.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChatAndUserActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);

            }
        });



                viewPager2.setAdapter(new Customadapter(this));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position)
                {
                    case 0:
                        tab.setText("About Me");
                        break;
                    case 1:
                        tab.setText("Posts");
                        break;

                }
            }
        }
        );


        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().popBackStack();
            }
        });
        FirebaseDatabase.getInstance().getReference().child("ratings").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final double[] rateVal = {0};
                int count=0;

                for(DataSnapshot rating:snapshot.getChildren()){
                    Log.e("rate", rating.child("rateValue").getValue()+"" );
                    rateVal[0] += Double.parseDouble(rating.child("rateValue").getValue().toString());
                    count++;
                }
                if(count>0)
                ratingBar.setRating((float)rateVal[0]/count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        tabLayoutMediator.attach();
   /*     recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        recyclerViewPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);*/
      /*  postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerViewPosts.setAdapter(postAdapter);*/

        userInfo();
        getFollowersAndFollowingCount();
        getPostCount();
//readPosts();
     //   Log.e("ninini", profileId);
        if (profileId.equals(fUser.getUid())) {
            editProfile.setText("Edit profile");
            send.setVisibility(View.GONE);
           rate.setVisibility(View.GONE);
        } else {
            send.setVisibility(View.VISIBLE);
            rate.setVisibility(View.VISIBLE);

            checkFollowingStatus();
        }

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnText = editProfile.getText().toString();

                if (btnText.equals("Edit profile")) {
                    startActivity(new Intent(getContext(), EditProfileActivity.class));
                } else {
                    if (btnText.equals("follow")) {
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid())
                                .child("following").child(profileId).setValue(true);

                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                                .child("followers").child(fUser.getUid()).setValue(true);
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid())
                                .child("following").child(profileId).removeValue();

                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                                .child("followers").child(fUser.getUid()).removeValue();
                    }
                }
            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowerActivity.class);
                intent.putExtra("id", profileId);
                intent.putExtra("title", "followers");
                startActivity(intent);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowerActivity.class);
                intent.putExtra("id", profileId);
                intent.putExtra("title", "followings");
                startActivity(intent);
            }
        });

        followingno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowerActivity.class);
                intent.putExtra("id", profileId);
                intent.putExtra("title", "followings");
                startActivity(intent);
            }
        });

        followersno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowerActivity.class);
                intent.putExtra("id", profileId);
                intent.putExtra("title", "followers");
                startActivity(intent);
            }
        });

return view;
    }


    private void checkFollowingStatus() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileId).exists()) {
                    editProfile.setText("following");
                } else {
                    editProfile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    /*private void readPosts() {

        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    if (post.getPublisher().equals(profileId)) {
                        postList.add(post);
                    }
                }

                Collections.reverse(postList);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/

    private void getPostCount() {

        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    if (post.getPublisher().equals(profileId)) counter ++;
                }

                postsno.setText(String.valueOf(counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void ShowDialog()
    {
        final AlertDialog popDialog = new AlertDialog.Builder(getContext()).create();

        LinearLayout linearLayout = new LinearLayout(getContext());
        final RatingBar rating = new RatingBar(getContext());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rating.setLayoutParams(lp);
        rating.setNumStars(5);
        rating.setStepSize((float) 0.5);

        //add ratingBar to linearLayout
        linearLayout.addView(rating);


        popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle("Add Rating: ");

        //add linearLayout to dailog
        popDialog.setView(linearLayout);

        popDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cansel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        popDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                ratingBar.setRating(rating.getRating());
                HashMap<String, Object> map = new HashMap<>();
                map.put("rateValue", rating.getRating());

DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("ratings").child(profileId).child(fUser.getUid());
mDatabase.setValue(map);
                dialog.dismiss();
            }
        });

        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                System.out.println("Rated val:"+v);
            }
        });
        popDialog.show();

    }

    private void getFollowersAndFollowingCount() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId);

        ref.child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followersno.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingno.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void userInfo() {

        FirebaseDatabase.getInstance().getReference().child("users").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("nnn", profileId );
                User user = dataSnapshot.getValue(User.class);
                if (user.getProfilepic()==null || user.getProfilepic().equals("")) {
                    // Log.e("err",user.getProfile_pic());

                  imageProfile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getProfilepic()).into(imageProfile);

                }
                username.setText(user.getUsername());
               // username.setText(user.getUsername());
               // bio.setText(user.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static ProfileFragment newInstance(String busName) {
        ProfileFragment yourFragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("profileid", busName);

        yourFragment.setArguments(args);
        return yourFragment;
    }


    public class Customadapter extends FragmentStateAdapter {

        public Customadapter(@NonNull ProfileFragment fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position)
            {
                case 0:
                    Bundle bundle1 = new Bundle();


                    bundle1.putString("profileid", profileId);

                    AboutFragment mFragment_B1 = new AboutFragment();
                    mFragment_B1.setArguments(bundle1);
                    return mFragment_B1;

                case 1:

                    Bundle bundle = new Bundle();


                    bundle.putString("profileid", profileId);

                    ProfilePostsFragment mFragment_B = new ProfilePostsFragment();
                    mFragment_B.setArguments(bundle);
                    return mFragment_B;
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
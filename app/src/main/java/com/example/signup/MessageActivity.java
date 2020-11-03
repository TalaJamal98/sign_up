package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.signup.Model.Chat;
import com.example.signup.Model.User;
import com.example.signup.adapter.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    CircleImageView imageView;
    CircleImageView RimageView;

    TextView textView ;
    FirebaseUser firebaseUser ;
    DatabaseReference databaseReference ;
    Intent intent ;
    ImageButton imageButton;
    EditText editText;
    RecyclerView recyclerView;
    MessageAdapter messageadapter;
    List<Chat> mChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
      //  RimageView= findViewById(R.id.imgvchatuser);
        imageView = findViewById(R.id.imagemsgtool);
        textView = findViewById(R.id.txtmsgtool);
        imageButton = findViewById(R.id.sendbutton);
        editText = findViewById(R.id.sendmsgedittext);
        recyclerView = findViewById(R.id.recyclerviewchat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        intent = getIntent();
        final String userid = intent.getStringExtra("userid");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                textView.setText(user.getUsername());
                if(user.getProfilepic()==null || user.getProfilepic().equals("")){
                   RimageView.setImageResource(R.mipmap.ic_launcher);

                }
                else{
                    Picasso.get().load(user.getProfilepic()).into(imageView);
                }
                readmessage(firebaseUser.getUid(),userid,user.getProfilepic());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
      //  seenMessage(userid);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                if (!message.equals(""))
                {
                    sendMessage(firebaseUser.getUid(),userid,message);
                }
                editText.setText(null);
            }
        });

    }

    public void sendMessage(String sender , String receiver , String message )
    {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("seenstatus","unseen");

        reference.child("Chats").push().setValue(hashMap);


                }

    public void readmessage(final String myid , final String userid,final String image) {
        mChat = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    assert chat != null;
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mChat.add(chat);
                    }


                    messageadapter = new MessageAdapter(MessageActivity.this, mChat);
                    recyclerView.setAdapter(messageadapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }






    }
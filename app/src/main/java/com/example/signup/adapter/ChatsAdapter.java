package com.example.signup.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signup.MessageActivity;
import com.example.signup.Model.Chat;
import com.example.signup.Model.User;
import com.example.signup.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MyViewholder> {

    Context context ;
    List<User> mUsers ;
    String thelastmsg ;
    FirebaseUser firebaseUser ;
    DatabaseReference databaseReference ;

    public ChatsAdapter(Context context ,List<User> mUsers) {
        this.context = context;
        this.mUsers= mUsers ;
    }

    @NonNull
    @Override
    public ChatsAdapter.MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item,parent,false);
        return new ChatsAdapter.MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatsAdapter.MyViewholder holder, int position) {
        final User user = mUsers.get(position);
        holder.textView.setText(user.getUsername());

       // holder.textView2.setText("Last message");
        Picasso.get().load(user.getProfilepic()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid",user.getId());
                context.startActivity(intent);
            }

        });
        lastMessage(user.getId(),holder.textView2,user,holder.textView);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    class MyViewholder extends RecyclerView.ViewHolder{
        public TextView textView , textView2 ;
        public CircleImageView imageView;
        public LinearLayout linearLayout ;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewchat);
            imageView = itemView.findViewById(R.id.imageViewchat);
            linearLayout = itemView.findViewById(R.id.linearlayout);
            textView2 = itemView.findViewById(R.id.lastmessagetextid);

        }
    }

    public void lastMessage (final String userid , final TextView textView , final User user , final TextView textView1)
    {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid()))
                    {
                        if (!chat.getSender().equals(firebaseUser.getUid()))
                            thelastmsg = chat.getMessage();
                        else
                            thelastmsg = "You : "+chat.getMessage();
                    }
                    textView.setText(thelastmsg);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
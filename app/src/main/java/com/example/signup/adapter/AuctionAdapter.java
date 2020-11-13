package com.example.signup.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.signup.CommentActivity;
import com.example.signup.Model.Notification;
import com.example.signup.Model.User;
import com.example.signup.Model.auction;
import com.example.signup.R;
import com.example.signup.event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.viewholder>{

    private Context mContext;
    private List<auction> mauction;

    private FirebaseUser firebaseUser;

    public AuctionAdapter(Context mContext, List<auction> mauction) {
        this.mContext = mContext;
        this.mauction = mauction;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.auction_item,parent,false);
        return new AuctionAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final auction auctionn =mauction.get(position);

        holder.cat.setText(auctionn.getCategory());

        FirebaseDatabase.getInstance().getReference().child("users").child(auctionn.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                holder.name.setText(user.getFirstname()+" "+user.getSecondname());

                holder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i= new Intent(mContext, event.class);
                        i.putExtra("myid",auctionn.getAuctionId());
                        mContext.startActivity(i);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public int getItemCount() {
     return mauction.size();
       // return 0;

    }


    public class  viewholder extends RecyclerView.ViewHolder{
        public ImageView profile;
        public TextView name;
        public TextView cat;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            profile= itemView.findViewById(R.id.auction_image);
            name=itemView.findViewById(R.id.name);
            cat=itemView.findViewById(R.id.prod);
        }
    }



}

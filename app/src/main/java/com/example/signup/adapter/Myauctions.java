package com.example.signup.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.signup.Model.auction;
import com.example.signup.R;
import com.example.signup.event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class Myauctions extends RecyclerView.Adapter<Myauctions.ViewHolder>{

    private Context mContext;
    String n, t, d ,h ,p;
    private List<auction> mAuction;

    private FirebaseUser firebaseUser;

    public Myauctions(Context mContext, List<auction> mAuction) {
        this.mContext = mContext;
        this.mAuction = mAuction;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.myauction_item,parent,false);
        return new Myauctions.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        auction auc =mAuction.get(position);

        holder.name.setText(n+ ""+ auc.getName());
        holder.highPrice.setText(h +""+ auc.getPrice());
        holder.time.setText(t+""+auc.getTime());
        holder.date.setText(d+""+auc.getDate());



    }



    @Override
    public int getItemCount() {
        return mAuction.size();
       // return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView pic;
        public TextView name;
        public TextView time;
        public TextView date;
        public TextView highPrice;
        public TextView person;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pic=itemView.findViewById(R.id.product);
            name=itemView.findViewById(R.id.productName);
            time=itemView.findViewById(R.id.timeRemaining);
            date=itemView.findViewById(R.id.date);
            highPrice=itemView.findViewById(R.id.higherPrice);
            person=itemView.findViewById(R.id.customer);

          n=  name.getText().toString();
          t= time.getText().toString();
          d= date.getText().toString();
          h=highPrice.getText().toString();
          p=person.getText().toString();




        }
    }


}

package com.example.signup.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

        holder.name.setText(auc.getName());
        holder.highPrice.setText( auc.getPrice());
        holder.date.setText(auc.getDate());
        Date date1=null;
        Date date2=null;

        long date = System.currentTimeMillis();

        SimpleDateFormat df = new SimpleDateFormat("kk:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
        String dateString = df.format(date);
        Date curr1=null;
        try {
            curr1=df.parse(dateString);

            date1 = df.parse(auc.getStartTime());
            date2 = df.parse(auc.getEndTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference=date2.getTime()-curr1.getTime();
        holder.person.setText(auc.getBuyer());
        Picasso.get().load(auc.getImageurl()).into(holder.pic);

        if(difference>0) {

    int days = (int) (difference / (1000 * 60 * 60 * 24));
    int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
    int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
    hours = (hours < 0 ? -hours : hours);
    Log.i("======= Hours", " :: " + hours);
    holder.time.setText(hours + " hours");
}
else{
    holder.time.setText("Finished");

}

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
            name=itemView.findViewById(R.id.Name);
            time=itemView.findViewById(R.id.time);
            date=itemView.findViewById(R.id.date);
            highPrice=itemView.findViewById(R.id.price);
            person=itemView.findViewById(R.id.customer);

          n=  name.getText().toString();
          t= time.getText().toString();
          d= date.getText().toString();
          h=highPrice.getText().toString();
          p=person.getText().toString();




        }
    }


}

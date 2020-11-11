package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.signup.Model.User;
import com.example.signup.Model.auction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class event extends AppCompatActivity {

    private TextView pname;
    private TextView pdes;
    private TextView pcat;
    private TextView pprice;
    private TextView ptime;
    private Button psave;
    private ImageView pup;
    private FirebaseUser fUser;
    private StorageReference storageRef;
    Intent intent;
    String myid;
    private CountDownTimer timer;


    long num;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        pname=findViewById(R.id.name);
        pdes=findViewById(R.id.des);
        pcat=findViewById(R.id.cat);
        pprice=findViewById(R.id.price);
        ptime=findViewById(R.id.time);
        psave=findViewById(R.id.save);
        pup=findViewById(R.id.more);


        Intent intent = getIntent();
        myid = intent.getStringExtra("myid");

        fUser = FirebaseAuth.getInstance().getCurrentUser();
       FirebaseDatabase.getInstance().getReference().child("Auctions").child(myid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    auction user = dataSnapshot.getValue(auction.class);

                    pname.setText(user.getName());
                    pcat.setText(user.getCategory());
                    pdes.setText(user.getDescription());
                    pprice.setText(user.getPrice());
                   // ptime.setText(user.getTime());

                    String something = user.getTime();
                    int h= Integer.parseInt(something);
                    int m= h*60;
                    int s= m*60;
                    int ms=s*1000;

                    num = (long)ms;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        pup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String newprice= pprice.getText().toString();
            int finalprice=  Integer.parseInt(newprice);
            finalprice=finalprice+10;
            pprice.setText(finalprice);

            }


        });
        psave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("price", pprice);

                FirebaseDatabase.getInstance().getReference().child("Auctions").child(fUser.getUid()).updateChildren(map);
            }
        });
    }

    public void start(){
        timer = new CountDownTimer(num, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                num=millisUntilFinished;
                update();

            }

            @Override
            public void onFinish() {
                timer.cancel();
            }
        }.start();

    }

    public void update(){
        int hours = (int) (num / 1000)/3600;
        int minutes= (int) ((num/1000) % 3600)/60;
        int sec =(int) (num/1000)%60;
        String timetext;
        if(hours>0){
            timetext=""+hours + ": "+minutes+":"+sec;

        }
        else{
            timetext=""+minutes+":"+sec;
        }



        ptime.setText(timetext);
    }

    public void checkDate(){
        Calendar calender = Calendar.getInstance();

    }

}
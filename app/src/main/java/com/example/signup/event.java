package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class event extends AppCompatActivity {

    private TextView pname;
    private TextView pdes;
    private TextView pcat;
    private TextView pprice;
    private TextView ptime;
    private TextView pdate;
    private Button psave;
    private Button pup;
    private FirebaseUser fUser;
    private StorageReference storageRef;
    Intent intent;
    String myid;
    ImageView img;
    private CountDownTimer timer;
    String full;
    private TextView auctionDate;
    long num;
    boolean isRunning;
    private long mEndTime;
    int curDay,curMonth,curYear;
    int cyear,cmonth,cday;
    private ImageView back;
    int finalprice;

    String finall ;
    auction user;
    User  user1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        back=findViewById(R.id.back);
        pname=findViewById(R.id.name);
        pdes=findViewById(R.id.des);
        pcat=findViewById(R.id.cat);
        pprice=findViewById(R.id.price);
        ptime=findViewById(R.id.time);
        psave=findViewById(R.id.save);
        pup=findViewById(R.id.moree);
        auctionDate=findViewById(R.id.auDate);
img=findViewById(R.id.pic);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent (event.this, MainActivity.class);
                startActivity(i);
            }
        });

        Calendar calender= Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calender.getTime());
         curYear = calender.get(Calendar.YEAR);
         curMonth = calender.get(Calendar.MONTH)+1;
         curDay= calender.get(Calendar.DAY_OF_MONTH);



        Intent intent = getIntent();
        myid = intent.getStringExtra("myid");


        fUser = FirebaseAuth.getInstance().getCurrentUser();
       FirebaseDatabase.getInstance().getReference().child("Auctions").child(myid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(auction.class);

                    pname.setText(user.getName());
                    pcat.setText("Category" +user.getCategory());
                    pdes.setText("Descreption: "+user.getDescription());
                    pprice.setText(user.getPrice());
                    auctionDate.setText(user.getDate());
                    Picasso.get().load(user.getImageurl()).into(img);
                   DateFormat df = new java.text.SimpleDateFormat("kk:mm:ss");


                    long date = System.currentTimeMillis();

                    SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
                    String dateString = sdf.format(date);
Date curr1=null;

                    Date date1 = null;
                    Date date2 = null;
                    try {
                        curr1=df.parse(dateString);
                        date1 = df.parse(user.getStartTime());
                        date2 = df.parse(user.getEndTime());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long diff = date2.getTime() - date1.getTime();



                    long curr=date2.getTime()-curr1.getTime();

                        num = curr;

                    full=user.getDate();

                    String[] separated = auctionDate.getText().toString().split("/");
                    cyear= Integer.parseInt(separated[2]);
                     cmonth= Integer.parseInt(separated[1]);
                     cday= Integer.parseInt(separated[0]);



                    if(curDay==cday && curMonth==cmonth && curYear==cyear && num>0){
                        pup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(isRunning) {



                                    FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).addValueEventListener(new ValueEventListener(){
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        user1=dataSnapshot.getValue(User.class);



        String newprice =user.getPrice();
                                           finalprice = Integer.valueOf(newprice);

                                            finalprice = finalprice + 10;
                                           finall = Integer.toString(finalprice);

                                            pprice.setText(finall);


                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });



                                }

                            }


                        });

                        start();
                        //onStart();
                        //update();

                    }else
                    {
if(num<=0){
    ptime.setText("Finished");

}
                    }






                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        psave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null && user1 != null) {
                    if (Integer.parseInt(finall) > Integer.parseInt(user.getPrice())) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("price", pprice.getText().toString());
                        map.put("buyer", user1.getUsername());
                        FirebaseDatabase.getInstance().getReference().child("Auctions").child(myid).updateChildren(map);
                    }


                }
            }
        });
    }

/*    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor= prefs.edit();
        editor.putLong("millisleft", num);
        editor.putBoolean("timerRunning",isRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

    }


    @Override
    protected void onStart() {
            super.onStart();

            SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            num = prefs.getLong("millisleft", num);
            isRunning = prefs.getBoolean("timerRunning", false);
            update();

            if (isRunning) {
                mEndTime = prefs.getLong("endTime", 0);
               num = mEndTime - System.currentTimeMillis();

                if (num < 0) {
                    num = 0;
                    isRunning = false;
                    update();
                } else {
                    start();

                }

            }


    }*/



    public void start(){

           // mEndTime = System.currentTimeMillis() + num;
            timer = new CountDownTimer(num, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    isRunning = true;
                    num = millisUntilFinished;
                    update();

                }

                @Override
                public void onFinish() {
                    isRunning = false;
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

}
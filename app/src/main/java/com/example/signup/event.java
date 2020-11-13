package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

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
    private CountDownTimer timer;
    String full;
    private TextView auctionDate;
    long num;
    boolean isRunning;
    private long mEndTime;
    int curDay,curMonth,curYear;
    int cyear,cmonth,cday;
    private ImageView back;




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
         curMonth = calender.get(Calendar.MONTH);
         curDay= calender.get(Calendar.DAY_OF_MONTH);

        Toast.makeText(event.this, "curday "+ curDay,
                Toast.LENGTH_SHORT).show();
        Toast.makeText(event.this, " curmonth "+ curMonth,
                Toast.LENGTH_SHORT).show();
        Toast.makeText(event.this, " curyear "+ curYear,
                Toast.LENGTH_SHORT).show();


        Intent intent = getIntent();
        myid = intent.getStringExtra("myid");


        fUser = FirebaseAuth.getInstance().getCurrentUser();
       FirebaseDatabase.getInstance().getReference().child("Auctions").child(myid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    auction user = dataSnapshot.getValue(auction.class);

                    pname.setText(user.getName());
                    pcat.setText(pcat.getText()+" " +user.getCategory());
                    pdes.setText(pdes.getText()+" "+user.getDescription());
                    pprice.setText(user.getPrice());
                    auctionDate.setText(user.getDate());
                    ptime.setText(user.getTime());


                   String something= user.getTime().toString();
                    int h= Integer.parseInt(something);
                    int m= h*60;
                    int s= m*60;
                    int ms=s*1000;
                    num = (long)ms;


                    full=user.getDate();

                    String[] separated = auctionDate.getText().toString().split("/");
                    cyear= Integer.parseInt(separated[2]);
                     cmonth= Integer.parseInt(separated[1]);
                     cday= Integer.parseInt(separated[0]);

                    Toast.makeText(event.this, "auction day "+ cday,
                            Toast.LENGTH_SHORT).show();
                    Toast.makeText(event.this, "auction month "+ cmonth,
                            Toast.LENGTH_SHORT).show();
                    Toast.makeText(event.this, "auction year "+ cyear,
                            Toast.LENGTH_SHORT).show();

                    if(curDay==cday && curMonth==cmonth && curYear==cyear){
                        pup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(isRunning) {

                                    String newprice = pprice.getText().toString();
                                    int finalprice = Integer.valueOf(newprice);

                                    finalprice = finalprice + 10;
                                    String finall = Integer.toString(finalprice);
                                    pprice.setText(finall);
                                }

                            }


                        });

                        start();
                        //onStart();
                        //update();
                        Toast.makeText(event.this, "year:  "+ cyear,
                                Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(event.this, "the auction is held on "+ full,
                                Toast.LENGTH_SHORT).show();
                    }






                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        psave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("price", pprice.getText().toString());

                FirebaseDatabase.getInstance().getReference().child("Auctions").child(myid).updateChildren(map);
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

            mEndTime = System.currentTimeMillis() + num;
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
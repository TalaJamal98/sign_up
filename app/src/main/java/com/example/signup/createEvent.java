package com.example.signup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class createEvent extends AppCompatActivity {

    private EditText name,desc,cat,price;
    private EditText time;
    Button add;
    private CountDownTimer timer;
    TextView donee;
    Button Mydate;
    private long left;
    long num;
    String full;
    String myname, mycat,mydesc,myprice,mytime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        name= findViewById(R.id.name);
        desc=findViewById(R.id.color);
        cat=findViewById(R.id.category);
        price=findViewById(R.id.price);
        time=findViewById(R.id.spinner1);
        add=findViewById(R.id.create);
        Mydate=findViewById(R.id.datee);
        donee=findViewById(R.id.done);

       // Intent intent =getIntent();
        // full = intent.getStringExtra("full");




        Mydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(createEvent.this, Calender.class);
                startActivityForResult(i,1);



            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
             //   start();
               // update();
                Intent i =new Intent(createEvent.this, myAuctions.class);
                i.putExtra("full", full);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==1){
            if(data.hasExtra("full")){
                Log.e("bb",data.getStringExtra("full"));
                full = data.getStringExtra("full");
                donee.setText("Your auction will be held on "+ full);

            }

        }else{
            Toast.makeText(createEvent.this, "error"+ full,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void createEvent(){
         myname=name.getText().toString();
         mydesc=desc.getText().toString();
         mycat=cat.getText().toString();
         myprice=price.getText().toString();
         mytime=time.getText().toString();
         String mydate=donee.getText().toString();

     /*   int h= Integer.parseInt(mytime);
        int m= h*60;
        int s= m*60;
        int ms=s*1000;

        num = (long)ms;
        */





        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Auctions");
        String auctionId =ref.push().getKey();

        HashMap<String, Object> map = new HashMap<>();
        map.put("AuctionId", auctionId);
        map.put("name", myname);
        map.put("description", mydesc);
        map.put("category", mycat);
        map.put("price", myprice);
        map.put("time", mytime);
        map.put("date", full);
        map.put("Publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

        ref.child(auctionId).setValue(map);

    }

  /*  public void start(){
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
*/
   /* public void update(){
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



        cat.setText(timetext);
    }*/
}
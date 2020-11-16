package com.example.signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class createEvent extends AppCompatActivity {
    private ImageView imageAdded;
    private Uri imageUri;
    private String imageUrl;

    private EditText name,desc,cat,price;
    Button add;
    private CountDownTimer timer;
    TextView donee;
    Button Mydate;
    private long left;
    long num;
    String full;
    String myname, mycat,mydesc,myprice,mytime;
EditText start,end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
start=findViewById(R.id.start);
        end=findViewById(R.id.end);
        imageAdded = findViewById(R.id.image_added);

        name= findViewById(R.id.name);
        desc=findViewById(R.id.color);
        cat=findViewById(R.id.category);
        price=findViewById(R.id.price);
        add=findViewById(R.id.create);
        Mydate=findViewById(R.id.datee);
        donee=findViewById(R.id.done);

       // Intent intent =getIntent();
        // full = intent.getStringExtra("full");


        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2:00"));
        String dateString = sdf.format(date);

// textView is the TextView view that should display it
        start.setText(dateString);
        end.setText(dateString);

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

            }
        });
        CropImage.activity().start(createEvent.this);
        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(createEvent.this, CategoryActivity.class);
                startActivityForResult(i, 2);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            imageAdded.setImageURI(imageUri);
        }
        else
        if(resultCode==RESULT_OK && requestCode==1){
            if(data.hasExtra("full")){
                Log.e("bb",data.getStringExtra("full"));
                full = data.getStringExtra("full");
                donee.setText("Your auction will be held on "+ full);

            }

        }

        else  if (resultCode == RESULT_OK && requestCode==2) {
            if (data.hasExtra("myData1")) {
                //      Log.e("iiii", data.getStringExtra("myData1") );
                cat.setText(data.getStringExtra("myData1"));

            }
        }
        else{
            Toast.makeText(createEvent.this, "error"+ full,
                    Toast.LENGTH_SHORT).show();
        }
    }




    private void createEvent() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null) {
            final StorageReference filePath = FirebaseStorage.getInstance().getReference("Auctions").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            StorageTask uploadtask = filePath.putFile(imageUri);
            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();
                    myname=name.getText().toString();
                    mydesc=desc.getText().toString();
                    mycat=cat.getText().toString();
                    myprice=price.getText().toString();
                    String mydate=donee.getText().toString();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Auctions");
                    String auctionId =ref.push().getKey();

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("AuctionId", auctionId);
                    map.put("name", myname);
                    map.put("description", mydesc);
                    map.put("category", mycat);
                    map.put("price", myprice);
                    map.put("date", full);
                    map.put("startTime", start.getText().toString().trim());
                    map.put("endTime", end.getText().toString().trim());
                    map.put("Publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    map.put("imageurl", imageUrl);

                    ref.child(auctionId).setValue(map);



                    pd.dismiss();
                    startActivity(new Intent(createEvent.this, MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(createEvent.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No image was selected!", Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExtension(Uri uri) {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));

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
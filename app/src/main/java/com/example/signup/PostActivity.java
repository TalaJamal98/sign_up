package com.example.signup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.socialview.Hashtag;
import com.hendraanggrian.appcompat.widget.HashtagArrayAdapter;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class PostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Uri imageUri;
    private String imageUrl;

    private ImageView close;
    private ImageView imageAdded;
    private Button post;
    EditText description;
    EditText name;
    EditText size;
    EditText price;
    private Spinner spinner;
    TextView cat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
cat=findViewById(R.id.productCat);
        close = findViewById(R.id.close);
        imageAdded = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description1);
        size = findViewById(R.id.productSize);
        price = findViewById(R.id.productPrice);
        name = findViewById(R.id.productName);
spinner=findViewById(R.id.customSpinner);



        ArrayList<customItem> customList = new ArrayList<>();
        customList.add(new customItem("white"));
        customList.add(new customItem("yellow"));
        customList.add(new customItem("fuchsia"));
        customList.add(new customItem("red"));
        customList.add(new customItem("silver"));
        customList.add(new customItem("gray"));
        customList.add(new customItem("olive"));
        customList.add(new customItem("purple"));
        customList.add(new customItem("maroon"));
        customList.add(new customItem("aqua"));
        customList.add(new customItem("lime"));
        customList.add(new customItem("teal"));
        customList.add(new customItem("green"));
        customList.add(new customItem("blue"));
        customList.add(new customItem("navy"));
        customList.add(new customItem("black"));



        CustomAdapter customAdapter = new CustomAdapter(this, customList);

        if (spinner != null) {
            spinner.setAdapter(customAdapter);
            spinner.setOnItemSelectedListener(this);
        }


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        CropImage.activity().start(PostActivity.this);
        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PostActivity.this, CategoryActivity.class);
                startActivityForResult(i, 1);

            }
        });

    }





    private void upload() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null) {
            final StorageReference filePath = FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

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

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                    String postId = ref.push().getKey();

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("postid", postId);
                    map.put("imageurl", imageUrl);
                    map.put("description", description.getText().toString());
                    map.put("name", name.getText().toString());
                    map.put("size", size.getText().toString());
                    map.put("price", price.getText().toString());

                    map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    map.put("category", cat.getText().toString());
                    map.put("color", spinner.getSelectedItem().toString());
map.put("time",getDate());
                    ref.child(postId).setValue(map);


                    pd.dismiss();
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No image was selected!", Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExtension(Uri uri) {

        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            imageAdded.setImageURI(imageUri);
        }
      else  if (resultCode == RESULT_OK && requestCode==1) {
            if (data.hasExtra("myData1")) {
          //      Log.e("iiii", data.getStringExtra("myData1") );
                cat.setText(data.getStringExtra("myData1"));

            }
        }


        else {
            // Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this, MainActivity.class));
            finish();

        }


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        customItem items = (customItem) adapterView.getSelectedItem();
     //   Toast.makeText(this, items.getSpinnerText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private String getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
return formattedDate;
    }
    public  String getDateCurrentTimeZone() {

        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        java.util.Date currenTimeZone = new java.util.Date((long) 1379487711 * 1000);
        return sdf.format(currenTimeZone);
    }






}

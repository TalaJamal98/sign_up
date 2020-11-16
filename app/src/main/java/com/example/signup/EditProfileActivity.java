package com.example.signup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.example.signup.Model.User;
import com.jgabrielfreitas.core.BlurImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView close;

    private CircleImageView imageProfile;
    private CircleImageView edit;

    private TextView back;
    private CircleImageView changePhoto;
    private TextView city;
    private TextView username;
    private TextView bio;
    private TextView birth;
    private TextView phone;
    private TextView gender;
    private TextView pass;

    private FirebaseUser fUser;

    private Uri mImageUri;
    private StorageTask uploadTask;
    private StorageReference storageRef;
    //  @Bind(R.id.bluring_image) BlurImageView blurImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        close = findViewById(R.id.close);
        imageProfile = findViewById(R.id.profile_image);
        back = findViewById(R.id.back);
        changePhoto = findViewById(R.id.camera_icon);
        city = findViewById(R.id.ed_city);
        username = findViewById(R.id.ed_name);
        bio = findViewById(R.id.ed_about);
        phone = findViewById(R.id.ed_phone);
        birth = findViewById(R.id.ed_birth);
        gender = findViewById(R.id.ed_gender);
        pass = findViewById(R.id.ed_pass);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference().child("Uploads");

        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                bio.setText(user.getAboutme());

                gender.setText(user.getGender());
                birth.setText(user.getBirthdate());
                pass.setText(user.getPassword());
                phone.setText(user.getPhone());
                city.setText(user.getCity());


                if (user.getProfilepic()==null || user.getProfilepic().equals("")) {
                    // Log.e("err",user.getProfile_pic());

                    imageProfile.setImageResource(R.mipmap.ic_launcher);


                }


                else {
                    Picasso.get().load(user.getProfilepic()).into(imageProfile);
                    // Picasso.get().load(user.getProfilepic()).into(blurImageView);
                    //   blurImageView.setBlur(20);


                    //  blurImageView.setBlur(20);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(EditProfileActivity.this);
            }
        });

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(EditProfileActivity.this);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditNameActivity.class));

            }
        });
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditCityActivity.class));

            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditPasswordActivity.class));

            }
        });

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditGenderActivity.class));

            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditPhoneActivity.class));

            }
        });
        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, EditDateActivity.class));

            }
        });

        bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, AboutMeActivity.class));

            }
        });
    }

    /* private void updateProfile() {
         HashMap<String, Object> map = new HashMap<>();
         map.put("username", username.getText().toString());
         map.put("bio", bio.getText().toString());
         FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).updateChildren(map);
     }
 */
    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (mImageUri != null) {
            final StorageReference fileRef = storageRef.child(System.currentTimeMillis() + ".jpeg");

            uploadTask = fileRef.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return  fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String url = downloadUri.toString();

                        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).child("profilepic").setValue(url);
                        pd.dismiss();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();

            uploadImage();
        } else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }
}
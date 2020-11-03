package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.signup.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AboutMeActivity extends AppCompatActivity {
    private EditText f;
    private Button save;
    ImageView back1;

    private FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        back1=findViewById(R.id.back);

        f=findViewById(R.id.first);
        save=findViewById(R.id.save);
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                f.setText(user.getAboutme());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAbout();

                finish();
            }
        });




    }

    private void updateAbout() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Saving..");
        pd.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("aboutme", f.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).updateChildren(map);
        pd.dismiss();
    }
}
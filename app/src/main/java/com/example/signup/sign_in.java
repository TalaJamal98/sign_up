package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class sign_in extends AppCompatActivity {
TextView signInText;
EditText emailLogin,passwordLogin;
Button LogBtn;
ProgressDialog pd;
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signInText=findViewById(R.id.sign_in_text);
        emailLogin=findViewById(R.id.emailLog);
        passwordLogin=findViewById(R.id.passwordLog);
        LogBtn=findViewById(R.id.loginBtn);
        mAuth = FirebaseAuth.getInstance();

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(sign_in.this, sign_up.class);
                startActivity(k);
                finish();
            }
        });

        LogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

    }



    private void LoginUser() {
        pd=new ProgressDialog(sign_in.this);
        pd.setMessage("Please wait..");
        pd.show();


        final String email = emailLogin.getText().toString().trim();
        final String password = passwordLogin.getText().toString();


        android.util.Log.v("success", "in finction btn");
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(sign_in.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = mAuth.getCurrentUser();
                String userId = user.getUid();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
mDatabase.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        pd.dismiss();
        Log.v("complete","yes yes");
        Intent intent=new Intent(sign_in.this,sign_up.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
pd.dismiss();
    }
});
            }
        });
    }

                }



package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    boolean checker;
    private TextView reset;

    private void emailCheck(){

        FirebaseUser user = mAuth.getCurrentUser();
        checker= user.isEmailVerified();
        if(checker){
            Intent intent = new Intent(sign_in.this , MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();


        }else{
            Toast.makeText(sign_in.this, "Please validate your email", Toast.LENGTH_SHORT).show();
            pd.dismiss();
            mAuth.signOut();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signInText=findViewById(R.id.sign_in_text);
        emailLogin=findViewById(R.id.emailLog);
        passwordLogin=findViewById(R.id.passwordLog);
        LogBtn=findViewById(R.id.loginBtn);
        mAuth = FirebaseAuth.getInstance();
        reset= findViewById(R.id.reset);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(sign_in.this, resetPass.class);
                startActivity(k);
                finish();
            }
        });

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
                String txt_email = emailLogin.getText().toString();
                String txt_password = passwordLogin.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(sign_in.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                } else {
                    LoginUser(txt_email , txt_password);
                }
            }
        });

    }



    private void LoginUser(String email , String password) {
        pd=new ProgressDialog(sign_in.this);
        pd.setMessage("Please wait..");
        pd.show();




        android.util.Log.v("success", "in finction btn");
        mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    emailCheck();


              /*      Intent intent = new Intent(sign_in.this , MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();*/
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(sign_in.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



}
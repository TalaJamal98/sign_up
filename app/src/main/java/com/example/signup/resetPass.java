package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class resetPass extends AppCompatActivity {

    private Button send;
    private EditText emailAdd;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        mAuth=FirebaseAuth.getInstance();

        send=(Button)findViewById(R.id.sendBtn);
        emailAdd=(EditText) findViewById(R.id.myEmail);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail= emailAdd.getText().toString();
                if(TextUtils.isEmpty(userEmail)){
                    Toast.makeText(resetPass.this, "Please enter your email address to continue", Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(resetPass.this, "Please check your email. ", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(resetPass.this, sign_in.class);
                                startActivity(i);
                            }else{
                                String error= task.getException().getMessage();
                                Toast.makeText(resetPass.this, "error: "+error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }
}
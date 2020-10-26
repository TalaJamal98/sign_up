package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class sign_up extends AppCompatActivity {
    EditText emailEt, passEt, confirm_pass, first_name, second_name;
    Button register_btn;
    FirebaseAuth mAuth;
    ProgressDialog pd;
    private RadioGroup radioGenderGroup;
    private RadioButton radioGenderButton;
    private DatabaseReference mDatabase;
    TextView signUpText;
    DatePicker datePicker;
    SimpleDateFormat dateformat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        datePicker = findViewById(R.id.birthdate1);
        emailEt = findViewById(R.id.reg_email);
        passEt = findViewById(R.id.password);
        confirm_pass = findViewById(R.id.confirm_password);
        first_name = findViewById(R.id.first_name);
        second_name = findViewById(R.id.second_name);
        register_btn = findViewById(R.id.reg_btn);
        mAuth = FirebaseAuth.getInstance();
        radioGenderGroup = findViewById(R.id.radioGender);
        signUpText = findViewById(R.id.sign_up_text);
         dateformat = new SimpleDateFormat("yyyy-MM-dd");

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(sign_up.this, sign_in.class);
                startActivity(k);
                finish();
            }
        });

        register_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("success", "in finction btn");
                registerUser();
            }
        });


    }

    private void registerUser() {
pd=new ProgressDialog(sign_up.this);
pd.setMessage("Please wait..");
pd.show();
        int selectedId = radioGenderGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioGenderButton = (RadioButton) findViewById(selectedId);

        final String email = emailEt.getText().toString();
        final String password = passEt.getText().toString();
        final String first_nam = first_name.getText().toString();
        final String sec_nam = second_name.getText().toString();
        final String confirm_password = confirm_pass.getText().toString();
        final String Gender = radioGenderButton.getText().toString();
        final String dateFormat = dateformat.format(new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()));

        Log.v("success", "in finction btn");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", userId);
                            map.put("firstname", first_name.getText().toString());
                            map.put("secondname", sec_nam);
                            map.put("username", first_name.getText().toString()+" "+second_name.getText().toString());
                            map.put("email", email);
                            map.put("password", password);
                            map.put("aboutme", "");
                            map.put("birthdate", dateFormat);
                            map.put("gender", Gender);

                            map.put("profilepic", "https://firebasestorage.googleapis.com/v0/b/bib-bayitback.appspot.com/o/653528.jpg?alt=media&token=990070e6-d08f-4946-b8b3-d1af59796e5a");
                            mDatabase.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(sign_up.this , MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("fail", "err", e);
                                        }
                                    });
                            //  updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            pd.dismiss();
                            Log.w("fail", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(sign_up.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }


                    }
                });
    }
}
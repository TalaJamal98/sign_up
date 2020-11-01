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
import android.util.Patterns;
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
import java.util.regex.Pattern;

public class sign_up extends AppCompatActivity {
    EditText emailEt, passEt, confirm_pass, first_name, second_name;
    Button register_btn;
    FirebaseAuth mAuth;
    ProgressDialog pd;
    private RadioGroup radioGenderGroup;
    RadioButton radioGenderButton;
    private DatabaseReference mDatabase;
    TextView signUpText;
    DatePicker datePicker;
    SimpleDateFormat dateformat;
    String email;
    String password;
    String radiovalue;
    String sec_nam;
    int f1,f2,f3,f4,f5;
    int flag;


    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter


                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    private void   sendToLogIn(){
        Intent i = new Intent(sign_up.this,sign_in.class);
        startActivity(i);
        finish();
    }


    private void sendLink(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(sign_up.this, "Registration is successful, check your email for verification. ",
                                Toast.LENGTH_SHORT).show();
                        sendToLogIn();
                        flag=1;

                    }else{
                        String error=task.getException().getMessage();
                        Toast.makeText(sign_up.this, "error: "+error,
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }

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



        email = emailEt.getText().toString().trim();
        if (email.isEmpty()) {
            f1=1;
            emailEt.setError("Field can't be empty");

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.setError("Please enter a valid email address");

            f1=1;

        } else {
            emailEt.setError(null);
            f1=0;

        }

        String first_nam = first_name.getText().toString().trim();
        if (first_nam.isEmpty()) {
            first_name.setError("Field can't be empty");
            first_name.setBackgroundResource(R.drawable.wrong);
            f2=1;

        } else if (first_nam.length() > 15) {
            first_name.setError("Username too long");
            first_name.setBackgroundResource(R.drawable.wrong);
            f2=1;

        } else {
            first_name.setError(null);
            f2=0;

        }

        password = passEt.getText().toString();
        if (password.isEmpty()) {
            passEt.setError("Field can't be empty");
            passEt.setBackgroundResource(R.drawable.wrong);
            f3=1;

        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            passEt.setError("Password too weak");
            passEt.setBackgroundResource(R.drawable.wrong);
            f3=1;

        } else {
            passEt.setError(null);
            f3=0;

        }


        sec_nam =  second_name.getText().toString().trim();
        if (sec_nam.isEmpty()) {
            second_name.setError("Field can't be empty");
            second_name.setBackgroundResource(R.drawable.wrong);
            f4=1;

        } else if (sec_nam.length() > 15) {
            second_name.setError("Username too long");
            second_name.setBackgroundResource(R.drawable.wrong);
            f4=1;

        } else {
            second_name.setError(null);
            f4=0;

        }

        String confirm_password =  confirm_pass.getText().toString();
        if (confirm_password.isEmpty()) {
            confirm_pass.setError("Field can't be empty");
            confirm_pass.setBackgroundResource(R.drawable.wrong);

            f5=1;

        } else if (!(confirm_password.compareTo(password)==0)) {
            confirm_pass.setError("Password doesn't match");
            confirm_pass.setBackgroundResource(R.drawable.wrong);
            f5=1;

        } else {
            confirm_pass.setError(null);
            f5=0;

        }



        // final String email = emailEt.getText().toString();
        // final String password = passEt.getText().toString();
        // final String first_nam = first_name.getText().toString();
        // final String sec_nam = second_name.getText().toString();
        // final String confirm_password = confirm_pass.getText().toString();
        final String Gender = radioGenderButton.getText().toString();
        final String dateFormat = dateformat.format(new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()));

        if(f1==0 && f2==0 && f3==0 && f4==0 && f5==0) {

            Log.v("success", "in finction btn");
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                pd.dismiss();
                                sendLink();
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("success", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();



                                String userId = user.getUid();
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("id", userId);
                                map.put("firstname", first_name.getText().toString());
                                map.put("secondname", sec_nam);
                                map.put("email", email);
                                map.put("password", password);
                                map.put("aboutme", "");
                                map.put("birthdate", dateFormat);
                                map.put("gender", Gender);
                                map.put("username", first_name.getText().toString()+" "+second_name.getText().toString());
                                map.put("city", "");
                                map.put("phone", "");

                                map.put("profilepic", "");
                                mDatabase.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Write was successful!
                                        // ...
                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("fail", "err", e);
                                            }
                                        });
                                //  updateUI(user);


                            }
                            else {
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
        else {
            pd.dismiss();
            Toast.makeText(sign_up.this, "Sign up failed, Please check your information.",
                    Toast.LENGTH_SHORT).show();

        }
    }
}
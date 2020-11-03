package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.HashMap;
import java.util.regex.Pattern;

public class EditPasswordActivity extends AppCompatActivity {

    private EditText pass1;
    private EditText pass2;
    private EditText conf;
    private Button save;
    ImageView back1;

    private FirebaseUser fUser;

    int f1,f2,f3,f4,f5;
    int flag;

    User user;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter


                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        back1=findViewById(R.id.back);

        pass1=findViewById(R.id.pass1);
        pass2=findViewById(R.id.pass);
        conf=findViewById(R.id.confirm);
        save=findViewById(R.id.save);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p1=pass1.getText().toString();
                String con=conf.getText().toString();

                String p2=pass2.getText().toString();
                if (p2.isEmpty()) {
                    pass2.setError("Field can't be empty");
                    pass2.setBackgroundResource(R.drawable.wrong);
                    f3=1;

                }
                else
                    if (!PASSWORD_PATTERN.matcher(p2).matches()) {
                        pass2.setError("Password must contain at least 4 characters.1 capital letter, 1 small letter, 1 digit and no white spaces");
                        pass2.setBackgroundResource(R.drawable.wrong);
                        f3=1;

                    }
                    else {
                        pass2.setError(null);
                        f3=0;

                    }

                if (p1.isEmpty()) {
                    pass1.setError("Field can't be empty");
                    pass1.setBackgroundResource(R.drawable.wrong);
                    f4=1;

                } else if (!(p1.compareTo(user.getPassword())==0)) {
                    pass1.setError("Not match the current password");
                    pass1.setBackgroundResource(R.drawable.wrong);
                    f4=1;

                } else {
                    pass1.setError(null);
                    f4=0;

                }

                if (con.isEmpty()) {
                    conf.setError("Field can't be empty");
                    conf.setBackgroundResource(R.drawable.wrong);

                    f5=1;

                } else if (!(con.compareTo(p2)==0)) {
                    conf.setError("Password doesn't match");
                    conf.setBackgroundResource(R.drawable.wrong);
                    f5=1;

                } else {
                    conf.setError(null);
                    f5=0;

                }

                if( f3==0 && f4==0 && f5==0) {
                    updatePass();

                    finish();
                }
            }

        });





    }

    private void updatePass() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Saving..");
        pd.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("password", pass2.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).updateChildren(map);
        pd.dismiss();
    }
}
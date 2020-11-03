package com.example.signup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.signup.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

public class EditDateActivity extends AppCompatActivity {
    ImageView back1;
    private Button save;
    private FirebaseUser fUser;
private DatePicker datePicker;
    int   day;
    int   month;
    int   year;
    String formatedDate;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_date);

        back1=findViewById(R.id.back);
        save=findViewById(R.id.save);
datePicker=findViewById(R.id.birthdate1);




        fUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

               String[] split = user.getBirthdate().split("-");
                int day = Integer.valueOf(split[0]);
                int month = Integer.valueOf(split[1]);
                int year = Integer.valueOf(split[2]);
                datePicker.init(year, month-1, day, null);
                /*Calendar c = Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(user.getBirthdate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/

                //   f.setText(PhoneNumberUtils.formatNumber(user.getBirthdate()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();

                finish();
            }
        });




    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateDate() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Saving..");
        pd.show();
        HashMap<String, Object> map = new HashMap<>();
        day  = datePicker.getDayOfMonth();
        month= datePicker.getMonth();
        year = datePicker.getYear();
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        formatedDate = sdf.format(c.getTime());

        map.put("birthdate",formatedDate);

        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).updateChildren(map);
        pd.dismiss();
    }
}
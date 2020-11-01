package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.signup.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EditGenderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    String gender;
    private Button save;
    ArrayList<customItem> customList;
    private FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gender);
        save = findViewById(R.id.save);

        spinner = findViewById(R.id.customSpinner);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        customList = new ArrayList<>();
        customList.add(new customItem("Select one"));
        customList.add(new customItem("Male"));
        customList.add(new customItem("Female"));
        spinner.setSelection(0, false);

        CustomAdapter customAdapter = new CustomAdapter(this, customList);

        if (spinner != null) {
            spinner.setAdapter(customAdapter);
            spinner.setOnItemSelectedListener(this);
        }

        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                for (int position = 0; position < customList.size(); position++) {
                    if (customList.get(position).getSpinnerText() .equals(user.getGender()) ) {
                        spinner.setSelection(position);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
updateGender();
                Log.e("ttttttttttttt", gender);
                finish();
            }
        });
        spinner.setOnItemSelectedListener(this);



    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item_position = String.valueOf(position);
        int itemposition = Integer.parseInt(item_position);
        gender = String.valueOf(spinner.getAdapter().getItem(itemposition));
        gender=customList.get(position).getSpinnerText();
        Log.e("selected position", "" + itemposition);
        Log.e("selected Text", gender);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateGender() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Saving..");
        pd.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("gender", gender);

        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).updateChildren(map);
        pd.dismiss();
    }
}


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
import android.widget.ImageView;
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

public class EditCityActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    String city;
    private Button save;
    ArrayList<customItem> customList;
    private FirebaseUser fUser;
    ImageView back1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_city);
        save = findViewById(R.id.save);

        spinner = findViewById(R.id.customSpinner);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        back1=findViewById(R.id.back);

        customList = new ArrayList<>();
        customList.add(new customItem("Select one"));
        customList.add(new customItem("Jerusalem"));
        customList.add(new customItem("Nablus"));
        customList.add(new customItem("Tulkarem"));
        customList.add(new customItem("Bethlehem"));
        customList.add(new customItem("Hebron"));
        customList.add(new customItem("Ramallah"));
        customList.add(new customItem("Jericho"));
        customList.add(new customItem("Al-Bireh"));
        customList.add(new customItem("Qalqilya"));

        customList.add(new customItem("Salfit"));
        customList.add(new customItem("Tubas"));



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
int flag=-1;
                for (int position = 0; position < customList.size(); position++) {
                    if (customList.get(position).getSpinnerText() .equals(user.getCity()) ) {
                        spinner.setSelection(position);
flag=1;
                    }

                }
                if(flag==-1)
                    spinner.setSelection(0);

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
                updateCity();
                Log.e("ttttttttttttt",city);
                finish();
            }
        });
        spinner.setOnItemSelectedListener(this);



    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item_position = String.valueOf(position);
        int itemposition = Integer.parseInt(item_position);
        city= String.valueOf(spinner.getAdapter().getItem(itemposition));
        city=customList.get(position).getSpinnerText();
        Log.e("selected position", "" + itemposition);
        Log.e("selected Text", city);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateCity() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Saving..");
        pd.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("city", city);

        FirebaseDatabase.getInstance().getReference().child("users").child(fUser.getUid()).updateChildren(map);
        pd.dismiss();
    }
}

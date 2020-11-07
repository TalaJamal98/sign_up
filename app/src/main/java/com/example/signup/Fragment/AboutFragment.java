package com.example.signup.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.signup.Model.User;
import com.example.signup.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class AboutFragment extends Fragment {

    private String profileId;
    private TextView city;
    private TextView email;
    private TextView phone;
    private TextView bio;
    private TextView birth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        Bundle bundle = getArguments();
        profileId =bundle.getString("profileid");
city=view.findViewById(R.id.city);

        email=view.findViewById(R.id.email);
        phone=view.findViewById(R.id.phoneNum);
        bio=view.findViewById(R.id.about);
        birth=view.findViewById(R.id.birth);

        userInfo();


        return view;
    }

    private void userInfo() {

        FirebaseDatabase.getInstance().getReference().child("users").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                city.setText(user.getCity());
                bio.setText(user.getAboutme());
                phone.setText(user.getPhone());
                email.setText(user.getEmail());
                birth.setText(user.getBirthdate());

                // username.setText(user.getUsername());
                // bio.setText(user.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
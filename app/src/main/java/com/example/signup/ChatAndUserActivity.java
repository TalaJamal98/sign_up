package com.example.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;

import com.example.signup.Fragment.ChatFragment;
import com.example.signup.Fragment.UsersFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ChatAndUserActivity extends AppCompatActivity {
    TabLayout tabLayout ;
    ViewPager2 viewPager2 ;
    DatabaseReference reference ;
    FirebaseUser firebaseUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_and_user);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewpagerid);
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#f2c40d"));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        viewPager2.setAdapter(new Customadapter(this));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position)
                {
                    case 0:
                        tab.setText("Chats");
                        tab.setIcon(R.drawable.chattt);
                        break;
                    case 1:
                        tab.setText("Users");
                        tab.setIcon(R.drawable.users);
                        break;

                }
            }
        }
        );
        tabLayoutMediator.attach();

    }






    public class Customadapter extends FragmentStateAdapter {

        public Customadapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position)
            {
                case 0:
                    return new ChatFragment();
                case 1:
                    return new UsersFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }


}






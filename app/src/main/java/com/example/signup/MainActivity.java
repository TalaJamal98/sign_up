package com.example.signup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signup.Fragment.FilterFragment;
import com.example.signup.Fragment.HomeFragment;
import com.example.signup.Fragment.NotificationsFragment;
import com.example.signup.Fragment.ProfileFragment;
import com.example.signup.Fragment.SearchFragment;
import com.example.signup.Model.Notification;
import com.example.signup.Model.User;
import com.example.signup.adapter.Myauctions;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {
    BadgeDrawable badgeDrawable;
    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;
    NavigationView nav_view;
int count;
    private DrawerLayout dl;
    private ActionBarDrawerToggle adt;
FirebaseUser fuser;
CircleImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
fuser=FirebaseAuth.getInstance().getCurrentUser();
        dl=(DrawerLayout)findViewById(R.id.drawer);
        adt= new ActionBarDrawerToggle(this, dl, R.string.open,R.string.close);
        adt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(adt);
        adt.syncState();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        badgeDrawable=bottomNavigationView.getOrCreateBadge(R.id.nav_heart);
        final int[] flag = {0};







        FirebaseDatabase.getInstance().getReference().child("Notifications").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    flag[0]=0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Notification n=snapshot.getValue(Notification.class);
                        if(n.getSeen().equals("no")){
                            Log.e("co", "increase");
                            flag[0]++;
                            Log.e("conter", String.valueOf(flag[0]));

                        }
                        // notificationList.add(n);
                    }
                    if(flag[0]>0) {
                        badgeDrawable.setVisible(true);

                        badgeDrawable.setNumber(flag[0]);
                    }
                    else {
                        badgeDrawable.setVisible(false);

                    }
                    Log.e("conter1", String.valueOf(flag[0]));

                    // Collections.reverse(notificationList);
                    //  notificationAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        nav_view = (NavigationView)findViewById(R.id.nav);
        View header =
                nav_view.inflateHeaderView(R.layout.navigation_header);
        TextView email = (TextView) header.findViewById(R.id.email);
        TextView name = (TextView) header.findViewById(R.id.name);

        image=header.findViewById(R.id.image);


        FirebaseDatabase.getInstance().getReference().child("users").child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getProfilepic()==null || user.getProfilepic().equals("")) {
                    // Log.e("err",user.getProfile_pic());

                    image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getProfilepic()).into(image);

                }
                name.setText(user.getUsername());
                email.setText(user.getEmail());
                // username.setText(user.getUsername());
                // bio.setText(user.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_home :
                        selectorFragment = new HomeFragment();
                        break;

                    case R.id.nav_search :
                        selectorFragment = new SearchFragment();
                        break;

                    case R.id.nav_add :
                        selectorFragment = null;
                        Intent i=new Intent(MainActivity.this,PostActivity.class);
                        i.putExtra("flag",1);
                        startActivity(i);
                        break;

                    case R.id.nav_heart :
                        selectorFragment = new NotificationsFragment();
                        break;

                    case R.id.nav_profile :
                        Bundle bundle = new Bundle();
                        bundle.putString("profileid",fuser.getUid() );
// set Fragmentclass Arguments
                        ProfileFragment fragobj = new ProfileFragment();
                        fragobj.setArguments(bundle);
                        selectorFragment = fragobj;
                        break;
                }


                if (selectorFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectorFragment).commit();
                }

                return  true;

            }
        });


        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home :
                        selectorFragment = new HomeFragment();
                     //   Toast.makeText(MainActivity.this, "HOME", Toast.LENGTH_SHORT);

                        break;

                    case R.id.logout :
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, sign_in.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                        break;

                    case R.id.map :
                        selectorFragment = null;
                        Intent i=new Intent(MainActivity.this,MapActivity.class);
                        startActivity(i);
                        break;

                        case R.id.filter :
                            selectorFragment = new FilterFragment();
                            break;
                            //   Toast.makeText(MainActivity.this, "HOME", Toast.LENGTH_SHORT);

                    case  R.id.profile:
                        Bundle bundle = new Bundle();
                        bundle.putString("profileid",fuser.getUid() );
// set Fragmentclass Arguments
                        ProfileFragment fragobj = new ProfileFragment();
                        fragobj.setArguments(bundle);
                        selectorFragment = fragobj;
break;

                    case  R.id.events:
                        startActivity(new Intent(MainActivity.this, myAuctions.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));



                }
            if (selectorFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , selectorFragment).commit();
                }
            return true;
            }
        });



        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String profileId = intent.getString("publisherId");

            getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", profileId).apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new HomeFragment()).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return adt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

  /* public void notNum(){
       badgeDrawable=bottomNavigationView.getOrCreateBadge(R.id.nav_heart);
     // int count1=readNotifications();

       if(readNotifications()>0){
           Log.e("co", String.valueOf(readNotifications()));
           badgeDrawable.setVisible(true);
           badgeDrawable.setNumber(readNotifications());
       }
       else{
           Log.e("co", String.valueOf(readNotifications()));
           badgeDrawable.setVisible(true);
           badgeDrawable.setNumber(readNotifications());
       }

   }
*/
  /*  public int readNotifications() {
        FirebaseDatabase.getInstance().getReference().child("Notifications").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notification n=snapshot.getValue(Notification.class);
                    if(n.getSeen().equals("no")){
                        Log.e("co", "increase");
                        count++;
                        Log.e("conter", String.valueOf(count));

                    }
                   // notificationList.add(n);
                }

               // Collections.reverse(notificationList);
              //  notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        Log.e("con", String.valueOf(count));
        return count;

    }*/

}
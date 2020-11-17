package com.example.signup.adapter;

import java.text.ParseException;
import java.util.Date;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signup.CommentActivity;
import com.example.signup.EditPostActivity;
import com.example.signup.FollowerActivity;
import com.example.signup.Fragment.PostDetailesFragment;
import com.example.signup.Fragment.ProfileFragment;
import com.example.signup.Model.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;
//import com.example.signup.CommentActivity;
/*
import com.rishav.firebasedemo.FollowersActivity;
import com.rishav.firebasedemo.Fragments.PostDetailFragment;
import com.rishav.firebasedemo.Fragments.ProfileFragment;
*/
import com.example.signup.Model.Post;
import com.example.signup.Model.User;
import com.example.signup.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Viewholder> {

    private Context mContext;
    private List<Post> mPosts;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, int position) {

        final Post post = mPosts.get(position);
        Picasso.get().load(post.getImageurl()).into(holder.postImage);
        // holder.description.setText(post.getDescription());
if(!post.getPublisher().equals(firebaseUser.getUid()))
    holder.detail.setVisibility(View.GONE);
else
    holder.detail.setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference().child("users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    Log.e("err", user.getId());

                    if (user.getProfilepic() == null || user.getProfilepic().equals("")) {
                        // Log.e("err",user.getProfile_pic());

                        holder.imageProfile.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Picasso.get().load(user.getProfilepic()).into(holder.imageProfile);

                    }

                    holder.username.setText(user.getUsername());
                    holder.category.setText(post.getCategory());
                    holder.price.setText(post.getPrice());
                    holder.prod_name.setText(post.getName());
                    holder.post_time.setText(convert(post.getTime()));


                } else {
                    Log.v("no", post.getPublisher());

                    Log.v("no", "no user");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        isLiked(post.getPostid(), holder.like);
        noOfLikes(post.getPostid(), holder.noOfLikes);
        getComments(post.getPostid(), holder.noOfComments);
        // isSaved(post.getPostid(), holder.save);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);

                    addNotification(post.getPostid(), post.getPublisher());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes")
                            .child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("profileid",post.getPublisher() );
// set Fragmentclass Arguments
                ProfileFragment fragobj = new ProfileFragment();
                fragobj.setArguments(bundle);
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , fragobj).commit();

/*
                Intent intent = new Intent(mContext, ProfileFragment.class);
                intent.putExtra("publisherId", post.getPublisher());
                mContext.startActivity(intent);*/
            }
        });

        holder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("profileid",post.getPublisher() );
// set Fragmentclass Arguments
                ProfileFragment fragobj = new ProfileFragment();
                fragobj.setArguments(bundle);
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , fragobj).commit();


            }
        });

     /*   holder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key", "value");
                Fragment fc= new ProfileFragment();
                fc.setArguments(bundle);


                Intent intent = new Intent(mContext, ProfileFragment.class);
                intent.putExtra("publisherId", post.getPublisher());
                mContext.startActivity(intent);
            }
        });*/

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", post.getPostid());
                intent.putExtra("authorId", post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.noOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ninino", post.getPublisher()+"hi");

                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", post.getPostid());
                intent.putExtra("authorId", post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.comLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", post.getPostid());
                intent.putExtra("authorId", post.getPublisher());
                mContext.startActivity(intent);
            }
        });

   /*     holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.save.getTag().equals("save")) {
                    FirebaseDatabase.getInstance().getReference().child("Saves")
                            .child(firebaseUser.getUid()).child(post.getPostid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Saves")
                            .child(firebaseUser.getUid()).child(post.getPostid()).removeValue();
                }
            }
        });*/

       /* holder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                        .edit().putString("profileId", post.getPublisher()).apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment()).commit();
            }
        });*/

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                        .edit().putString("profileId", post.getPublisher()).apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment()).commit();
            }
        });


        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("postid", post.getPostid()).apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new PostDetailesFragment()).commit();
            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().putString("postid", post.getPostid()).apply();

                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new PostDetailesFragment()).commit();
            }
        });

        holder.noOfLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FollowerActivity.class);
                intent.putExtra("id", post.getPublisher());
                intent.putExtra("title", "likes");
                intent.putExtra("postid", post.getPostid());
                Log.e("hi", post.getPostid());


                mContext.startActivity(intent);
            }
        });

        holder.likeT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FollowerActivity.class);
                intent.putExtra("id", post.getPublisher());
                intent.putExtra("title", "likes");
                intent.putExtra("postid", post.getPostid());

                //Log.e("hi", post.getPostid());

                mContext.startActivity(intent);
            }
        });


        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(mContext,v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edit:

                                Intent intent = new Intent(mContext, EditPostActivity.class);
                                //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                                intent.putExtra("postid",post.getPostid());
                               mContext.startActivity(intent);

                                return true;
                            case R.id.delete:
                                FirebaseDatabase.getInstance().getReference("Posts").child(post.getPostid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                            Toast.makeText(mContext,"Deleted!",Toast.LENGTH_SHORT).show();
                                    }
                                });

                                FirebaseDatabase.getInstance().getReference("Comments").child(post.getPostid()).removeValue();
                                FirebaseDatabase.getInstance().getReference("Likes").child(post.getPostid()).removeValue();


                                DatabaseReference refer = FirebaseDatabase.getInstance().getReference().child("Notifications");
                                refer.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot requestSnapshot: dataSnapshot.getChildren()) {
                                             Log.e("n", "in");
                                            Log.e("is Post",requestSnapshot.getKey());

                                            FirebaseDatabase.getInstance().getReference().child("Notifications").child(requestSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    for (DataSnapshot requestSnapshot1: snapshot.getChildren()) {
                                                        Notification n = requestSnapshot1.getValue(Notification.class);
                                                        Log.e("notify", requestSnapshot1 + "");
                                                        if (n.isPost() == true && n.getPostid().equals(post.getPostid())) {
                                                            // Log.e("notify", n.getNotid()+"" );

                                                            FirebaseDatabase.getInstance().getReference().child("Notifications").child(requestSnapshot.getKey()).child(n.getNotid()).removeValue();
                                                        }
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        throw databaseError.toException(); // don't ignore errors
                                    }
                                });

                                return true;

                            default:
                                return false;
                        }

                    }
                });
                popupMenu.inflate(R.menu.post_menu);
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        public CircleImageView imageProfile;
        public ImageView postImage;
        public ImageView like;
        public ImageView comment;
        public ImageView save;
        public LinearLayout more;
        public TextView category;
        public TextView prod_name;
        public TextView post_time;

        public TextView price;
        public TextView username;
        public TextView noOfLikes;
        public TextView author;
        public TextView noOfComments;
        public TextView likeT;
public ImageView detail;
        public LinearLayout comLay;

        SocialTextView description;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.profile_image);
            postImage = itemView.findViewById(R.id.post_image);
            post_time = itemView.findViewById(R.id.post_time);
detail=itemView.findViewById(R.id.post_details);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            comLay = itemView.findViewById(R.id.comments);
            likeT = itemView.findViewById(R.id.liketext);
            //  save = itemView.findViewById(R.id.save);
/*
            more = itemView.findViewById(R.id.more);
*/
            more = itemView.findViewById(R.id.more);

            username = itemView.findViewById(R.id.post_username);
            price = itemView.findViewById(R.id.post_prod_price);
            prod_name = itemView.findViewById(R.id.post_prod_name);
            category = itemView.findViewById(R.id.post_category);


            noOfLikes = itemView.findViewById(R.id.post_likes_no);
            //    author = itemView.findViewById(R.id.author);
            noOfComments = itemView.findViewById(R.id.post_comments_no);

/*
            description = itemView.findViewById(R.id.description);
*/

        }
    }

   /* private void isSaved (final String postId, final ImageView image) {
        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postId).exists()) {
                    image.setImageResource(R.drawable.ic_save_black);
                    image.setTag("saved");
                } else {
                    image.setImageResource(R.drawable.ic_save);
                    image.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    private void isLiked(String postId, final ImageView imageView) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.heart);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.heart1);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void noOfLikes(String postId, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getComments(String postId, final TextView text) {
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addNotification(String postId, String publisherId) {
        HashMap<String, Object> map = new HashMap<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notifications");
        String notId = ref.push().getKey();

        if (!publisherId.equals(firebaseUser.getUid())) {
            Log.d("fede", "" + publisherId);
            Log.d("fede", "" + firebaseUser.getUid());

            map.put("userid", firebaseUser.getUid());
            map.put("text", "liked your post.");
            map.put("postid", postId);
            map.put("isPost", true);
            map.put("Seen", "no");
            map.put("notid", notId);


            FirebaseDatabase.getInstance().getReference().child("Notifications").child(publisherId).child(notId).setValue(map);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String convert(String time) {
        final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = time;
        String niceDateStr="";
        try {
            Date date = inputFormat.parse(dateStr);
            niceDateStr = (String) DateUtils.getRelativeTimeSpanString(date.getTime() , Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return niceDateStr;
    }





  /*  public static String getTimeAgo(long time, Context ctx) {
         final int SECOND_MILLIS = 1000;
       final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
      final int DAY_MILLIS = 24 * HOUR_MILLIS;
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = getCurrentTime(ctx);
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }*/

}
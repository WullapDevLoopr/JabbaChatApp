package org.codekidd.jabbachatapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUsersList;

    private DatabaseReference mUsersDatabase; // set the database reference to "users" obj in order to retrive all users values


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mToolbar = (Toolbar) findViewById(R.id.users_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set the database reference to "users" obj in order to retrive all users values
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users"); // now include it in the FirebaseRecyclerAdapter


        mUsersList = (RecyclerView)findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));
//        recycler view is ready now we need a module class for the recycler



    }

//    create an onStart Method


    @Override
    protected void onStart() {
        super.onStart();

//        create a Firebase Recycler Adapter and set it to mUsersList
//        create a viewholder first

//        pass in the module class, viewHolder
        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,
                R.layout.users_single_layout,
                UsersViewHolder.class,
                mUsersDatabase
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {

                viewHolder.setDisplayName(model.getName());
                viewHolder.setUserStatus(model.getStatus());
                viewHolder.setUserImage(model.getThumb_image(), getApplicationContext());

//                mView defines the whole view ie the complete constraint layout of each recycler view
                //              therefore i can set if user should click on UserImage an it will do something else
//                for now im setting an onClick on the whole view
//say i want to get the key of a user

                final String user_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent profileIntent = new Intent(UsersActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("user_id",user_id);
                        startActivity(profileIntent);

                    }
                });


            }

        };

//        set adapter to recycler view
        mUsersList.setAdapter(firebaseRecyclerAdapter);
    }

//    ViewHolder
    public static class UsersViewHolder extends RecyclerView.ViewHolder {
//alt + enter == create constructor
//    we need a view needed by the firebase adapter
    View mView;

    public UsersViewHolder(View itemView) {
        super(itemView);
        mView = itemView;


    }

    public void setDisplayName(String name){
//        now set the values to the textview in Users activity
        TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
        userNameView.setText(name);
    }


    public void setUserStatus(String status) {
        TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
        userStatusView.setText(status);
    }

    public void setUserImage(String thumb_image, Context ctx) {

        CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
//        here i use picasso to load the image in the userImageView
        Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.settings_img).into(userImageView);

    }
}

}

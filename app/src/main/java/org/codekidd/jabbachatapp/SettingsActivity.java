package org.codekidd.jabbachatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingsActivity extends AppCompatActivity {


    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

//    layout
    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;
    private TextView mBio;
    private TextView mHobbies;
    private TextView mDisplayEmail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mDisplayImage = (CircleImageView)findViewById(R.id.settings_image);
        mName = (TextView)findViewById(R.id.settings_display_name);
        mStatus = (TextView)findViewById(R.id.settings_status);
        mBio = (TextView)findViewById(R.id.settings_display_bio);
        mHobbies = (TextView)findViewById(R.id.settings_diplay_hobbies);
        mDisplayEmail = (TextView)findViewById(R.id.display_email);



//        =========here ill retrieve the data i set to the database into this activity to display============
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
//          get user id
        String current_uid = mCurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
//        now to retriev all the values of the object above add value event listener
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                this method handles any changes made and ...retrives the data
                Toast.makeText(SettingsActivity.this,dataSnapshot.toString(),Toast.LENGTH_LONG).show();

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String bio = dataSnapshot.child("bio").getValue().toString();
                String hobbies = dataSnapshot.child("hobbies").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();

//                now is to set the values to our layout in settings activity ===change the values
                mName.setText(name);
                mStatus.setText(status);
                mBio.setText(bio);
                mHobbies.setText(hobbies);
                mDisplayEmail.setText(email);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}

package org.codekidd.jabbachatapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {


    private ImageView mProfileImage;
    private TextView mProfileName;
    private TextView mProfileStatus;
    private TextView mProfileBio;
    private TextView mProfileHobby;
    private TextView mProfileEmail;
    private TextView mProfileFriendsCount;
    private Button mProfileSendReqBtn;
    private Button mDeclineBtn;

    //    create a database reference
    private DatabaseReference mUsersDatabase;
    private ProgressDialog mProgressDialog;

    private DatabaseReference mFriendReqDatabase;

    private DatabaseReference mFriendDatabase;

    private DatabaseReference mNotificationDatabase;

    private FirebaseUser mCurrent_user;

    private String mCurrent_state;

    private android.support.v7.widget.Toolbar mToolbar;

    private DatabaseReference mRootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(mToolbar);
//        return user's name instead of user's id as seen below ====>
        getSupportActionBar().setTitle("Profile");

        getSupportActionBar().setIcon(R.drawable.blog_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        final String user_id = getIntent().getStringExtra("user_id");

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
//create new object in database caled Friend_req
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();


        mProfileImage = (ImageView) findViewById(R.id.profile_image);
        mProfileName = (TextView) findViewById(R.id.profile_displayName);
        mProfileStatus = (TextView) findViewById(R.id.profile_status);
        mProfileBio = (TextView) findViewById(R.id.profile_bio);
        mProfileHobby = (TextView) findViewById(R.id.profile_hobby);
        mProfileEmail = (TextView) findViewById(R.id.profile_email);
        mProfileFriendsCount = (TextView) findViewById(R.id.profile_totalFriends);
        mProfileSendReqBtn = (Button) findViewById(R.id.profile_send_req_btn);
        mDeclineBtn = (Button) findViewById(R.id.profile_decline_btn);

        mCurrent_state = "not_friends";


        mDeclineBtn.setVisibility(View.INVISIBLE);
        mDeclineBtn.setEnabled(false);



        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Loading User Data");
        mProgressDialog.setMessage("Please wait while we load the user data.");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();






        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String display_name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String bio = dataSnapshot.child("bio").getValue().toString();
                String hobby = dataSnapshot.child("hobbies").getValue().toString();

                mProfileName.setText(display_name);
                mProfileStatus.setText(status);
                mProfileBio.setText(bio);
                mProfileHobby.setText(hobby);
                mProfileEmail.setText(email);

//               using picasso to load image to profile activity
                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.profile_img_jabbachat).into(mProfileImage);


                if (mCurrent_user.getUid().equals(user_id)) {

                    mDeclineBtn.setEnabled(false);
                    mDeclineBtn.setVisibility(View.INVISIBLE);

                    mProfileSendReqBtn.setEnabled(false);
                    mProfileSendReqBtn.setVisibility(View.INVISIBLE);
                }


//               the query below will detect and change the send request button accordingly

//-........................=========FRIENDS LIST /  REQUEST FEATURES===============.............

                mFriendReqDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(user_id)) {
//                           get the type of request
                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                            if (req_type.equals("received")) {

//                               meaning request has been sent to user

//                                        i want to change the current_state
                                mCurrent_state = "req_received";
                                mProfileSendReqBtn.setText("Accept Friend Request");

                                mDeclineBtn.setVisibility(View.VISIBLE);
                                mDeclineBtn.setEnabled(true);

                            } else if (req_type.equals("sent")) {

                                mCurrent_state = "req_sent";
                                mProfileSendReqBtn.setText("Cancel Friend Request");

                                mDeclineBtn.setVisibility(View.INVISIBLE);
                                mDeclineBtn.setEnabled(false);

                            }

                            mProgressDialog.dismiss();

                        } else {

//                           check if user is already a friend or not if yes display unfriend on btn
                            mFriendDatabase.child(mCurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.hasChild(user_id)) {

                                        mCurrent_state = "friends";
                                        mProfileSendReqBtn.setText("Unfriend this User");

                                        mDeclineBtn.setVisibility(View.INVISIBLE);
                                        mDeclineBtn.setEnabled(false);
                                    }

                                    mProgressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    mProgressDialog.dismiss();
                                }
                            });
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {


            }


        });


        mProfileSendReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProfileSendReqBtn.setEnabled(false);


//               ........... ==============NOT FRIENDS STATE====================.........

                if (mCurrent_state.equals("not_friends")) {

                    DatabaseReference newNotificationref = mRootRef.child("notifications").child(user_id).push();
                    String newNotificationId = newNotificationref.getKey();

                    HashMap<String, String> notificationData = new HashMap<>();
                    notificationData.put("from", mCurrent_user.getUid());
                    notificationData.put("type", "request");


                    Map requestMap = new HashMap();
                    requestMap.put("Friend_req/" + mCurrent_user.getUid() + "/" + user_id + "/request_type", "sent");
                    requestMap.put("Friend_req/" + user_id + "/" + mCurrent_user.getUid() + "/request_type", "received");
                    requestMap.put("notifications/" + user_id + "/" + newNotificationId, notificationData);


                    mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                            if (databaseError != null) {

                                Toast.makeText(ProfileActivity.this, "there were some error in sending request", Toast.LENGTH_LONG).show();

                            } else {

                                mCurrent_state = "req_sent";
                                mProfileSendReqBtn.setText("Cancel Friend Request");

                            }

                            mProfileSendReqBtn.setEnabled(true);


                        }
                    });


                }

                //               ........... ==============CANCEL FRIENDS REQUEST STATE====================.........

                if (mCurrent_state.equals("req_sent")) {
//                    delete the query

                    mFriendReqDatabase.child(mCurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                            it is appropriate to use onComplete listener instead onSuccess

                            mFriendReqDatabase.child(user_id).child(mCurrent_user.getUid()).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            mProfileSendReqBtn.setEnabled(true);
                                            mCurrent_state = "not_friends";
                                            mProfileSendReqBtn.setText("Send Friend Request");

                                            mDeclineBtn.setVisibility(View.INVISIBLE);
                                            mDeclineBtn.setEnabled(false);

                                        }
                                    });

                        }


                    });



                }


//                =============............REQUEST RECEIVED STATE..............==================

                if (mCurrent_state.equals("req_received")) {

                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                    Map friendsMap = new HashMap();
                    friendsMap.put("Friends/" + mCurrent_user.getUid() + "/" + user_id + "/date", currentDate);
                    friendsMap.put("Friends/" + user_id + "/"  + mCurrent_user.getUid() + "/date", currentDate);


                    friendsMap.put("Friend_req/" + mCurrent_user.getUid() + "/" + user_id, null);
                    friendsMap.put("Friend_req/" + user_id + "/" + mCurrent_user.getUid(), null);



                    mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                mProfileSendReqBtn.setEnabled(true);
//                                        i want to change the current_state
                                mCurrent_state = "friends";
                                mProfileSendReqBtn.setText("Unfriend this User");

                                mDeclineBtn.setVisibility(View.INVISIBLE);
                                mDeclineBtn.setEnabled(false);

                            } else {
                                String error = databaseError.getMessage();
                                Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

//from heree cut out code....

                }

//                ========UNFRIEND====================
                if (mCurrent_state.equals("friends")) {

                    Map unfriendMap = new HashMap();
                    unfriendMap.put("Friends/" + mCurrent_user.getUid() + "/" + user_id, null);
                    unfriendMap.put("Friends/" + user_id + "/" + mCurrent_user.getUid(), null);


                    mRootRef.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {


//                                        i want to change the current_state
                                mCurrent_state = "not_friends";
                                mProfileSendReqBtn.setText("Send Friend Request");

                                mDeclineBtn.setVisibility(View.INVISIBLE);
                                mDeclineBtn.setEnabled(false);

                            } else {
                                String error = databaseError.getMessage();
                                Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                            }

                            mProfileSendReqBtn.setEnabled(true);
                        }
                    });

                }


            }
        });

    }

}
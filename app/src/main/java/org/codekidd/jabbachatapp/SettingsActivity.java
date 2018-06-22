package org.codekidd.jabbachatapp;

//libraries imported
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;


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

    private Button mStatusBtn;
    private FloatingActionButton mImageBtn;
    private Button mBioBtn;
    private Button mHobbiesBtn;

    private static final int GALLERY_PICK = 1;
//creating storage reference
    private StorageReference mImageStorage;

    private ProgressDialog mProgresDialog;



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

//        ========BTNS========
        mImageBtn = (FloatingActionButton)findViewById(R.id.settings_image_btn);
        mStatusBtn = (Button)findViewById(R.id.settings_status_btn);
        mBioBtn = (Button)findViewById(R.id.settings_bio_btn);
        mHobbiesBtn = (Button)findViewById(R.id.settings_hobby_btn);

        mImageStorage = FirebaseStorage.getInstance().getReference();



//        =========here ill retrieve the data i set to the database into this activity to display============
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
//          get user id
        String current_uid = mCurrentUser.getUid();


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
//        offline capability
        mUserDatabase.keepSynced(true);

//        now to retriev all the values of the object above add value event listener
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                this method handles any changes made and ...retrives the data
//                Toast.makeText(SettingsActivity.this,dataSnapshot.toString(),Toast.LENGTH_LONG).show();

                String name = dataSnapshot.child("name").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
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

                if (!image.equals("default")) {

//                this allows picasso to retrive the image and display it in the cirleimageview
                   // Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.settings_img).into(mDisplayImage);

                    Picasso.with(SettingsActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.settings_img).into(mDisplayImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
//                            im made image final
                            Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.settings_img).into(mDisplayImage);

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        ===========onClickListener============
        mStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status_value = mStatus.getText().toString();

                Intent status_intent = new Intent(SettingsActivity.this,StatusActivity.class);
//                im sending bac the status value to the status activity
                status_intent.putExtra("status_value",status_value);
                startActivity(status_intent);
            }
        });


        mHobbiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hobbies_value = mHobbies.getText().toString();
                Intent hobbies_intent = new Intent(SettingsActivity.this,HobbiesActivity.class);
                hobbies_intent.putExtra("hobbies_value",hobbies_value);
                startActivity(hobbies_intent);
            }
        });

        mBioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bio_value = mBio.getText().toString();
                Intent bio_intent = new Intent(SettingsActivity.this,BioActivity.class);
                bio_intent.putExtra("bio_value",bio_value);
                startActivity(bio_intent);
            }
        });

//        =================UPLOADING PROFILE IMAGE ====================

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setting intent to pick an image from users gallery
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),GALLERY_PICK);


//                ok that is done! now is to crop the image and im using a library from GITHUB
                // start picker to get image for cropping and then use the image in cropping activity
//                CropImage.activity()
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .start(SettingsActivity.this);


            }
        });



    }
//this for startActivityForResult...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            mProgresDialog = new ProgressDialog(SettingsActivity.this);
            mProgresDialog.setTitle("Uploading Image...");
            mProgresDialog.setMessage("please wait while your profile image uploads");
            mProgresDialog.setCanceledOnTouchOutside(false);
            mProgresDialog.show();


            Uri imageUri = data.getData();



            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .setMinCropWindowSize(500,500)
                    .start(this);

//            Toast.makeText(SettingsActivity.this,imageUri, Toast.LENGTH_LONG).show();
        } else {
//            error error check: if profile image selection is discarded progress dialo still appears fix it.

        }


//            now is to get the result from the crop activity and store image in firebase storage

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
//compressing thumb image
                File thumb_filePath = new File(resultUri.getPath());

                String current_user_id = mCurrentUser.getUid();


                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxHeight(200)
                            .setMaxWidth(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }


//                    converts data to byte form allowing upload to database
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();



                StorageReference filepath = mImageStorage.child("profile_images").child(current_user_id+".jpg");
                final StorageReference thumb_filepath = mImageStorage.child("profile_images").child("thumbs").child(current_user_id +".jpg");


                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SettingsActivity.this,"uploading...", Toast.LENGTH_LONG).show();
//                            we wamt to download the image and store it in the realtime database image object

                            final String download_url = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();

                                    if (thumb_task.isSuccessful()){

                                        Map update_hashMap = new HashMap<>();
                                        update_hashMap.put("image",download_url);
                                        update_hashMap.put("thumb_image", thumb_downloadUrl);

                                        //                            now we have stored it the string download_url next is to store in database


                                        mUserDatabase.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    mProgresDialog.dismiss();
                                                    Toast.makeText(SettingsActivity.this,"success uploading image...", Toast.LENGTH_LONG).show();


                                                } else {
                                                    Toast.makeText(SettingsActivity.this,"error... check connection and try again", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(SettingsActivity.this,"error handling in uploading thumbnail", Toast.LENGTH_LONG).show();
                                        mProgresDialog.dismiss();

                                    }
                                }
                            });





                        }else {
                            Toast.makeText(SettingsActivity.this,"error uploading image...check connection and try again", Toast.LENGTH_LONG).show();

                            mProgresDialog.dismiss();
                        }
                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }
//    generate random string to store image names in storage
    public static String random(){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96)+32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


}

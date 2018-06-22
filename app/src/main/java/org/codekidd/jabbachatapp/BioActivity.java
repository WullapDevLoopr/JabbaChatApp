package org.codekidd.jabbachatapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BioActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout mBio;
    private FloatingActionButton mSaveBiobtn;


    private DatabaseReference mBioDatabase;
    private FirebaseUser mCurrentUser;

    private ProgressDialog mProgress;

    private TextView counterTextview;
    private EditText editCounter_bio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mBioDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mToolbar = (Toolbar)findViewById(R.id.bio_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Bio");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //to retrive the status value from seetingsActivity and store in string status_value
        String bio_value = getIntent().getStringExtra("bio_value");



        mBio = (TextInputLayout)findViewById(R.id.bio_input);
        mSaveBiobtn = (FloatingActionButton) findViewById(R.id.bio_save_btn);

        mBio.getEditText().setText(bio_value);


        //        counter
        counterTextview = (TextView)findViewById(R.id.bio_counter);
        editCounter_bio = (EditText)findViewById(R.id.counter_bio);

        editCounter_bio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = editCounter_bio.getText().toString();
                int symbols = text.length();
                counterTextview.setText("max " + symbols + "/40");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mSaveBiobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //                progress
                mProgress  = new ProgressDialog(BioActivity.this);
                mProgress.setTitle("Updating...");
                mProgress.setMessage("Please wait while we save changes");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();

                String bio = mBio.getEditText().getText().toString();

                mBioDatabase.child("bio").setValue(bio).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(),"Success! Press back button to go back",Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(),"Oops something went wrong, Try again",Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        });


    }
}

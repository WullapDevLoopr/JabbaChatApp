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

public class HobbiesActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextInputLayout mHobbies;
    private FloatingActionButton mSaveHobbtn;

    private DatabaseReference mHobbiesDatabase;
    private FirebaseUser mCurrentUser;

    private ProgressDialog mProgress;

    private TextView counterTextview;
    private EditText editCounter_hobbies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobbies);


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mHobbiesDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mToolbar = (Toolbar)findViewById(R.id.hobbies_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Hobbies");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //to retrive the status value from seetingsActivity and store in string status_value
        String hobbies_value = getIntent().getStringExtra("hobbies_value");

        mHobbies = (TextInputLayout)findViewById(R.id.hobbies_input);
        mSaveHobbtn = (FloatingActionButton) findViewById(R.id.hobbies_save_btn);

        mHobbies.getEditText().setText(hobbies_value);


        //        counter
        counterTextview = (TextView)findViewById(R.id.hobbies_counter);
        editCounter_hobbies = (EditText)findViewById(R.id.counter_hobbies);

        editCounter_hobbies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = editCounter_hobbies.getText().toString();
                int symbols = text.length();
                counterTextview.setText("max " + symbols + "/90");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mSaveHobbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //                progress
                mProgress  = new ProgressDialog(HobbiesActivity.this);
                mProgress.setTitle("Updating...");
                mProgress.setMessage("Please wait while we save changes");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();

                String hobbies = mHobbies.getEditText().getText().toString();

                mHobbiesDatabase.child("hobbies").setValue(hobbies).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(),"Success! Press back button to go back",Toast.LENGTH_LONG).show();


                        } else {
                            Toast.makeText(getApplicationContext(),"Oops Something went wrong! try again",Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        });



    }
}

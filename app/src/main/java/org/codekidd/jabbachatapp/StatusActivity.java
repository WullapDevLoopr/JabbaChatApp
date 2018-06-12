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


public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout mStatus;
    private FloatingActionButton mSavebtn;


    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;

    private ProgressDialog mProgress;
    private TextView counterTextview;
    private EditText editCounter_status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
//        to get a single value NO NEED FOR HASHMAP here


        mToolbar = (Toolbar)findViewById(R.id.status_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//to retrive the status value from seetingsActivity and store in string status_value
        String status_value = getIntent().getStringExtra("status_value");



        mStatus = (TextInputLayout)findViewById(R.id.status_input);
        mSavebtn = (FloatingActionButton) findViewById(R.id.status_save_btn);

        mStatus.getEditText().setText(status_value);

//        counter
        counterTextview = (TextView)findViewById(R.id.status_counter);
        editCounter_status = (EditText)findViewById(R.id.counter_status);

        editCounter_status.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = editCounter_status.getText().toString();
                int symbols = text.length();
                counterTextview.setText("max " + symbols + "/90");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        mSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progress
                mProgress  = new ProgressDialog(StatusActivity.this);
                mProgress.setTitle("Updating...");
                mProgress.setMessage("Please wait while we save changes");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();

                String status = mStatus.getEditText().getText().toString();
//                now i have gottn the value next is to store it to firebase set the status
                mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(),"Success! Press back button to go back",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(),"Oops Something went wrong, please retry",Toast.LENGTH_LONG).show();

                        }
                    }
                });

            }
        });





    }
}

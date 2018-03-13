package org.codekidd.jabbachatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterSignUpActivity extends AppCompatActivity {
    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mCreateBtn;
    private FirebaseAuth mAuth;
    private android.support.v7.widget.Toolbar mToolbar;

    private ProgressDialog mRegProgress;






//written by Nyako Epahraim Alhamdu 12th March 2018
//    GITHUB @DcodeKidd
// START onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_sign_up);

        mToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegProgress = new ProgressDialog(this);



        mAuth = FirebaseAuth.getInstance();

        mDisplayName = (TextInputLayout)findViewById(R.id.reg_display_name);
        mEmail = (TextInputLayout)findViewById(R.id.reg_email);
        mPassword = (TextInputLayout)findViewById(R.id.reg_password);
        mCreateBtn = (Button)findViewById(R.id.reg_create_btn);

//        setting listener for mCreateBtn
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                when users click the button I want to retrieve the data from the TextInputLayout fields and register to the firebase database
                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();


//                whats next is to process the registration Signup new user
                register_user(display_name,email,password);

            }
        });



    } //END onCreate


//Register new user method START register_user()
    private void register_user(String display_name, String email, String password) {

        //               TASKS TO CHECK IF EMAIL, PASSWORD FIELDS ARE EMPTY? ALSO IF PASSWORD LENGTH IS TOO SHORT ETC


//        check for empty User name Field
        if (display_name.isEmpty()){
            mDisplayName.setError("Username is required!");
            mDisplayName.requestFocus();
            return;
        }

//        check for empty Email Field!
        if (email.isEmpty()){
            mEmail.setError("Email is required!");
            mEmail.requestFocus();
            return;
        }

//        check for Valid Email Address
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Please enter a valid Email Address");
            mEmail.requestFocus();
            return;
        }

//        check for empty Password Field
        if (password.isEmpty()){
            mPassword.setError("Password is required!");
            mPassword.requestFocus();
            return;
        }
//        set notice for minimum password to be 6 as in firebase auth documentation
        if (password.length()< 6){
            mPassword.setError("Password too short! Minimum length of password is 6");
            mPassword.requestFocus();
            return;
        }



//                set progress dialog

        mRegProgress.setTitle("Signing you Up!");
        mRegProgress.setMessage("Please wait... setting up your Account");
        mRegProgress.setCanceledOnTouchOutside(false);
        mRegProgress.show();


        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {


//               check if task is successful
               if (task.isSuccessful()){
                   mRegProgress.dismiss();
//                   meaning user is registered
                  finish(); // this is to avoid user going back to registration page by pressing back btn
                   Toast.makeText(RegisterSignUpActivity.this,"Account Sign Up is SUCCESSFUL!", Toast.LENGTH_LONG).show();
                   Intent mainIntent = new Intent(RegisterSignUpActivity.this,MainActivity.class);
                   startActivity(mainIntent);
                   finish();

               } else {
                   mRegProgress.hide();
//                   Error user is not registered successfully
                   Toast.makeText(RegisterSignUpActivity.this,"Error! Please check and try again", Toast.LENGTH_LONG).show();

               }
           }
       });

    } //END register_user()



}

package org.codekidd.jabbachatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {


    private Toolbar mToolbar;

    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;
    private Button mLogin_btn;
    private ProgressDialog mLogProgress;
    private FirebaseAuth mAuth;

//    ctreatign database reference
    private DatabaseReference mUserDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//instantiate

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar)findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLogProgress = new ProgressDialog(this);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mLoginEmail = (TextInputLayout)findViewById(R.id.login_email);
        mLoginPassword = (TextInputLayout)findViewById(R.id.login_password);
        mLogin_btn = (Button)findViewById(R.id.login_btn);

        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                now i am going to retrieve the value of the user Email and Password

                String email = mLoginEmail.getEditText().getText().toString();
                String password = mLoginPassword.getEditText().getText().toString();
//check if the fields are empty

                //                whats next is to process the Signin user
                loginUser(email,password);

            }
        });


    }

//    already have account? sign up
    public void onSignUpPressed(View view){
        Intent signupRedirectIntent = new Intent(LoginActivity.this,RegisterSignUpActivity.class);
        startActivity(signupRedirectIntent);
        finish();
    }

//Login User Method
    private void loginUser(String email, String password) {

        //               TASKS TO CHECK IF EMAIL, PASSWORD FIELDS ARE EMPTY? ALSO IF PASSWORD LENGTH IS TOO SHORT ETC


//        check for empty Email Field!
        if (email.isEmpty()){
            mLoginEmail.setError("Email is required!");
            mLoginEmail.requestFocus();
            return;
        }

//        check for Valid Email Address
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mLoginEmail.setError("Please enter a valid Email Address");
            mLoginEmail.requestFocus();
            return;
        }

//        check for empty Password Field
        if (password.isEmpty()){
            mLoginPassword.setError("Password is required!");
            mLoginPassword.requestFocus();
            return;
        }
//        set notice for minimum password to be 6 as in firebase auth documentation
        if (password.length()< 6){
            mLoginPassword.setError("Password too short! Minimum length of password is 6");
            mLoginPassword.requestFocus();
            return;
        }



//                set progress dialog

        mLogProgress.setTitle("Authenticating user");
        mLogProgress.setMessage("Please wait... signing in your Account");
        mLogProgress.setCanceledOnTouchOutside(false);
        mLogProgress.show();


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

//                check if user is logged in
                if (task.isSuccessful()){

                    mLogProgress.dismiss();

//                    store users token id ===used later for firebase notifications Functions


                    String current_user_id = mAuth.getCurrentUser().getUid();

                    String deviceToken = FirebaseInstanceId.getInstance().getToken();

//                    storing user and token
                    mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(LoginActivity.this,"Log in was Successful!",Toast.LENGTH_LONG).show();
//                    send user to mainActivity by Intents
                            Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
//                    below allows users to be able to go back to your main phone
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        }
                    });





                }else {
//                    user is not successfully logged in
                    mLogProgress.hide();
                    Toast.makeText(LoginActivity.this,"Oops! something went wrong, check and try again",Toast.LENGTH_LONG).show();



                }
            }
        });



    }

}

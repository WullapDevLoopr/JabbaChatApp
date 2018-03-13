package org.codekidd.jabbachatapp;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {
//create instances
    private Button mRegBtn;
    private Button mLoginBtn;


//    START onCreate()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mRegBtn = (Button)findViewById(R.id.start_reg_btn);
        //now when user clicks on the button it should do something by adding onClickListener
        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we want to send user to RegisterSignUpActivity
                //Intents enables for such functionality
                Intent reg_intent = new Intent(StartActivity.this,RegisterSignUpActivity.class);
                startActivity(reg_intent);

            }
        });

        mLoginBtn = (Button)findViewById(R.id.start_login_btn);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log_intent = new Intent(StartActivity.this,LoginActivity.class);
                startActivity(log_intent);
            }
        });

    } //END onCreate()


}

package org.codekidd.jabbachatapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

//    declare instances
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ViewPager mViewPager;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;


//    START onCreate()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        initializing the auth instance
        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("JabbaChat App");

        //tabs
        mViewPager = (ViewPager)findViewById(R.id.main_tabPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout = (TabLayout)findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);


//        setting the tab icon
        mTabLayout.getTabAt(0).setIcon(R.drawable.blog_icon);
        mTabLayout.getTabAt(1).setIcon(R.drawable.chat_icon);
        mTabLayout.getTabAt(2).setIcon(R.drawable.friends_icon);
        mTabLayout.getTabAt(3).setIcon(R.drawable.request_icon);


//i want to change and updaate color of icons as it slides here
        mTabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        mTabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#cccccc"), PorterDuff.Mode.SRC_IN);
        mTabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#cccccc"), PorterDuff.Mode.SRC_IN);
        mTabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#cccccc"), PorterDuff.Mode.SRC_IN);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#cccccc"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//the end of icons




    } //END onCreate()

//    create onStart method used to check continuously
    public void onStart() {
        super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        the above is the key steps to check of user is logged in or not. By getting user and storing in variable "currentUser"
//        Again i can check if user is not logged in by using if-else statms as seen below
        if (currentUser == null){

            sendToStart();


        }
} //END onStart()



//send user back to StartActivity
    private void sendToStart() {
        Intent StartIntent = new Intent(MainActivity.this,StartActivity.class);
        startActivity(StartIntent);
        finish();  //to avoid user going back by pressing back button
    }

    //    Editing the menuBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

//        to make menubar work is to inflaite it

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }
//    create options selected


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     super.onOptionsItemSelected(item);

     if (item.getItemId() == R.id.main_logout_btn){
         FirebaseAuth.getInstance().signOut();
         sendToStart();
     }
     if (item.getItemId() == R.id.main_settings_btn){
         Intent SettingsIntent = new Intent(MainActivity.this,SettingsActivity.class);
         startActivity(SettingsIntent);

     }
     if (item.getItemId() == R.id.main_all_btn){
         Intent usersIntent = new Intent(MainActivity.this,UsersActivity.class);
         startActivity(usersIntent);
     }

     return true;
    }
}

package org.codekidd.jabbachatapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeSliderActivity extends AppCompatActivity {

    private ViewPager slideViewPager;
    private LinearLayout layoutDots;
    private TextView [] dotstv;
    private int [] slidesLayouts;
    private Button btnSkip, btnContinue;

    private SliderPagerAdapter sliderPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!isFirstTimeStartApp()){
            startMainActivity();
            finish();
        }

        setStatusBarTransparent();
//all above the setContentView
        setContentView(R.layout.activity_welcome_slider);
        slideViewPager = (ViewPager)findViewById(R.id.slide_view_page);
        layoutDots = findViewById(R.id.dotsLayout);
        btnSkip = findViewById(R.id.button_skip);
        btnContinue = findViewById(R.id.button_next);

//starts main activity
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startMainActivity();
            }
        });

//opens next intro slider
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPage= slideViewPager.getCurrentItem()+1;
                if (currentPage < slidesLayouts.length){
//                    move to first page
                    slideViewPager.setCurrentItem(currentPage);

                } else {
                    startMainActivity();
                }
            }
        });

        slidesLayouts = new int[]{R.layout.slider_one,R.layout.slider_two,R.layout.slider_three};
        sliderPagerAdapter = new SliderPagerAdapter(slidesLayouts, getApplicationContext());
        slideViewPager.setAdapter(sliderPagerAdapter);

        slideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == slidesLayouts.length-1){
//                    on the last page slider
                    btnContinue.setText("Get Started!");
                    btnSkip.setVisibility(View.GONE);

                } else {
                    btnContinue.setText("Continue");
                    btnSkip.setVisibility(View.VISIBLE);

                }

                setDotStatus(position);
            }



            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setDotStatus(0);

    }
//    end onClick

    private boolean isFirstTimeStartApp(){
        SharedPreferences ref = getApplicationContext().getSharedPreferences("IntroJabbaChatApp", Context.MODE_PRIVATE);
        return ref.getBoolean("FirstTimeStartFlag", true);

    }

    private void setFirstTimeStartStatus(boolean boolstart){
        SharedPreferences ref = getApplicationContext().getSharedPreferences("IntroJabbaChatApp", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = ref.edit();
        editor.putBoolean("FirstTimeStartFlag", boolstart);
        editor.commit();
    }


    private void setDotStatus(int page){
        layoutDots.removeAllViews();
        dotstv = new TextView[slidesLayouts.length];

        for (int i = 0; i < dotstv.length; i++){
            dotstv[i] = new TextView(this);
            dotstv[i].setText(Html.fromHtml("&#8226;"));
            dotstv[i].setTextSize(30);
            dotstv[i].setTextColor(Color.parseColor("#a9b4bb"));
            layoutDots.addView(dotstv[i]);

        }

//        now to set the current active dot
        if (dotstv.length > 0 ){
            dotstv[page].setTextColor(Color.parseColor("#ffffff"));
        }

    }
//if u dont wnrt user to go back to slider after "Get Started" change below to false!
    private void startMainActivity(){
        setFirstTimeStartStatus(true);
        startActivity(new Intent(WelcomeSliderActivity.this, MainActivity.class));
        finish();

    }

    private void setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT >= 21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window slide_window = getWindow();
            slide_window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            slide_window.setStatusBarColor(Color.TRANSPARENT);

        }

    }

}

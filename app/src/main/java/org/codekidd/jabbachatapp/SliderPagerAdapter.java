package org.codekidd.jabbachatapp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by codekid on 09/03/2018.
 */

public class SliderPagerAdapter extends PagerAdapter {
    private LayoutInflater slideInflater;
    private int [] slidesLayouts;
    private Context slideContext;


    public SliderPagerAdapter(int[] slidesLayouts, Context slideContext) {
        this.slideContext = slideContext;
        this.slidesLayouts = slidesLayouts;
    }



    @Override
    public int getCount() {

        return slidesLayouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        slideInflater = (LayoutInflater)slideContext.getSystemService(slideContext.LAYOUT_INFLATER_SERVICE);
        View slideView = slideInflater.inflate(slidesLayouts[position],container,false);
        container.addView(slideView);
        return slideView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View slideView = (View)object;
        container.removeView(slideView);

    }
}

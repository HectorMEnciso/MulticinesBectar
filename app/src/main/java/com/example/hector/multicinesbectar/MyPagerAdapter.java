package com.example.hector.multicinesbectar;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Hector on 17/06/2015.
 */
public class MyPagerAdapter extends PagerAdapter {
    private Context ctx;
    public MyPagerAdapter(Context ctx){
        this.ctx = ctx;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView tView = new TextView(ctx);
        tView.setText("Page number: " + position+1);
        tView.setTextColor(Color.RED);
        tView.setTextSize(20);
        container.addView(tView);
        return tView;
    }
    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
          }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}

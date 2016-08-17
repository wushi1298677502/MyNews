package com.example.scxh.myxlistview.MyTextFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.scxh.myxlistview.Logs;
import com.example.scxh.myxlistview.R;
import com.example.scxh.myxlistview.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class TabFragment extends Fragment implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private LinearLayout mOneTabLayout;
    private LinearLayout mTwoTabLayout;
    private LinearLayout mThreeTabLayout;
    private LinearLayout mFinance;
    private LinearLayout mScience;
    List<Fragment> list = new ArrayList<>();
    ViewPager mViewPager;
    String[] str = new String[]{
            "T1348647909107",
            "T1348648517839",
            "T1348649079062",
            "T1348648756099",
            "T1348649580692"
    };
    public TabFragment() {
        // Required empty public constructor
    }
    public static Fragment newInstance(){
        TabFragment tabFragment = new TabFragment();
        return tabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (ViewPager)getView(). findViewById(R.id.tabfragment_framlayout);
        mViewPager.setOffscreenPageLimit(2);
        mOneTabLayout = (LinearLayout)getView(). findViewById(R.id.tabfragment_one);
        mTwoTabLayout = (LinearLayout) getView().findViewById(R.id.tabfragment_two);
        mThreeTabLayout = (LinearLayout)getView(). findViewById(R.id.tabfragment_three);
        mFinance = (LinearLayout)getView(). findViewById(R.id.tabfragment_finance);
        mScience = (LinearLayout)getView(). findViewById(R.id.tabfragment_science);


        mOneTabLayout.setOnClickListener(this);
        mTwoTabLayout.setOnClickListener(this);
        mThreeTabLayout.setOnClickListener(this);
        mFinance.setOnClickListener(this);
        mScience.setOnClickListener(this);

        mOneTabLayout.setBackgroundResource(R.color.red);
        MyTextAdapter myAdapter = new MyTextAdapter(getChildFragmentManager());// TODO: 2016/8/3 实例化的参数是getSupportFragmentManager
        myAdapter.SetDatalist(str);
        mViewPager.setAdapter(myAdapter);
        mViewPager.addOnPageChangeListener(this);// TODO: 2016/8/3  FragmentPagerAdapter的监听项
        mViewPager.setPageTransformer(true,new ZoomOutPageTransformer());
    }

    @Override
    public void onClick(View view) {
        clearBackground();
        switch (view.getId()){
            case R.id.tabfragment_one:
                mViewPager.setCurrentItem(0);
                mOneTabLayout.setBackgroundResource(R.color.red);
                break;
            case R.id.tabfragment_two:
                mViewPager.setCurrentItem(1);
                mTwoTabLayout.setBackgroundResource(R.color.red);
                break;
            case R.id.tabfragment_three:
                mViewPager.setCurrentItem(2);
                mThreeTabLayout.setBackgroundResource(R.color.red);
                break;
            case R.id.tabfragment_finance:
                mViewPager.setCurrentItem(3);
                mFinance.setBackgroundResource(R.color.red);
                break;
            case R.id.tabfragment_science:
                mViewPager.setCurrentItem(4);
                mScience.setBackgroundResource(R.color.red);
                break;
        }
    }

    // TODO: 2016/8/3 每次切换的时候重置背景
    public void clearBackground(){
        Logs.e("clearBackground");
        mOneTabLayout.setBackgroundResource(R.color.red1);
        mTwoTabLayout.setBackgroundResource(R.color.red1);
        mThreeTabLayout.setBackgroundResource(R.color.red1);
        mFinance.setBackgroundResource(R.color.red1);
        mScience.setBackgroundResource(R.color.red1);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public void onPageSelected(int position) {
        clearBackground();
        switch (position){
            case 0:
                mOneTabLayout.setBackgroundResource(R.color.red);//把当前的选中项颜色加深
                break;
            case 1:
                mTwoTabLayout.setBackgroundResource(R.color.red);

                break;
            case 2:
                mThreeTabLayout.setBackgroundResource(R.color.red);
                break;
            case 3:
                mFinance.setBackgroundResource(R.color.red);
                break;
            case 4:
                mScience.setBackgroundResource(R.color.red);
                break;
        }
    }

    public void onPageScrollStateChanged(int state) {

    }

    class MyTextAdapter extends FragmentStatePagerAdapter {
        public String[] urls = new String[]{};
        public MyTextAdapter(FragmentManager fm) {
            super(fm);
        }
        public void SetDatalist(String[] urls){
            this.urls = urls;
            notifyDataSetChanged();
        }
        @Override
        public Fragment getItem(int position) {
            Logs.e("position>>>>"+position);
            return MainFragment.newInstance(str[position],position);
        }

        @Override
        public int getCount() {
            return str.length;
        }
    }
}

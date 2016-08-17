package com.example.scxh.myxlistview.MyPicFragment;


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
 * A simple {@link Fragment} subclass.
 */
public class MainTableFragment extends Fragment implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private LinearLayout selected;
    private LinearLayout funny;
    private LinearLayout beauty;
    private LinearLayout story;
    ViewPager mViewPager;
    List<Fragment> list = new ArrayList<>();
    String[] str = new String[]{
            "http://api.sina.cn/sinago/list.json?channel=hdpic_toutiao&adid=4ad30dabe134695c3b7c3a65977d7e72&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=&imei=867064013906290&uid=802909da86d9f5fc&p=",
            "http://api.sina.cn/sinago/list.json?channel=hdpic_funny&adid=4ad30dabe134695c3b7c3a65977d7e72&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=12050_0001&imei=867064013906290&uid=802909da86d9f5fc&p=",
            "http://api.sina.cn/sinago/list.json?channel=hdpic_pretty&adid=4ad30dabe134695c3b7c3a65977d7e72&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=12050_0001&imei=867064013906290&uid=802909da86d9f5fc&p=",
            "http://api.sina.cn/sinago/list.json?channel=hdpic_story&adid=4ad30dabe134695c3b7c3a65977d7e72&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=12050_0001&imei=867064013906290&uid=802909da86d9f5fc&p="
    };
    public MainTableFragment() {
        // Required empty public constructor
    }
    public static Fragment newInstance(){
        MainTableFragment mainTableFragment = new MainTableFragment();
        return  mainTableFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_table_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewPager = (ViewPager)getView(). findViewById(R.id.maintable_framlayout);
        selected = (LinearLayout)getView().findViewById(R.id.maintable_selected);
        funny =(LinearLayout)getView().findViewById(R.id.maintable_funny);
        beauty = (LinearLayout)getView().findViewById(R.id.maintable_beauty);
        story =(LinearLayout)getView().findViewById(R.id.maintable_story);

        selected.setOnClickListener(this);
        funny.setOnClickListener(this);
        beauty.setOnClickListener(this);
        story.setOnClickListener(this);



        selected.setBackgroundResource(R.color.red);
        MyPicAdapter myPicAdapter = new MyPicAdapter(getChildFragmentManager());// TODO: 2016/8/3 实例化的参数是getSupportFragmentManager
        myPicAdapter.SetDatalist(str);
        mViewPager.setAdapter(myPicAdapter);
        mViewPager.addOnPageChangeListener(this);// TODO: 2016/8/3  FragmentPagerAdapter的监听项
        mViewPager.setPageTransformer(true,new ZoomOutPageTransformer());
    }

        public void onClick(View view) {
            clearBackground();
            switch (view.getId()){
                case R.id.maintable_selected:
                    mViewPager.setCurrentItem(0);
                    selected.setBackgroundResource(R.color.red);
                    break;
                case R.id.maintable_funny:
                    mViewPager.setCurrentItem(1);
                    funny.setBackgroundResource(R.color.red);
                    break;
                case R.id.maintable_beauty:
                    mViewPager.setCurrentItem(2);
                    beauty.setBackgroundResource(R.color.red);
                    break;
                case R.id.maintable_story:
                    mViewPager.setCurrentItem(3);
                    story.setBackgroundResource(R.color.red);
                    break;

            }
        }

        // TODO: 2016/8/3 每次切换的时候重置背景
    public void clearBackground(){
        Logs.e("clearBackground");
        selected.setBackgroundResource(R.color.red1);
        funny.setBackgroundResource(R.color.red1);
        beauty.setBackgroundResource(R.color.red1);
        story.setBackgroundResource(R.color.red1);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public void onPageSelected(int position) {
        clearBackground();
        switch (position){
            case 0:
                selected.setBackgroundResource(R.color.red);//把当前的选中项颜色加深
                break;
            case 1:
                funny.setBackgroundResource(R.color.red);
                break;
            case 2:
                beauty.setBackgroundResource(R.color.red);
                break;
            case 3:
                story.setBackgroundResource(R.color.red);
                break;
        }
    }

    public void onPageScrollStateChanged(int state) {

    }

    class MyPicAdapter extends FragmentStatePagerAdapter {
        public String[] urls = new String[]{};
        public MyPicAdapter(FragmentManager fm) {
            super(fm);
        }
        public void SetDatalist(String[] urls){
            this.urls = urls;
            notifyDataSetChanged();
        }
        @Override
        public Fragment getItem(int position) {
            return SelectedFragment.newInstance(str[position],position);
        }

        @Override
        public int getCount() {
            return str.length;
        }
    }

}

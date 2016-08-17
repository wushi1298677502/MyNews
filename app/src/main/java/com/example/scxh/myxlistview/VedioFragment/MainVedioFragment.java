package com.example.scxh.myxlistview.VedioFragment;


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
public class MainVedioFragment extends Fragment  implements View.OnClickListener,ViewPager.OnPageChangeListener{


    private LinearLayout hot;
    private LinearLayout entertainment;
    private LinearLayout funny;
    private LinearLayout selected;
    int pageNo = 0; //页号 ，表示第几页,第一页从0开始
    int pageSize = 10; //页大小，显示每页多少条数据
    String news_type_id = "T1348647909107";  //新闻类型标识, 此处表示头条新闻
    String video_type_id = "V9LG4B3A0";//hot
    private  String baseUrl = "http://c.3g.163.com/nc/video/list/"+ video_type_id + "/n/" +pageNo*pageSize+ "-" +pageSize+ ".html";
//    String video_type_id = "V9LG4B3A0";//hot
//    String video_type_id = "V9LG4CHOR"//entertainment
//    String video_type_id = "V9LG4E6VR"//funny
//    String video_type_id = "00850FRB"//selected
    ViewPager mViewPager;
    List<Fragment> list = new ArrayList<>();

    String[] str = new String[]{
            "V9LG4B3A0",
            "V9LG4CHOR",
            "V9LG4E6VR",
            "00850FRB"
    };
    public MainVedioFragment() {
        // Required empty public constructor
    }
    public static Fragment newInstance(){
        MainVedioFragment mainVedioFragment = new MainVedioFragment();
        return  mainVedioFragment;
    }
//
//    http://c.3g.163.com/nc/video/list/V9LG4E6VR/n/0-10.html
//    http://c.3g.163.com/nc/video/list/V9LG4CHOR/n/0-10.html
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_vedio_layout, container, false);
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewPager = (ViewPager)getView(). findViewById(R.id.mainvedio_framlayout);
        hot = (LinearLayout)getView().findViewById(R.id.mainvedio_hot);
        entertainment =(LinearLayout)getView().findViewById(R.id.mainvedio_entertainment);
        funny = (LinearLayout)getView().findViewById(R.id.mainvedio_funny);
        selected =(LinearLayout)getView().findViewById(R.id.mainvedio_selected);

        hot.setOnClickListener(this);
        entertainment.setOnClickListener(this);
        funny.setOnClickListener(this);
        selected.setOnClickListener(this);


        hot.setBackgroundResource(R.color.red);
        MyAdapter myAdapter = new MyAdapter(getChildFragmentManager());// TODO: 2016/8/3 实例化的参数是getSupportFragmentManager
        myAdapter.SetDatalist(str);
        mViewPager.setAdapter(myAdapter);
        mViewPager.addOnPageChangeListener(this);// TODO: 2016/8/3  FragmentPagerAdapter的监听项
        mViewPager.setPageTransformer(true,new ZoomOutPageTransformer());
    }

    public void onClick(View view) {
        clearBackground();
        switch (view.getId()){
            case R.id.mainvedio_hot:
                mViewPager.setCurrentItem(0);
                hot.setBackgroundResource(R.color.red);
                break;
            case R.id.mainvedio_entertainment:
                mViewPager.setCurrentItem(1);
                entertainment.setBackgroundResource(R.color.red);
                break;
            case R.id.mainvedio_funny:
                mViewPager.setCurrentItem(2);
                funny.setBackgroundResource(R.color.red);
                break;
            case R.id.mainvedio_selected:
                mViewPager.setCurrentItem(3);
                selected.setBackgroundResource(R.color.red);
                break;

        }
    }

    // TODO: 2016/8/3 每次切换的时候重置背景
    public void clearBackground(){
        Logs.e("clearBackground");
        selected.setBackgroundResource(R.color.red1);
        funny.setBackgroundResource(R.color.red1);
        hot.setBackgroundResource(R.color.red1);
        entertainment.setBackgroundResource(R.color.red1);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public void onPageSelected(int position) {
        clearBackground();
        switch (position){
            case 0:
                hot.setBackgroundResource(R.color.red);//把当前的选中项颜色加深
                break;
            case 1:
                entertainment.setBackgroundResource(R.color.red);
                break;
            case 2:
                funny.setBackgroundResource(R.color.red);
                break;
            case 3:
                selected.setBackgroundResource(R.color.red);
                break;
        }
    }

    public void onPageScrollStateChanged(int state) {

    }

    class MyAdapter extends FragmentStatePagerAdapter {
        public String[] urls = new String[]{};
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        public void SetDatalist(String[] urls){
            this.urls = urls;
            notifyDataSetChanged();
        }
        @Override
        public Fragment getItem(int position) {
            return MainVedioChildFragment.newInstance(str[position],position);
        }

        @Override
        public int getCount() {
            return str.length;
        }
    }

}

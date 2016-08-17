package com.example.scxh.myxlistview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by scxh on 2016/8/8.
 */
public class MenuFragment extends Fragment implements View.OnClickListener{
    TextView mNewsBtn,mVedioBtn,mPictureBtn,mWeather,mMap;
    FragmentToActivity fragmentToActivity;



    public interface FragmentToActivity{
        void excute(View view);
    }
    public static Fragment newInstance(){
        MenuFragment menuFragment = new MenuFragment();
        return menuFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FragmentToActivity){
            this.fragmentToActivity = (FragmentToActivity) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentToActivity");
        }
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_menufragment_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mNewsBtn = (TextView) getView().findViewById(R.id.menufragment_newsbtn);
        mPictureBtn = (TextView) getView().findViewById(R.id.menufragment_picturebtn);
        mVedioBtn = (TextView) getView().findViewById(R.id.menufragment_vediobtn);
        mWeather = (TextView) getView().findViewById(R.id.menufragment_weatherbtn);
        mMap = (TextView) getView().findViewById(R.id.menufragment_mapbtn);
        mNewsBtn.setOnClickListener(this);
        mPictureBtn.setOnClickListener(this);
        mVedioBtn.setOnClickListener(this);
        mWeather.setOnClickListener(this);
        mMap.setOnClickListener(this);
    }
    public void onClick(View view) {
        fragmentToActivity.excute(view);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}

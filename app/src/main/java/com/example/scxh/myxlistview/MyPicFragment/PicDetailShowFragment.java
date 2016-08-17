package com.example.scxh.myxlistview.MyPicFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.scxh.myxlistview.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PicDetailShowFragment extends Fragment {

    ImageView imageView;
    public String url,mAlt,mPetcentTotal;
    public ProgressBar mProgressBar;
    private int mPageNo,mTotal;
    TextView mTextVIew,mTextViewAlt;
    public static Fragment newInstance(String str,String alt,int num,int total){
        PicDetailShowFragment myFragmentViewPagerFragment = new PicDetailShowFragment();
        Bundle bundle = new Bundle();
        bundle.putString("MESSAGE",str);
        bundle.putString("ALT",alt);
        bundle.putInt("PAGENUM",num);
        bundle.putInt("TOTAL",total);
        myFragmentViewPagerFragment.setArguments(bundle);
        return myFragmentViewPagerFragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments()==null?null:getArguments().getString("MESSAGE");
        mAlt = getArguments()==null?null:getArguments().getString("ALT");
        mPageNo = getArguments() == null ? -1 : getArguments().getInt("PAGENUM");
        mTotal = getArguments() == null ? -1 : getArguments().getInt("TOTAL");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pic_detail_show_layout, container, false);
    }
    public  void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPetcentTotal = mPageNo+"/"+mTotal;
        imageView = (ImageView) getView().findViewById(R.id.fragmentviewpager_Img);
        mProgressBar = (ProgressBar) getView().findViewById(R.id.fragmentviewpager_progressbar);
        mTextVIew = (TextView) getView().findViewById(R.id.fragmentviewpager_Tex);
        mTextViewAlt = (TextView) getView().findViewById(R.id.fragmentviewpager_content);
        mTextVIew.setText(mPetcentTotal);
        mTextViewAlt.setText(mAlt);
        mProgressBar.setVisibility(View.VISIBLE);
        Glide.with(getContext()).load(url).into(imageView);

    }


}


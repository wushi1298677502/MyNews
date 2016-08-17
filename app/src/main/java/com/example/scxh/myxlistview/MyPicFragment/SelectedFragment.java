package com.example.scxh.myxlistview.MyPicFragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.scxh.myxlistview.AlreadyDefinitUtils.ConnectionUtil;
import com.example.scxh.myxlistview.MyPicGsonJava.PicContentNews;
import com.example.scxh.myxlistview.MyPicGsonJava.Pics;
import com.example.scxh.myxlistview.MyPicGsonJava.PicsList;
import com.example.scxh.myxlistview.MyPicGsonJava.Selected;
import com.example.scxh.myxlistview.MyPicGsonJava.SelectedContent;
import com.example.scxh.myxlistview.Logs;
import com.example.scxh.myxlistview.MyActivity.PicActivity;
import com.example.scxh.myxlistview.R;
import com.example.xlistviewlibary.XListView;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedFragment extends Fragment   implements XListView.IXListViewListener,AdapterView.OnItemClickListener {

    XListView mXListView;
    private static final String SELECTED_CACHE_NAME = "com.example.scxh.myxlistview.SelectedFragment";
    String httpUrl = "http://api.sina.cn/sinago/list.json?channel=hdpic_toutiao&adid=4ad30dabe134695c3b7c3a65977d7e72&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=&imei=867064013906290&uid=802909da86d9f5fc&p=";
    private ConnectionUtil connectionUtil;
    int length;
    private MyPagerAdapter mPagerAdapter;
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");//hh 小写是十二进制，HH 大写是24进制
    Context mContext;
    public String url;
    private int mPageNo;
//    ArrayList<Pics> list = new ArrayList<>();
    public SelectedFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String str,int num) {
        SelectedFragment selectedFragment = new SelectedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("MESSAGE",str);
        bundle.putInt("NUM",num);
        selectedFragment.setArguments(bundle);
        return selectedFragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        Logs.e("onAttach>>>");
        mContext = context;
    }
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments()==null?null:getArguments().getString("MESSAGE");
        mPageNo = getArguments()==null?null:getArguments().getInt("NUM");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selected_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mXListView = (XListView) getView().findViewById(R.id.selectedFragment_xlistview);

        connectionUtil = new ConnectionUtil(getContext());
        mPagerAdapter = new MyPagerAdapter(getContext());
        getDataLists();
        mXListView.setAdapter(mPagerAdapter);

        mXListView.setXListViewListener(this);
        mXListView.setPullLoadEnable(true); //上拉加载更多开关
        mXListView.setPullRefreshEnable(true);   //下拉刷新开关
        mXListView.setOnItemClickListener(this);
    }

    public void getDataLists() {
        connectionUtil.asyncConnect(url, ConnectionUtil.Mothod.GET, new ConnectionUtil.HttpConnectionInterface() {

            public void excute(String cont) {
                if (cont == null) {
                    Toast.makeText(getContext(), "请求出错!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mContext.getSharedPreferences(SELECTED_CACHE_NAME, Context.MODE_PRIVATE).edit().putString(url, cont).commit();
                }
//                setViewpage(cont);
                setListview(cont);

            }

        });

    }

    public void setListview(String content) {
        Logs.e("content :" + content);
        mXListView.stopLoadMore();
        mXListView.stopRefresh();
        mXListView.setRefreshTime(simpleDateFormat.format(new Date(System.currentTimeMillis())));

        Gson gson = new Gson();
        Selected conten = gson.fromJson(content, Selected.class);
        SelectedContent selectedContent = conten.getData();
        ArrayList<PicContentNews> listPicsContentNews = selectedContent.getList();

        mPagerAdapter.addDataList(listPicsContentNews);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PicContentNews picContentNews = (PicContentNews) adapterView.getAdapter().getItem(i);
        String  id = picContentNews.getId();
        Pics pics = picContentNews.getPics();
        int total = pics.getTotal();
        Logs.e("id>>>>" + total);
        Bundle bundle = new Bundle();
        bundle.putInt("TOTAL", total);
        bundle.putString("ID", id);
        Intent intent = new Intent(getContext(), PicActivity.class);
        intent.putExtra("BUNDLE", bundle);
        startActivity(intent);

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }


    class MyPagerAdapter extends BaseAdapter {
        List<PicContentNews> list = new ArrayList<>();
        LayoutInflater layoutInflater;

        public MyPagerAdapter(Context context) {
            this.layoutInflater = LayoutInflater.from(context);
        }

        public void addDataList(List<PicContentNews> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getViewTypeCount() {
            return 4;
        }

        @Override
        public int getItemViewType(int position) {
            PicContentNews picContentNews = (PicContentNews) getItem(position);
            Pics pics = picContentNews.getPics();
            int num = pics.getPicTemplate();
            return num;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            int type = getItemViewType(i);
            if (type == 1) {
                return getOnePic(i, view, viewGroup);
            } else if (type == 2) {
                return getTwoPic(i, view, viewGroup);
            } else if (type == 3) {
                return getThreePic(i, view, viewGroup);
            } else if(type == 4) {
                return getFourPic(i, view, viewGroup);
            }else {
                return getOnePic(i, view, viewGroup);
            }
        }


        public View getOnePic(int i, View view, ViewGroup viewGroup) {
            HoldView holdView;
            if (view == null) {
                view = layoutInflater.inflate(R.layout.activity_picitem_layout, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.picitem_img);
                TextView title = (TextView) view.findViewById(R.id.picitem_title);


                holdView = new HoldView();
                holdView.pic = imageView;
                holdView.title = title;

                view.setTag(holdView);
            }
            holdView = (HoldView) view.getTag();
            PicContentNews picContentNews = (PicContentNews) getItem(i);
            Pics pics = picContentNews.getPics();
            ArrayList<PicsList> picsLists = pics.getList();
            PicsList one = picsLists.get(0);
            String img = one.getKpic();
            Glide.with(getContext()).load(img).into(holdView.pic);
            holdView.title.setText(one.getAlt());//对于直接从网络取数据的图片使用第三方包，还可以缓存
            return view;
        }
        public View getTwoPic(int i, View view, ViewGroup viewGroup) {
            HoldView holdView;
            if (view == null) {
                view = layoutInflater.inflate(R.layout.activity_picitemtwo_layout, null);
                ImageView imageleft = (ImageView) view.findViewById(R.id.picitemtwo_imgone);
                ImageView imageright = (ImageView) view.findViewById(R.id.picitemtwo_imgtwo);
                TextView title = (TextView) view.findViewById(R.id.picitemtwo_title);


                holdView = new HoldView();
                holdView.pic = imageleft;
                holdView.picTwo = imageright;
                holdView.title = title;
                view.setTag(holdView);
            }
            holdView = (HoldView) view.getTag();
            PicContentNews picContentNews = (PicContentNews) getItem(i);
            Pics pics = picContentNews.getPics();
            ArrayList<PicsList> picsLists = pics.getList();
            PicsList one = picsLists.get(0);
            String img = one.getKpic();
            Glide.with(getContext()).load(img).into(holdView.pic);
            PicsList two = picsLists.get(1);
            String imgtwo = two.getKpic();
            Glide.with(getContext()).load(imgtwo).into(holdView.picTwo);
            holdView.title.setText(one.getAlt());
            return view;
        }
        public View getThreePic(int i, View view, ViewGroup viewGroup) {
            HoldView holdView;
            if (view == null) {
                view = layoutInflater.inflate(R.layout.activity_picitemthree_layout, null);
                TextView title = (TextView) view.findViewById(R.id.picitemthree_title);
                ImageView imagetop = (ImageView) view.findViewById(R.id.picitemthree_imgone);
                ImageView imageleft = (ImageView) view.findViewById(R.id.picitemthree_imgtwo);
                ImageView imageright = (ImageView) view.findViewById(R.id.picitemthree_imgthree);


                holdView = new HoldView();
                holdView.pic = imagetop;
                holdView.picTwo = imageleft;
                holdView.picThree = imageright;
                holdView.title = title;

                view.setTag(holdView);
            }
            holdView = (HoldView) view.getTag();
            PicContentNews picContentNews = (PicContentNews) getItem(i);
            Pics pics = picContentNews.getPics();
            ArrayList<PicsList> picsLists = pics.getList();
            PicsList one = picsLists.get(0);
            String img = one.getKpic();
            Glide.with(getContext()).load(img).into(holdView.pic);
            PicsList two = picsLists.get(1);
            String imgtwo = two.getKpic();
            Glide.with(getContext()).load(imgtwo).into(holdView.picTwo);
            PicsList three = picsLists.get(2);
            String imgthree = three.getKpic();
            Glide.with(getContext()).load(imgthree).into(holdView.picThree);
            holdView.title.setText(one.getAlt());
            return view;
        }
        public View getFourPic(int i, View view, ViewGroup viewGroup) {
            HoldView holdView;
            if (view == null) {
                view = layoutInflater.inflate(R.layout.activity_picitemfour_layout, null);
                TextView title = (TextView) view.findViewById(R.id.picitemfour_title);
                ImageView imagetopright = (ImageView) view.findViewById(R.id.picitemfour_imgone);
                ImageView imagetopleft = (ImageView) view.findViewById(R.id.picitemfour_imgthtwo);
                ImageView imagebottomright = (ImageView) view.findViewById(R.id.picitemfour_imgthree);
                ImageView imagebottomleft = (ImageView) view.findViewById(R.id.picitemfour_imgfour);


                holdView = new HoldView();
                holdView.pic = imagetopright;
                holdView.picTwo = imagetopleft;
                holdView.picThree = imagebottomright;
                holdView.picFour = imagebottomleft;
                holdView.title = title;

                view.setTag(holdView);
            }
            holdView = (HoldView) view.getTag();
            PicContentNews picContentNews = (PicContentNews) getItem(i);
            Pics pics = picContentNews.getPics();
            ArrayList<PicsList> picsLists = pics.getList();
            PicsList one = picsLists.get(0);
            String img = one.getKpic();
            Glide.with(getContext()).load(img).into(holdView.pic);
            PicsList two = picsLists.get(1);
            String imgtwo = two.getKpic();
            Glide.with(getContext()).load(imgtwo).into(holdView.picTwo);
            PicsList three = picsLists.get(2);
            String imgthree = three.getKpic();
            Glide.with(getContext()).load(imgthree).into(holdView.picThree);
            PicsList four = picsLists.get(3);
            String imgfour = four.getKpic();
            Glide.with(getContext()).load(imgfour).into(holdView.picFour);
            holdView.title.setText(one.getAlt());
            return view;
        }

        public class HoldView {
            ImageView pic;
            ImageView picTwo;
            ImageView picThree;
            ImageView picFour;
            TextView title;

        }
    }

}




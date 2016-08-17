package com.example.scxh.myxlistview.VedioFragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.scxh.myxlistview.Logs;
import com.example.scxh.myxlistview.R;
import com.example.scxh.myxlistview.VedioGsonJava.VedioContent;
import com.example.scxh.myxlistview.VedioGsonJava.VedioContentNews;
import com.example.xlistviewlibary.XListView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainVedioChildFragment extends Fragment implements XListView.IXListViewListener,AdapterView.OnItemClickListener{

    public String url;
    private int mPageNo;
    XListView mXListView;
    int pageNo = 0; //页号 ，表示第几页,第一页从0开始
    int pageSize = 10; //页大小，显示每页多少条数据
    String news_type_id = "T1348647909107";  //新闻类型标识, 此处表示头条新闻
    String video_type_id = "V9LG4B3A0";//hot
    private int mCurrentPageNo = 0; //当前页号
    private int mTotalPageCount = 5; //总页数
    private static final String MAINVEDIO_CACHE_NAME = "com.example.scxh.myxlistview.VedioFragment.MainVedioChildFragment";
    private  String baseUrl = "http://c.3g.163.com/nc/video/list/"+ video_type_id + "/n/" +pageNo*pageSize+ "-" +pageSize+ ".html";
    private ConnectionUtil connectionUtil;
    int length;
    private MyPagerAdapter mPagerAdapter;
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");//hh 小写是十二进制，HH 大写是24进制
    List<VedioContentNews> list = new ArrayList<>();
    Context mContext;

    //    ArrayList<Pics> list = new ArrayList<>();
    public MainVedioChildFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(String str,int num) {
        MainVedioChildFragment mainVedioChildFragment = new MainVedioChildFragment();
        Bundle bundle = new Bundle();
        bundle.putString("MESSAGE",str);
        bundle.putInt("NUM",num);
        mainVedioChildFragment.setArguments(bundle);
        return mainVedioChildFragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        Logs.e("onAttach>>>");
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments()==null?null:getArguments().getString("MESSAGE");
        mPageNo = getArguments()==null?null:getArguments().getInt("NUM");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_vedio_child_layout, container, false);
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mXListView = (XListView) getView().findViewById(R.id.mainvediodchildfragment_xlistview);

        connectionUtil = new ConnectionUtil(getContext());
        mPagerAdapter = new MyPagerAdapter(getContext());
        getDataLists(pageNo);
        mXListView.setAdapter(mPagerAdapter);

        mXListView.setXListViewListener(this);
        mXListView.setPullLoadEnable(true); //上拉加载更多开关
        mXListView.setPullRefreshEnable(true);   //下拉刷新开关
        mXListView.setOnItemClickListener(this);
    }

    public void getDataLists(int pageNo) {
        final String baseUrl = "http://c.3g.163.com/nc/video/list/"+ url + "/n/" +pageNo*pageSize+ "-" +pageSize+ ".html";
        connectionUtil.asyncConnect(baseUrl, ConnectionUtil.Mothod.GET, new ConnectionUtil.HttpConnectionInterface() {

            public void excute(String cont) {
                if (cont == null) {
                    Toast.makeText(getContext(), "请求出错!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mContext.getSharedPreferences(MAINVEDIO_CACHE_NAME, Context.MODE_PRIVATE).edit().putString(baseUrl, cont).commit();
                }
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
        VedioContent conten = gson.fromJson(content, VedioContent.class);
        switch (mPageNo){
            case 0:
                setHotGson(conten);
                break;
            case 1:
                setEntertainment(conten);
                break;
            case 2:
                setFunny(conten);
                break;
            case 3:
                setSelected(content);
                break;
        }

    }
    public void setHotGson( VedioContent conten){
        ArrayList<VedioContentNews> vedioContentNews = conten.getV9LG4B3A0();
        mPagerAdapter.addDataList(vedioContentNews);
    }
    public void setEntertainment( VedioContent conten){
        ArrayList<VedioContentNews> vedioContentNews = conten.getV9LG4CHOR();
        mPagerAdapter.addDataList(vedioContentNews);
    }
    public void setFunny( VedioContent conten){
        ArrayList<VedioContentNews> vedioContentNews = conten.getV9LG4E6VR();
        mPagerAdapter.addDataList(vedioContentNews);
    }
    public void setSelected(String content) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(content);
            JSONArray jsonObj = jsonObject.getJSONArray("00850FRB");
            length = jsonObj.length();
            Logs.e("length>>>>" + length);
            for (int g = 0; g < length; g++) {
                VedioContentNews vedioContentNews = new VedioContentNews();
                JSONObject jsonObjItemads = jsonObj.getJSONObject(g);
                vedioContentNews.setTitle(jsonObjItemads.getString("title"));
                vedioContentNews.setCover(jsonObjItemads.getString("cover"));
                vedioContentNews.setMp4_url(jsonObjItemads.getString("mp4_url"));
                list.add(vedioContentNews);
            }
            }catch(JSONException e){
                e.printStackTrace();
            }
        mPagerAdapter.addDataList(list);
        }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        VedioContentNews vedioContentNews = (VedioContentNews) adapterView.getAdapter().getItem(i);
        String httpUrl = vedioContentNews.getMp4_url();
        Uri uri = Uri.parse(httpUrl);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);

    }

    @Override
    public void onRefresh() {
        pageNo = 0;
        getDataLists(pageNo);
        mXListView.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        ++pageNo;
        if (pageNo > mTotalPageCount) {
            pageNo = mTotalPageCount;
            mXListView.stopLoadMore();
            Toast.makeText(getContext(), "已加载到最后一页", Toast.LENGTH_SHORT).show();
            return;
        }
        Logs.e("pageNo>>>"+pageNo);
        getDataLists(pageNo);
    }


    class MyPagerAdapter extends BaseAdapter {
        List<VedioContentNews> list = new ArrayList<>();
        LayoutInflater layoutInflater;

        public MyPagerAdapter(Context context) {
            this.layoutInflater = LayoutInflater.from(context);
        }

        public void addDataList(List<VedioContentNews> list) {
            this.list.addAll(list);
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


        public View getView(int i, View view, ViewGroup viewGroup) {
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
            VedioContentNews vedioContentNews = (VedioContentNews) getItem(i);
            Glide.with(getContext()).load(vedioContentNews.getCover()).into(holdView.pic);
            holdView.title.setText(vedioContentNews.getTitle());//对于直接从网络取数据的图片使用第三方包，还可以缓存
            return view;
        }
        public class HoldView {
            ImageView pic;
            TextView title;

        }
    }

}





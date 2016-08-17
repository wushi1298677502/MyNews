package com.example.scxh.myxlistview.MyTextFragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.scxh.myxlistview.MyTextGsonJava.Ads;
import com.example.scxh.myxlistview.AlreadyDefinitUtils.AsyncMemoryCacheImageLoader;
import com.example.scxh.myxlistview.AlreadyDefinitUtils.ConnectionUtil;
import com.example.scxh.myxlistview.MyTextGsonJava.Content;
import com.example.scxh.myxlistview.MyActivity.ContentActivity;
import com.example.scxh.myxlistview.MyTextGsonJava.Imgextra;
import com.example.scxh.myxlistview.Logs;
import com.example.scxh.myxlistview.R;
import com.example.scxh.myxlistview.MyTextGsonJava.news;
import com.example.xlistviewlibary.XListView;
import com.google.gson.Gson;
import com.scxh.slider.library.SliderLayout;
import com.scxh.slider.library.SliderTypes.BaseSliderView;
import com.scxh.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements XListView.IXListViewListener,AdapterView.OnItemClickListener{

    /**
     *   1.头条新闻列表接口
     参数定义:
     int pageNo = 0; //页号 ，表示第几页,第一页从0开始
     int pageSize = 20; //页大小，显示每页多少条数据
     String news_type_id = "T1348647909107";  //新闻类型标识, 此处表示头条新闻

     Url请求地址: "http://c.m.163.com/nc/article/headline/"+ news_type_id +pageNo*pageSize+ "-"  +pageSize+ ".html"

     请求方式:Get

     例如: http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html        //表示请求头条新闻第一页20条数据
     http://c.m.163.com/nc/article/headline/T1348647909107/20-20.html    //表示请求头条新闻第二页20条数据
     http://c.m.163.com/nc/article/headline/T1348647909107/40-20.html    //表示请求头条新闻第三页20条数据
     int docid =   A90HHI6I00014SEH ; //新闻ID ,从新闻列表项目获取
     */
    private static final int PAGE_SIZE = 20; //每页数据个数
    public static final String CHACH_NAME = "com.example.scxh.myxlistview.MyTextFragment.MainFragment";
    public static final String CHACH_ENTERTAINMENT_NAME = "com.example.scxh.myxlistview.MyTextFragment.EntertainmentFragment";
    int pageNo = 0; //页号 ，表示第几页,第一页从0开始
    int nums;
    int pageSize = 20; //页大小，显示每页多少条数据
    String news_type_id = "T1348647909107";  //新闻类型标识, 此处表示头条新闻
    private int mCurrentPageNo = 0; //当前页号
    private int mTotalPageCount = 5; //总页数
    private XListView mListView;
    private MyPagerAdapter mPagerAdapter;
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");//hh 小写是十二进制，HH 大写是24进制
    String baseUrl ;
    String cacheContent;
    private ConnectionUtil connectionUtil;
    ArrayList<news> list = new ArrayList<>();
    ArrayList<Ads> listAds = new ArrayList<>();
    JSONArray ads;
    Context mContext;
    TextView mTextView;
    int length;
    Ads a ;
    public String urls;
    private int mPageNo;
    SliderLayout mSliderLayout;

    public MainFragment() {

        // Required empty public constructor
    }


    public static Fragment newInstance(String str,int num){
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("MESSAGE",str);
        bundle.putInt("NUM",num);
        mainFragment.setArguments(bundle);
        return mainFragment;
    }
    public void onAttach(Context context) {
        super.onAttach(context);
        Logs.e("onAttach>>>");
        this.mContext = context;

    }
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        news_type_id = getArguments()==null?null:getArguments().getString("MESSAGE");
        nums = getArguments()==null?-1:getArguments().getInt("NUM");
        Logs.e("news_type_id>>>"+news_type_id);
        if(nums == 0){
            baseUrl = "http://c.m.163.com/nc/article/headline/"+ news_type_id +"/"+pageNo*pageSize+ "-"  +pageSize+ ".html";
        }else {
            baseUrl = "http://c.m.163.com/nc/article/list/"+ news_type_id +"/"+pageNo*pageSize+ "-"  +pageSize+ ".html";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = (XListView)getView().findViewById(R.id.myXlistview);
        Logs.e("mListView>>>>"+mListView);

        View viewtwo = LayoutInflater.from(getContext()).inflate(R.layout.activity_viewpager_layout, null);
        mSliderLayout = (SliderLayout)viewtwo. findViewById(R.id.mysliderlayout);

        connectionUtil = new ConnectionUtil(getContext());
        mPagerAdapter = new MyPagerAdapter(getContext());

        mListView.setAdapter(mPagerAdapter);

        mListView.setXListViewListener(this);
        mListView.setPullLoadEnable(true); //上拉加载更多开关
        mListView.setPullRefreshEnable(true);   //下拉刷新开关




            if(nums == 0){
                cacheContent = mContext.getSharedPreferences(CHACH_NAME,Context.MODE_PRIVATE).getString(baseUrl,null);

            }else {
                cacheContent = mContext.getSharedPreferences(CHACH_ENTERTAINMENT_NAME,Context.MODE_PRIVATE).getString(baseUrl,null);
            }
        if (cacheContent != null){
            setViewpage(cacheContent);
            setListview(cacheContent);
        }

        Logs.w("cacheContent>>>"+cacheContent);
        getDataLists(mCurrentPageNo);
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);

        mListView.addHeaderView(viewtwo);
        mListView.setOnItemClickListener(this);
    }
    public void onResume() {
        super.onResume();
        Logs.e("onResume>>>");

    }



    public void onRefresh() {
        mSliderLayout.removeAllSliders();  //首先移除原先加载的再添加新的
        Logs.e("onRefresh");
        pageNo = 0;
        getDataLists(pageNo);
        mListView.stopRefresh();

    }

    public void onLoadMore() {
        ++pageNo;
        if (pageNo > mTotalPageCount) {
            pageNo = mTotalPageCount;
            mListView.stopLoadMore();
            Toast.makeText(getContext(), "已加载到最后一页", Toast.LENGTH_SHORT).show();
            return;
        }
        Logs.e("pageNo>>>"+pageNo);
        getDataLists(pageNo);

    }
    public void getDataLists(int pageNo) {


        connectionUtil.asyncConnect(baseUrl, ConnectionUtil.Mothod.GET, new ConnectionUtil.HttpConnectionInterface() {

            public void excute(String cont) {
                Logs.e("MainFragment>>>"+baseUrl);
                if (cont == null) {
                    Toast.makeText(getContext(), "请求出错!", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if(news_type_id.contains("T1348647909107")){
                        Logs.e("getDatalists>>>>T1348647909107");
                        mContext.getSharedPreferences(CHACH_NAME,Context.MODE_PRIVATE).edit().putString(baseUrl,cont).commit();

                    }else if(news_type_id.contains("T1348648517839")){
                        Logs.e("getDatalists>>>>T1348648517839");
                        mContext.getSharedPreferences(CHACH_ENTERTAINMENT_NAME,Context.MODE_PRIVATE).edit().putString(baseUrl,cont).commit();
                    }else {}
                }
                setViewpage(cont);
                setListview(cont);

            }

        });

    }
    public void setListview(String content){
        Logs.e("content :" + content);
        mListView.stopLoadMore();
        mListView.stopRefresh();
        mListView.setRefreshTime(simpleDateFormat.format(new Date(System.currentTimeMillis())));

        Gson gson = new Gson();
        Content conten = gson.fromJson(content, Content.class);

        Logs.e("nums >>> :"+nums);
        mTotalPageCount = 5;
        switch (nums){
            case 0:
                setMainGson(conten);
                break;
            case 1:
                setEntertainment(conten);
                break;
            case 2:
                setSports(conten);
                break;
            case 3:
                setFinance(conten);
                break;
            case 4:
                setScience(conten);
                break;
        }



    }
    public void setMainGson( Content conten){
        list = conten.getT1348647909107();
        Logs.e("list>>>>setMainGson"+list);
        mPagerAdapter.addDataList(list);

    }
    public void setEntertainment(Content conten){
        list = conten.getT1348648517839();
        Logs.e("list>>>>setEntertainment"+list);
        mPagerAdapter.addDataList(list);
    }
    public void setSports( Content conten){
         list = conten.getT1348649079062();
        Logs.e("list>>>>setSports"+list);
        mPagerAdapter.addDataList(list);
    }
    public void setFinance( Content conten){
        ArrayList<news> list = conten.getT1348648756099();
        mPagerAdapter.addDataList(list);
    }
    public void setScience( Content conten){
        ArrayList<news> list = conten.getT1348649580692();
        mPagerAdapter.addDataList(list);
    }
    public void setViewpage(String content){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(content);
            Logs.e("news_type_id>>>"+news_type_id);
            JSONArray jsonObj = jsonObject.getJSONArray(news_type_id);
            length = jsonObj.length();
            JSONObject jsonObjItemads = jsonObj.getJSONObject(0);
            ads= jsonObjItemads.getJSONArray("ads");
            int len = ads.length();
            Logs.e("ads>>>>"+ads);
            Logs.e("len>>>>"+len);
            for(int g = 0;g<len;g++) {
                JSONObject jsonObjItem = jsonObj.getJSONObject(g);
                String imgsrc = jsonObjItem.getString("imgsrc");
                String tex = jsonObjItem.getString("title");
                Logs.e("imgsrc>>>>" + imgsrc);

                TextSliderView textSliderView = new TextSliderView(getContext());
                textSliderView
                        .description(tex)
                        .image(imgsrc)
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(textSliderView);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        news n = (news) adapterView.getAdapter().getItem(i);
        String docid = n.getDocid();
        Logs.e("docid>>>>"+docid);
        Bundle bundle = new Bundle();
        bundle.putString("DOCID",docid);
        Intent intent = new Intent(getContext(),ContentActivity.class);
        intent.putExtra("BUNDLE",bundle);
        startActivity(intent);

    }

    class MyPagerAdapter extends BaseAdapter {
        List<news> list = new ArrayList<>();
        LayoutInflater layoutInflater;
        public MyPagerAdapter(Context context){
            this.layoutInflater = LayoutInflater.from(context);
        }
        public void addDataList(List<news> list){
            this.list.addAll(list);
            notifyDataSetChanged();
        }
        public int getCount() {
            Logs.e("list>>>"+list);
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
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            news n = (news) getItem(position);
            ArrayList<Imgextra> arrayList = n.getImgextra();
            if(arrayList != null){
                return 0;
            }else {
                return 1;
            }
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            int type = getItemViewType(i);
            if(type == 0){
                return getExtraView(i,view,viewGroup);
            }else {
                return getNormalView(i,view,viewGroup);
            }
        }

        public View getNormalView(int i, View view, ViewGroup viewGroup){
            HoldView holdView;
            if(view == null){
                view = layoutInflater.inflate(R.layout.activity_item_layout,null);
                ImageView imageView = (ImageView) view.findViewById(R.id.item_img);
                TextView title = (TextView) view.findViewById(R.id.item_title);
                TextView content = (TextView) view.findViewById(R.id.item_content);

                holdView = new HoldView();
                holdView.pic = imageView;
                holdView.title = title;
                holdView.content = content;
                view.setTag(holdView);
            }
            holdView = (HoldView) view.getTag();
            news n = (news) getItem(i);
            Logs.e("normal>>>i"+i);
            String title = n.getTitle();
            String content = n.getDigest();
            String img = n.getImgsrc();

            holdView.title.setText(title);
            holdView.content.setText(content);
            Glide.with(getContext()).load(img).into(holdView.pic);//对于直接从网络取数据的图片使用第三方包，还可以缓存
            return view;
        }

        // TODO: 2016/8/8 获取多张图片
        public View getExtraView(int i, View view, ViewGroup viewGroup){
            final HoldViewTwo holdViewTwo;
            if (view == null) {
                // todo 一级优化  优化View不被重复解析
                view = layoutInflater.inflate(R.layout.activity_imgextra_layout, null);
                TextView textView = (TextView) view.findViewById(R.id.imgextra_title);
                ImageView left = (ImageView) view.findViewById(R.id.imgextra_left);
                ImageView center = (ImageView) view.findViewById(R.id.imgextra_center);
                ImageView right = (ImageView) view.findViewById(R.id.imgextra_right);

                // todo 二级优化  优化view控件不被重复加载
                holdViewTwo = new HoldViewTwo();
                holdViewTwo.title = textView;
                holdViewTwo.left = left;
                holdViewTwo.center = center;
                holdViewTwo.right = right;
                view.setTag(holdViewTwo);// todo setTag里面放的是一个对象，为了view控件不被重复加载，需要再定义一个类放控件
            }else{
                holdViewTwo = (HoldViewTwo) view.getTag();
            }
            // todo 从View对象中获取控件实例
            news item = (news) getItem(i);
            Logs.e("getExtraView>>>i"+i);
            String title = item.getTitle();
            String img = item.getImgsrc();
            ArrayList<Imgextra> arrayList = item.getImgextra();
            String center = arrayList.get(0).getImgsrc();
            String right = arrayList.get(1).getImgsrc();
            Logs.e("getExtraView>>>right."+right);
            Logs.e("getExtraView>>>img."+img);
            Logs.e("getExtraView>>>center."+center);
            holdViewTwo.title.setText(title);
            AsyncMemoryCacheImageLoader.getInstanceAsycnHttpImageView().loadBitmap(getContext().getResources(),img,holdViewTwo.left,R.drawable.ic_launcher,0,0);
            AsyncMemoryCacheImageLoader.getInstanceAsycnHttpImageView().loadBitmap(getContext().getResources(),center,holdViewTwo.center,R.drawable.ic_launcher,0,0);
            AsyncMemoryCacheImageLoader.getInstanceAsycnHttpImageView().loadBitmap(getContext().getResources(),right,holdViewTwo.right,R.drawable.ic_launcher,0,0);
            return view;
        }
    }
    public class HoldView{
        ImageView pic;
        TextView title;
        TextView content;
    }
    public class HoldViewTwo{
        TextView title;
        ImageView left;
        ImageView center;
        ImageView right;
    }

    /**
     * 从网络获取图片
     *
     * @param httpUrl
     * @return
     */
    public Bitmap doDownLoadPictrue(String httpUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}



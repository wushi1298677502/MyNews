package com.example.scxh.myxlistview.MyActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.scxh.myxlistview.AlreadyDefinitUtils.ConnectionUtil;
import com.example.scxh.myxlistview.MyPicGsonJava.PicContentNewsDetaiPics;
import com.example.scxh.myxlistview.Logs;
import com.example.scxh.myxlistview.MyPicFragment.PicDetailShowFragment;
import com.example.scxh.myxlistview.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PicActivity extends AppCompatActivity {

    ViewPager mVierPager;
    private ConnectionUtil connectionUtil;
    String  id =   "A90HHI6I00014SEH" ; //新闻ID ,从新闻列表项目获取
    ArrayList<PicContentNewsDetaiPics> list = new ArrayList<>();
    myAdapter adapter;
    int total;
    ProgressBar mProgressBar;
    Toolbar mToolbar;
    String baseUrl = "http://api.sina.cn/sinago/article.json?postt=hdpic_hdpic_toutiao_4&wm=b207&from=6042095012&chwm=12050_0001&oldchwm=12050_0001&imei=867064013906290&uid=802909da86d9f5fc&id=";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);

        Bundle bundle = getIntent().getBundleExtra("BUNDLE");
        id= bundle.getString("ID");
        total = bundle.getInt("TOTAL");
        Logs.e("ContentActivity>>>>"+id);
        connectionUtil = new ConnectionUtil(this);//记住实例化，否这会 java.lang.NullPointerException: Attempt to invoke virtual method 'void com.example.scxh.myxlistview.AlreadyDefinitUtils.ConnectionUtil.asyncConnect(java.lang.String, com.example.scxh.myxlistview.AlreadyDefinitUtils.ConnectionUtil$Mothod, com.example.scxh.myxlistview.AlreadyDefinitUtils.ConnectionUtil$HttpConnectionInterface)' on a null object reference
        mVierPager = (ViewPager) findViewById(R.id.myfragment_viewpager);
        mProgressBar = (ProgressBar) findViewById(R.id.pic_progressbar);
        mToolbar = (Toolbar) findViewById(R.id.pic_ToolBar);
        mProgressBar.setVisibility(View.VISIBLE);
        getDataLists();
        adapter = new myAdapter(getSupportFragmentManager());
        mVierPager.setAdapter(adapter);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void getDataLists() {
        String httpUrl = baseUrl + id;
        Logs.e("id>>>"+id);
        Logs.e("PicActivity.getDataLists.httpUrl>>>"+httpUrl);
        connectionUtil.asyncConnect(httpUrl, ConnectionUtil.Mothod.GET, new ConnectionUtil.HttpConnectionInterface() {

            public void excute(String cont) {
                if (cont == null) {
                    Toast.makeText(PicActivity.this, "请求出错!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mProgressBar.setVisibility(View.GONE);
                setViewpage(cont);
                Logs.e("list>>>>"+list.size());
                adapter.SetDatalist(list);

            }

        });

    }
    public void setViewpage(String content){
        JSONObject jsonObject ;
        try {
            jsonObject = new JSONObject(content);
            JSONObject jsonObjectone = jsonObject.getJSONObject("data");
            String title = jsonObjectone.getString("title");
            String lead = jsonObjectone.getString("lead");
            Logs.e("lead>>>>"+lead);
            JSONArray pics = jsonObjectone.getJSONArray("pics");
            int len = pics.length();
            Logs.e("length>>>"+len);
            for(int g = 0;g<len;g++) {
                JSONObject jsonObjItem = pics.getJSONObject(g);
                String imgsrc = jsonObjItem.getString("kpic");
                String tex = jsonObjItem.getString("alt");
                Logs.e("tex>>>>" + tex);
                Logs.e("imgsrc>>>>" + imgsrc);
                PicContentNewsDetaiPics   picContentNewsDetaiPics = new PicContentNewsDetaiPics();
                picContentNewsDetaiPics.setAlt(tex);
                picContentNewsDetaiPics.setKpic(imgsrc);
                list.add(picContentNewsDetaiPics);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    class myAdapter extends FragmentStatePagerAdapter {
        ArrayList<PicContentNewsDetaiPics> list  = new ArrayList<>(); // java.lang.NullPointerException: Attempt to invoke virtual method 'int java.util.ArrayList.size()' on a null object reference
        public myAdapter(FragmentManager fm) {
            super(fm);
        }
        public void SetDatalist(  ArrayList<PicContentNewsDetaiPics> list){
            this.list = list;
            notifyDataSetChanged();
        }
        @Override
        public Fragment getItem(int position) {
            Logs.e("list.get(position).getKpic()>>>"+list.get(position).getKpic());
            return PicDetailShowFragment.newInstance(list.get(position).getKpic(),list.get(position).getAlt(),position+1,total);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}

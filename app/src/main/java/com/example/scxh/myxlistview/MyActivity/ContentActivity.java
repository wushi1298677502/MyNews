package com.example.scxh.myxlistview.MyActivity;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scxh.myxlistview.AlreadyDefinitUtils.ConnectionUtil;
import com.example.scxh.myxlistview.Logs;
import com.example.scxh.myxlistview.R;
import com.scxh.slider.library.SliderLayout;
import com.scxh.slider.library.SliderTypes.BaseSliderView;
import com.scxh.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContentActivity extends AppCompatActivity {

    private ConnectionUtil connectionUtil;
    String  docid =   "A90HHI6I00014SEH" ; //新闻ID ,从新闻列表项目获取
    TextView mtitle,mcontent,mresource;
    SliderLayout sliderLayout;
    public ProgressBar mProgressBar;
    public Toolbar mToolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_content_layout);
        Bundle bundle = getIntent().getBundleExtra("BUNDLE");
        docid= bundle.getString("DOCID");
        Logs.e("ContentActivity>>>>"+docid);

        connectionUtil = new ConnectionUtil(this);//记住实例化，否这会 java.lang.NullPointerException: Attempt to invoke virtual method 'void com.example.scxh.myxlistview.AlreadyDefinitUtils.ConnectionUtil.asyncConnect(java.lang.String, com.example.scxh.myxlistview.AlreadyDefinitUtils.ConnectionUtil$Mothod, com.example.scxh.myxlistview.AlreadyDefinitUtils.ConnectionUtil$HttpConnectionInterface)' on a null object reference
        mtitle = (TextView) findViewById(R.id.contentfragment_title);
        mcontent = (TextView) findViewById(R.id.contentfragment_body);
        mresource = (TextView)findViewById(R.id.contentfragment_source);
        mProgressBar = (ProgressBar) findViewById(R.id.content_progressbar);
        sliderLayout = (SliderLayout)findViewById(R.id.contentfragment_sliderlayout);
        mToolbar = (Toolbar) findViewById(R.id.content_ToolBar);
        mProgressBar.setVisibility(View.VISIBLE);
        getDataLists(docid);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void getDataLists(String  docid) {
        String baseUrl = "http://c.m.163.com/nc/article/"+docid +"/full.html";
        Logs.e("docid>>>"+docid);
        connectionUtil.asyncConnect(baseUrl, ConnectionUtil.Mothod.GET, new ConnectionUtil.HttpConnectionInterface() {

            public void excute(String cont) {
                if (cont == null) {
                    Toast.makeText(ContentActivity.this, "请求出错!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mProgressBar.setVisibility(View.GONE);
                setViewpage(cont);

            }

        });

    }
    public void setViewpage(String content){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(content);
            JSONObject jsonObjectone = jsonObject.getJSONObject(docid);
            String body = jsonObjectone.getString("body");
            Logs.e("body>>>>"+body);
            String title = jsonObjectone.getString("title");
            String source = jsonObjectone.getString("source");
            mtitle.setText(Html.fromHtml(title));
            mcontent.setText(Html.fromHtml(body));
            mresource.setText(Html.fromHtml(source));
            JSONArray jsonArray = jsonObjectone.getJSONArray("img");
            int len = jsonArray.length();
            Logs.e("length>>>"+len);
            for(int g = 0;g<len;g++) {
                JSONObject jsonObjItem = jsonArray.getJSONObject(g);
                String imgsrc = jsonObjItem.getString("src");
                String tex = jsonObjItem.getString("alt");
                Logs.e("imgsrc>>>>" + imgsrc);

                TextSliderView textSliderView = new TextSliderView(this);
                textSliderView
                        .description(tex)
                        .image(imgsrc)
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                sliderLayout.addSlider(textSliderView);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


//    public void onBackPressed(){
//
//        Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
//        if(back==true){
//
//            finish();
//        }
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                back=true;
//                SystemClock.sleep(2000);
//                back=false;
//            }
//        }).start();
//
//    }
}
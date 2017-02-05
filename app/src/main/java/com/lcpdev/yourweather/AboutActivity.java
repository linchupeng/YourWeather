package com.lcpdev.yourweather;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by linchupeng on 2017/2/4.
 * github:https://github.com/linchupeng/YourWeather
 */

public class AboutActivity extends BaseActivity {
    private Button btn_code;
    private Button btn_shared;
    private Button btn_friends;
    private Button btn_update;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        btn_code= (Button) findViewById(R.id.btn_code);
        btn_friends= (Button) findViewById(R.id.btn_friends);
        btn_shared= (Button) findViewById(R.id.btn_share);
        btn_update= (Button) findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new BtnListener());
        btn_shared.setOnClickListener(new BtnListener());
        btn_friends.setOnClickListener(new BtnListener());
        btn_code.setOnClickListener(new BtnListener());
    }
    class BtnListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_code:
                    goToHtml("https://github.com/linchupeng/YourWeather");
                    break;

            }
        }
    }

    private void goToHtml(String url) {
        Uri uri = Uri.parse(url);   //指定网址
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);           //指定Action
        intent.setData(uri);                            //设置Uri
        startActivity(intent);        //启动Activity
    }

}

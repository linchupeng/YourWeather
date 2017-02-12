package com.lcpdev.yourweather.model;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.lcpdev.yourweather.MainActivity;
import com.lcpdev.yourweather.R;

/**
 * Created by LCP on 2017/2/6.
 * @ Email:chuge94@163.com
 * GitHub:https://github.com/linchupeng/YourWeather
 */

public class FirstActivity extends Activity {
    private TextView countDown;
    private MyCountDownTimer downTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        countDown= (TextView) findViewById(R.id.countDown);
        downTimer =new MyCountDownTimer(3000,1000);
        downTimer.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(intent);
                FirstActivity.this.finish();
            }
        },2000);
    }
    private Handler handler =new Handler();

    /**
     * 继承CountDownTimer 重写onTick，onFinish方法
     */
    private class MyCountDownTimer extends CountDownTimer{
        /**
         * millisInFuture
         *     表示以毫秒为单位 倒计时的总数
         *
         *     例如 millisInFuture=1000 表示1秒
         * countDownInterval
         *     表示 间隔 多少微秒 调用一次 onTick 方法
         *
         *     例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         *
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisInFuture) {
        countDown.setText("倒计时（"+millisInFuture/1000+")");
        }

        @Override
        public void onFinish() {
        countDown.setText("正在跳转...");
        }
    }
}

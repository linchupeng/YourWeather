package com.lcpdev.yourweather.model;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcpdev.yourweather.MainActivity;
import com.lcpdev.yourweather.R;

import static android.R.attr.width;

/**
 * Created by LCP on 2017/2/6.
 * @ Email:chuge94@163.com
 * GitHub:https://github.com/linchupeng/YourWeather
 */

public class FirstActivity extends Activity {
    private ImageView splashSun;
    private ImageView splashCould1;
    private ImageView splashCould2;
    private ImageView splashCould3;
    private TextView countDown;
    private MyCountDownTimer downTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        initView();
        downTimer = new MyCountDownTimer(4000, 1000);
        downTimer.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(intent);
                FirstActivity.this.finish();
            }
        }, 1000);
    }
    private Handler handler =new Handler();

    private void initView() {
        splashSun= (ImageView) findViewById(R.id.splashSun);
        splashCould1= (ImageView) findViewById(R.id.splashCloud1);
        splashCould2= (ImageView) findViewById(R.id.splashCould2);
        splashCould3= (ImageView) findViewById(R.id.splashCould3);
        countDown= (TextView) findViewById(R.id.countDown);
        // 需要在布局填充完成后才能获取到View的尺寸
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        playAnim();
                        // 需要移除监听，否则会重复触发
                        getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    /**
     * 设置动画属性 其中太阳360无限循环 云朵水平平移
     */
    private void playAnim() {
        playSunAnim();
        playCloud_1Anim();
        playCloud_2Anim();
        playCloud_3Anim();
    }

    private void playSunAnim() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(splashSun, "rotation", 0f, 360f);
        anim.setRepeatMode(ObjectAnimator.RESTART);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(30 * 1000);
        anim.start();
    }

    private void playCloud_1Anim() {
        float cloud1TranslationX = splashCould1.getTranslationX();
        ObjectAnimator anim = ObjectAnimator.ofFloat(splashCould1, "translationX", cloud1TranslationX-250f,cloud1TranslationX);
        anim.setDuration(8 * 1000);
        anim.start();
    }

    private void playCloud_2Anim() {
        float cloud2TranslationX = splashCould2.getTranslationX();
        ObjectAnimator anim = ObjectAnimator.ofFloat(splashCould2, "translationX", cloud2TranslationX-200f, cloud2TranslationX);
        anim.setDuration(7* 1000);
        anim.start();
    }

    private void playCloud_3Anim() {
        float cloud3TranslationX = splashCould3.getTranslationX();
        ObjectAnimator anim = ObjectAnimator.ofFloat(splashCould3, "translationX",cloud3TranslationX,-300f,cloud3TranslationX);
        anim.setDuration(8 * 1000);
        anim.start();
    }





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

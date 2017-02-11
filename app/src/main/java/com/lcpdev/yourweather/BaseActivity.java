package com.lcpdev.yourweather;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import static com.lcpdev.yourweather.model.DoubleClickExit.exitTime;

/**
 * Created by 林楚鹏 on 2017/1/17.
 */

public class BaseActivity extends AppCompatActivity{
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbarBase;
    /**
     * 设置状态栏颜色
     * 也就是所谓沉浸式状态栏
////     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar indexToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(indexToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,
                indexToolBar,R.string.drawer_open,R.string.drawer_close);
        toggle.syncState();
        mDrawerLayout.addDrawerListener(toggle);
    }


    }






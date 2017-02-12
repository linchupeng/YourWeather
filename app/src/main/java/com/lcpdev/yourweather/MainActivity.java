
package com.lcpdev.yourweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Parcelable;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lcpdev.yourweather.database.City;
import com.lcpdev.yourweather.gson.Weather;
import com.lcpdev.yourweather.model.Common;
import com.lcpdev.yourweather.util.HttpUtil;
import com.lcpdev.yourweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.R.attr.key;
import static com.lcpdev.yourweather.model.Common.getCityIdByName;

/**
 * Created by LCP on 2017/1/16.
 * @ Email:chuge94@163.com
 * GitHub:https://github.com/linchupeng/YourWeather
 */
public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private List<City> cityList;
    public AMapLocationClient mLocationClient=null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationListener mLocationListener=new AMapLocationListener() {
        @Override public void onLocationChanged(AMapLocation amapLocation) {
            if(amapLocation!=null){
                if(amapLocation.getErrorCode()==0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    String city = amapLocation.getCity();
                    if (!TextUtils.isEmpty(city)) {
                        String cityName = city.replace("市", "");
                        Log.i("定位成功", "当前城市为" + cityName);
                        queryWeatherCode(cityName);
                        Toast.makeText(MainActivity.this, cityName, Toast.LENGTH_SHORT).show();
                    }

                }else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("aMapError", "ErrCode:" + amapLocation.getErrorCode()
                                + ", errInfo:" + amapLocation.getErrorInfo());
                        Log.e("定位失败","");
                    Toast.makeText(MainActivity.this, "糟糕定位失败〒_〒", Toast.LENGTH_SHORT).show();
                }
                        //停止定位
                        mLocationClient.stopLocation();
                        //销毁定位
                        mLocationClient.onDestroy();
             }
         }
     };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigation();
        initToolBar();
        initLocation();


    }

    private void initToolBar() {
        Toolbar indexToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(indexToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,
                indexToolBar,R.string.drawer_open,R.string.drawer_close);
        toggle.syncState();
        mDrawerLayout.addDrawerListener(toggle);
    }

    /**
     * 初始化Navigation参数
     */
    private void initNavigation(){
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_city:
                        Intent intentCity = new Intent(MainActivity.this,ChooseCity.class);
                        startActivity(intentCity);
                        break;
                    case R.id.multi_cities:
                        Toast.makeText(MainActivity.this, "此功能再下个版本添加！", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:
                        Intent intentSetting =new Intent(MainActivity.this,AboutActivity.class);
                        startActivity(intentSetting);
                        break;
                    case R.id.exit:
                        finish();
                }
                return false;
            }
        });
    }



    /**
     * 初始化高德地图定位参数
     */
    private void initLocation() {
        mLocationClient=new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(mLocationListener);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mLocationClient.startLocation();
    }

    /**
     * 转换城市编码
     */
    private void queryWeatherCode(String cityName) {
//        try {
//            String str = new String(cityName.getBytes(), "UTF-8");
//            cityName = URLEncoder.encode(str, "UTF-8");
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
        String weatherId = Common.getCityIdByName(cityName);
        Intent intent =new Intent(MainActivity.this,WeatherActivity.class);
        intent.putExtra("weather_id",weatherId);
        startActivity(intent);
        finish();
        }

    /**
     * 点击返回键两次退出程序
     */
    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else{
                finish();
                System.exit(0);
                Process.killProcess(Process.myPid());
            }

        }
    }

}

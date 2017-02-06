
package com.lcpdev.yourweather;

import android.content.Intent;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
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
                    String info1 = amapLocation.getCity();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                    amapLocation.getCountry();//国家信息
//                    amapLocation.getProvince();//省信息
                    amapLocation.getCity();//城市信息
//                    amapLocation.getDistrict();//城区信息
//                    amapLocation.getStreet();//街道信息
//                    amapLocation.getStreetNum();//街道门牌号信息
//                    amapLocation.getCityCode();//城市编码
//                    amapLocation.getAdCode();//地区编码
//                    amapLocation.getAoiName();//获取当前定位点的AOI信息
                    Toast.makeText(MainActivity.this, info1, Toast.LENGTH_SHORT).show();
                }else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("aMapError", "ErrCode:" + amapLocation.getErrorCode()
                                + ", errInfo:" + amapLocation.getErrorInfo());
                    Toast.makeText(MainActivity.this, "糟糕定位失败〒_〒", Toast.LENGTH_SHORT).show();
                }
                        Log.i("Tag",amapLocation.getCity());
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNavigation();
        initLocation();

    }
    /**
     * 初始化Navigation参数
     */

    private void initNavigation(){
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar =getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        }
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
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
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
     * 点击返回键两次退出程序
     */
    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis()-exitTime>2000){
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }else {
            finish();
            System.exit(0);
            Process.killProcess(Process.myPid());
        }

    }


}

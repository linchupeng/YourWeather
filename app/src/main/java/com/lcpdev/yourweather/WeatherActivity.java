package com.lcpdev.yourweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lcpdev.yourweather.gson.Forecast;
import com.lcpdev.yourweather.gson.Weather;
import com.lcpdev.yourweather.model.ActivityCollector;
import com.lcpdev.yourweather.model.Common;
import com.lcpdev.yourweather.util.HttpUtil;
import com.lcpdev.yourweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.R.attr.name;
import static com.lcpdev.yourweather.R.layout.forecast;
import static com.lcpdev.yourweather.model.Common.getDate;
import static com.lcpdev.yourweather.model.DoubleClickExit.exitTime;

public class WeatherActivity extends BaseActivity{
    private ScrollView scrWeatherLayout;
    private TextView tempText;
    private ImageView imgWeather;
    private TextView titleCityName;
    private DrawerLayout mDrawerLayout;
//    private TextView temp_Min;
//    private TextView temp_Max;
    private TextView weatherInfoText;
    private TextView pm25Text;
    private TextView aqiText;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private LinearLayout forecastLayout;
    private Toolbar toolbarWeather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();
//        initToolBar();
    }
//    private void initToolBar() {
//        toolbarWeather = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbarWeather);
//
//        if (getSupportActionBar()!=null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            //去除默认Title显示
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//        }
////        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,
////                toolbarWeather,R.string.drawer_open,R.string.drawer_close);
////        toggle.syncState();
////        mDrawerLayout.addDrawerListener(toggle);
//
//
//    }

    /**
     *初始化天气信息所需要的控件
     */
    public void initView() {

        scrWeatherLayout = (ScrollView) findViewById(R.id.weather_scrollView);
        titleCityName= (TextView) findViewById(R.id.toolbarCN);
        tempText = (TextView) findViewById(R.id.temp);
        imgWeather= (ImageView) findViewById(R.id.img_cond);
        weatherInfoText = (TextView) findViewById(R.id.weather_info);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm2_5);
        comfortText = (TextView) findViewById(R.id.comf_txt);
        carWashText = (TextView) findViewById(R.id.car_txt);
        sportText = (TextView) findViewById(R.id.sport_txt);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("Weather", null);




        if (weatherString != null) {
            //有缓存时直接解析天气
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        } else {
            //无缓存时去服务器查询天气
            String weatherId = getIntent().getStringExtra("weather_id");
            scrWeatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
            Log.d("weatherActivity_ID",weatherId);
        }


    }
    //public替换private
    public  void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String temp = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        int imgCode = weather.now.more.code;
        //BugBugBugBugBugBug!!!!卡在这里了.....
        tempText.setText(temp);
        titleCityName.setText(cityName);
        weatherInfoText.setText(weatherInfo);
        if (imgCode == 100) {
            imgWeather.setImageResource(R.mipmap.sun);
        }
        if ((imgCode >= 101 && imgCode <= 213) || (imgCode >= 500 && imgCode <= 901)) {
            imgWeather.setImageResource(R.mipmap.cloudy);
        }
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastsList){
            View view = LayoutInflater.from(this).inflate(R.layout.item_forecast,forecastLayout,false);
            TextView dateText= (TextView) view.findViewById(R.id.date_Text);
            TextView infoText = (TextView) view.findViewById(R.id.info_Text);
            TextView minText= (TextView) view.findViewById(R.id.min_Text);
            TextView maxText= (TextView) view.findViewById(R.id.max_Text);
            String WeatherDate=forecast.date;
            String weekDate = Common.getDate(WeatherDate);
            dateText.setText(weekDate);
            infoText.setText(forecast.more.info);
            minText.setText(forecast.temperature.min+"℃");
            maxText.setText(forecast.temperature.max+"℃");
            forecastLayout.addView(view);
        }
        if (weather.aqi !=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort ="舒适度："+weather.suggestion.comfort.info;
        String carWash ="洗车指数："+weather.suggestion.carWash.info;
        String sport="活动建议："+weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        scrWeatherLayout.setVisibility(View.VISIBLE);
    }

    public void requestWeather(String weatherId) {
       String weatherUrl = "https://api.heweather.com/v5/weather?city="+weatherId+"&key=342a3bf415f84fc7ba09cf90e66fcee1";
        Log.i("天气详情", weatherUrl);
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "一不小心获取失败了Q_Q ", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("status", "status");
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "后台获取天气失败( >﹏< )", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis()-exitTime>2000){
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }else {
            ActivityCollector.finishAll();
            System.exit(0);

        }
    }
}




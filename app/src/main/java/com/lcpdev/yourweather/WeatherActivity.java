package com.lcpdev.yourweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lcpdev.yourweather.gson.Forecast;
import com.lcpdev.yourweather.gson.HourForecast;
import com.lcpdev.yourweather.gson.Weather;
import com.lcpdev.yourweather.model.Common;
import com.lcpdev.yourweather.util.HttpUtil;
import com.lcpdev.yourweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.lcpdev.yourweather.R.mipmap.air;
import static com.lcpdev.yourweather.R.mipmap.comf;

/**
 * Created by LCP on 2017/1/29.
 * @ Email:chuge94@163.com
 * GitHub:https://github.com/linchupeng/YourWeather
 */

public class WeatherActivity extends BaseActivity{
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbarCity;
    private ScrollView scrWeatherLayout;
    private TextView tempText;
    private ImageView imgWeather;
    private TextView titleCityName;
    private TextView weatherInfoText;
    private TextView airQlty;
    private TextView pm25Text;
    private TextView airBrf;
    private TextView comfBrf;
    private TextView fluBrf;
    private TextView drsgBrf;
    private TextView aqiText;
    private TextView airTxt;
    private TextView comfortTxt;
    private TextView influenzaTxt;
    private TextView dressTxt;
    private LinearLayout hourforecastLayout;
    private LinearLayout forecastLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        drawerView();
        initNavigation();
        initView();

    }

    private void drawerView() {
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbarCity = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarCity);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,
                toolbarCity,R.string.drawer_open,R.string.drawer_close);
        toggle.syncState();
        mDrawerLayout.addDrawerListener(toggle);
    }

    private void initNavigation(){
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_city:
                        Intent intentCity = new Intent(WeatherActivity.this,ChooseCity.class);
                        startActivity(intentCity);
                        break;
                    case R.id.multi_cities:
                        Toast.makeText(WeatherActivity.this, "此功能再下个版本添加！", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:
                        Intent intentSetting =new Intent(WeatherActivity.this,AboutActivity.class);
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
     *初始化天气信息所需要的控件
     */
    public void initView() {
//
        scrWeatherLayout = (ScrollView)findViewById(R.id.weather_scrollView);
        titleCityName= (TextView) findViewById(R.id.toolbarCN);
        tempText = (TextView) findViewById(R.id.temp);
        imgWeather= (ImageView) findViewById(R.id.img_cond);
        weatherInfoText = (TextView) findViewById(R.id.weather_info);
        hourforecastLayout= (LinearLayout) findViewById(R.id.hour_layout);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        airQlty= (TextView) findViewById(R.id.airQlty);
        pm25Text = (TextView) findViewById(R.id.pm2_5);
        airBrf = (TextView) findViewById(R.id.sug_air);
        comfBrf= (TextView) findViewById(R.id.sug_comf);
        fluBrf= (TextView) findViewById(R.id.sug_flu);
        drsgBrf= (TextView) findViewById(R.id.sug_drsg);
        airTxt = (TextView) findViewById(R.id.air_txt);
        comfortTxt = (TextView) findViewById(R.id.comf_txt);
        influenzaTxt = (TextView) findViewById(R.id.flu_txt);
        dressTxt = (TextView) findViewById(R.id.drsg_txt);
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
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String temp = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        int imgCode = weather.now.more.code;
        tempText.setText(temp);
        titleCityName.setText(cityName);
        weatherInfoText.setText(weatherInfo);
        if (imgCode == 100) {
            imgWeather.setImageResource(R.mipmap.sun);
        }
        if (imgCode >= 101 && imgCode <= 103){
            imgWeather.setImageResource(R.mipmap.cloudy);
        }
        if (imgCode==104){
            imgWeather.setImageResource(R.mipmap.overcast);
        }
        if (imgCode >= 200 && imgCode <= 213){
            imgWeather.setImageResource(R.mipmap.windy);
        }
        if (imgCode >= 300 && imgCode <= 313 ){
            imgWeather.setImageResource(R.mipmap.rain);
        }
        if (imgCode >= 400 && imgCode <= 407){
            imgWeather.setImageResource(R.mipmap.snow);
        }
        if (imgCode >= 500 && imgCode <= 501){
            imgWeather.setImageResource(R.mipmap.fog);
        }
        if (imgCode == 502){
            imgWeather.setImageResource(R.mipmap.haze);
        }
        if (imgCode >= 503 && imgCode <= 508){
            imgWeather.setImageResource(R.mipmap.sandstorm);
        }
//            LayoutInflater.from(this).inflate(R.layout.item_hour,null);
        hourforecastLayout.removeAllViews();
        for (HourForecast hourForecast:weather.hourForecastList){
            View viewHour =LayoutInflater.from(this).inflate(R.layout.item_hour,hourforecastLayout,false);
            TextView hourText = (TextView) viewHour.findViewById(R.id.hour_clock);
            TextView tmpText = (TextView) viewHour.findViewById(R.id.hour_temp);
            TextView humText = (TextView) viewHour.findViewById(R.id.hour_hum);
            TextView windText = (TextView) viewHour.findViewById(R.id.hour_wind);
            hourText.setText(hourForecast.date);
            tmpText.setText(hourForecast.tmp);
            humText.setText(hourForecast.hum);
            windText.setText(hourForecast.wind.spd+"Km/h");
            hourforecastLayout.addView(viewHour);
        }
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastsList){
            View view = LayoutInflater.from(this).inflate(R.layout.item_forecast,forecastLayout,false);
            TextView dateText= (TextView) view.findViewById(R.id.date_Text);
            TextView infoText = (TextView) view.findViewById(R.id.info_Text);
            TextView minText= (TextView) view.findViewById(R.id.min_Text);
            TextView maxText= (TextView) view.findViewById(R.id.max_Text);
            String WeatherDate=forecast.date;
            //日期转换星期
            String weekDate = Common.getDate(WeatherDate);
            dateText.setText(weekDate);
            infoText.setText(forecast.more.info);
            minText.setText(forecast.temperature.min+"℃");
            maxText.setText(forecast.temperature.max+"℃");
            forecastLayout.addView(view);
        }
        if (weather.aqi !=null){
            airQlty.setText("："+weather.aqi.city.qlty);
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String airbrt = "空气指数---"+weather.suggestion.air.info;
        String comfbrf = "舒适指数---"+weather.suggestion.comfort.info;
        String flubrf = "感冒指数---"+weather.suggestion.flu.info;
        String drsgbrf = "穿衣指数---"+weather.suggestion.drsg.info;
        String airtxt = weather.suggestion.air.infotxt;
        String comforttxt =weather.suggestion.comfort.infotxt;
        String influenzatxt =weather.suggestion.flu.infotxt;
        String dresstxt=weather.suggestion.drsg.infotxt;
        airBrf.setText(airbrt);
        comfBrf.setText(comfbrf);
        fluBrf.setText(flubrf);
        drsgBrf.setText(drsgbrf);
        airTxt.setText(airtxt);
        comfortTxt.setText(comforttxt);
        influenzaTxt.setText(influenzatxt);
        dressTxt.setText(dresstxt);
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




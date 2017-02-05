package com.lcpdev.yourweather;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.lcpdev.yourweather.util.HttpUtil;
import com.lcpdev.yourweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.lcpdev.yourweather.R.layout.forecast;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView scrWeatherLayout;
    private TextView tempText;
//    private TextView temp_Min;
//    private TextView temp_Max;
    private TextView weatherInfoText;
    private TextView pm25Text;
    private TextView aqiText;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private LinearLayout forecastLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //初始化控件 加载信息
        initView();

    }

    private void initView() {
        scrWeatherLayout = (ScrollView) findViewById(R.id.weather_scrollView);
        tempText = (TextView) findViewById(R.id.temp);
//        temp_Min= (TextView) findViewById(R.id.min_Text);
//        temp_Max= (TextView) findViewById(R.id.max_Text);
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
            Log.d("Weather","weather");
        } else {
            //无缓存时去服务器查询天气
            String weatherId = getIntent().getStringExtra("weather_id");
            scrWeatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
            Log.d("weatherID2",weatherId);
        }

    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String temp = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        tempText.setText(temp);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastsList){
            View view = LayoutInflater.from(this).inflate(R.layout.item_forecast,forecastLayout,false);
            TextView dateText= (TextView) view.findViewById(R.id.date_Text);
            TextView infoText = (TextView) view.findViewById(R.id.info_Text);
            TextView minText= (TextView) view.findViewById(R.id.min_Text);
            TextView maxText= (TextView) view.findViewById(R.id.max_Text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            minText.setText(forecast.temperature.min+"℃");
            maxText.setText(forecast.temperature.max+"℃");
            forecastLayout.addView(view);
        }
//            aqiText.setText(weather.aqi.city.aqi);
//            pm25Text.setText(weather.aqi.city.pm25);
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
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=342a3bf415f84fc7ba09cf90e66fcee1";
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
//                Log.d("responseText",responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("status","status");
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


    }




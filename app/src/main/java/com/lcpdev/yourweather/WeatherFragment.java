package com.lcpdev.yourweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static com.lcpdev.yourweather.R.id.airQlty;

/**
 * Created by linchupeng on 2017/2/14.
 * github:https://github.com/linchupeng/YourWeather
 */

public class WeatherFragment extends Fragment {

    private ScrollView scrWeatherLayout;
    private TextView tempText;
    private ImageView imgWeather;
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
//    public SwipeRefreshLayout swipeRefresh;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.activity_weather,container,false);
        scrWeatherLayout = (ScrollView)view.findViewById(R.id.weather_scrollView);
//        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        tempText = (TextView) view.findViewById(R.id.temp);
        imgWeather= (ImageView)view.findViewById(R.id.img_cond);
        weatherInfoText = (TextView)view.findViewById(R.id.weather_info);
        hourforecastLayout= (LinearLayout)view.findViewById(R.id.hour_layout);
        forecastLayout = (LinearLayout)view.findViewById(R.id.forecast_layout);
        aqiText = (TextView)view.findViewById(R.id.aqi_text);
        airQlty= (TextView)view.findViewById(R.id.airQlty);
        pm25Text = (TextView)view.findViewById(R.id.pm2_5);
        airBrf = (TextView)view.findViewById(R.id.sug_air);
        comfBrf= (TextView)view.findViewById(R.id.sug_comf);
        fluBrf= (TextView)view.findViewById(R.id.sug_flu);
        drsgBrf= (TextView)view.findViewById(R.id.sug_drsg);
        airTxt = (TextView)view.findViewById(R.id.air_txt);
        comfortTxt = (TextView)view.findViewById(R.id.comf_txt);
        influenzaTxt = (TextView)view.findViewById(R.id.flu_txt);
        dressTxt = (TextView)view.findViewById(R.id.drsg_txt);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String weatherString = prefs.getString("Weather", null);
//        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark,App));


//        final String swipeWeatherId;
        if (weatherString != null) {
            //有缓存时直接解析天气
            Weather weather = Utility.handleWeatherResponse(weatherString);
//            swipeWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            //无缓存时去服务器查询天气
            String weatherId = (String) getArguments().get("weather_id");
//            String swipeWeatherId =(String) getArguments().get("weather_id");
//            String weatherId = getIntent().getStringExtra("weather_id");
            scrWeatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
            Log.d("weatherActivity_ID",weatherId);
        }
//        swipeRefresh.setOnClickListener(new SwipeRefreshLayout.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//              requestWeather(swipeWeatherId);
//            }
//        });


        return view;
    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String temp = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        int imgCode = weather.now.more.code;
        tempText.setText(temp);
//        titleCityName.setText(cityName);
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

        hourforecastLayout.removeAllViews();
        for (HourForecast hourForecast:weather.hourForecastList){
            View viewHour =LayoutInflater.from(getActivity()).inflate(R.layout.item_hour,hourforecastLayout,false);
            TextView hourText = (TextView) viewHour.findViewById(R.id.hour_clock);
            TextView tmpText = (TextView) viewHour.findViewById(R.id.hour_temp);
            TextView humText = (TextView) viewHour.findViewById(R.id.hour_hum);
            TextView windText = (TextView) viewHour.findViewById(R.id.hour_wind);
            //去年月日保留时间
            String Hour = hourForecast.date;
            String hourWeather = Common.getHour(Hour);
            hourText.setText(hourWeather);
            tmpText.setText(hourForecast.tmp+"℃");
            humText.setText(hourForecast.hum+"%");
            windText.setText(hourForecast.wind.spd+"Km/h");
            hourforecastLayout.addView(viewHour);
        }
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastsList){
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_forecast,forecastLayout,false);
            TextView dateText= (TextView) view.findViewById(R.id.date_Text);
            TextView infoText = (TextView) view.findViewById(R.id.info_Text);
            ImageView forecastImg = (ImageView) view.findViewById(R.id.foreDayWeather);
            TextView minText= (TextView) view.findViewById(R.id.min_Text);
            TextView maxText= (TextView) view.findViewById(R.id.max_Text);
            String WeatherDate=forecast.date;
            //日期转换星期
            String weekDate = Common.getDate(WeatherDate);
            dateText.setText(weekDate);
//            int foreCode  = forecast.more.foreCode;
//            if (foreCode >= 100 && foreCode <= 103  ){
//                forecastImg.setImageResource(R.mipmap.foredaysun);
//            }
//            if ((foreCode >= 104 && foreCode <= 213)||(foreCode >= 500 && foreCode <= 502)){
//                forecastImg.setImageResource(R.mipmap.foredaycloud);
//            }
//            if (foreCode >= 300 && foreCode <= 313 ){
//                forecastImg.setImageResource(R.mipmap.foredayrain);
//            }
//            if (foreCode >= 400 && foreCode <=407 ){
//                forecastImg.setImageResource(R.mipmap.foredaysnow);
//            }
//            if (foreCode >=503 && foreCode <= 508){
//                forecastImg.setImageResource(R.mipmap.foredaysand);
//            }
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
    public void requestWeather( String weatherId) {
        String weatherUrl = "https://api.heweather.com/v5/weather?city="+weatherId+"&key=342a3bf415f84fc7ba09cf90e66fcee1";
        Log.i("天气详情", weatherUrl);
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "一不小心获取失败了Q_Q ", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("status", "status");
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(getActivity(), "后台获取天气失败( >﹏< )", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}

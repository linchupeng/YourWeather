package com.lcpdev.yourweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.lcpdev.yourweather.MainActivity;
import com.lcpdev.yourweather.gson.Weather;
import com.lcpdev.yourweather.util.HttpUtil;
import com.lcpdev.yourweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {

    }

    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 6*60*60*1000;//6小时
        long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
        Intent intentService = new Intent(this,AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this,0,intentService,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString  = pref.getString("weather",null);

        if (weatherString!=null){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            String weatherUrl = "https://api.heweather.com/v5/weather?city="+weatherId+"&key=342a3bf415f84fc7ba09cf90e66fcee1";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Weather weather = Utility.handleWeatherResponse(responseText);
                            if (weather != null && "ok".equals(weather.status)) {
                                Log.d("status", "Ok");
                                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                                editor.putString("weather", responseText);
                                editor.apply();
                            }
                        }
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}




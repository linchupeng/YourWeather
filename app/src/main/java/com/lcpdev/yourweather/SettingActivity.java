package com.lcpdev.yourweather;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lcpdev.yourweather.gson.Aqi;
import com.lcpdev.yourweather.gson.Basic;
import com.lcpdev.yourweather.gson.Weather;
import com.lcpdev.yourweather.util.Utility;

import static com.lcpdev.yourweather.R.xml.settingui;

/**
 * Created by LCP on 2017/2/13.
 * @ Email:chuge94@163.com
 * GitHub:https://github.com/linchupeng/YourWeather
 */

public class SettingActivity extends PreferenceActivity {

    private Toolbar mToolbar;
    private AppCompatDelegate delegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        initView();
        setToolbar();
        initFragment();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        delegate.getSupportActionBar().setDisplayShowTitleEnabled(false);
        delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void setSupportActionBar(Toolbar mToolbar) {
        getDelegate().setSupportActionBar(mToolbar);

    }

    private void setToolbar() {
        mToolbar.setTitle("设置");
    }

    private void initFragment() {
        getFragmentManager().beginTransaction().replace(R.id.content,new SettingFragment()).commit();
    }

    public AppCompatDelegate getDelegate() {
        if (delegate == null){
            delegate =AppCompatDelegate.create(this,null);
        }
        return delegate;
    }

    public static class SettingFragment extends PreferenceFragment{
        private Preference mUpdate;
        private  Preference mUpdate_time;
        private CheckBoxPreference mNotification;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(settingui);
            initNotification();
            initUpdate_time();
            initUpdate();
        }




        private void initNotification() {
            SharedPreferences prefs = getActivity().getSharedPreferences("notification",MODE_PRIVATE);
            final String cityName = prefs.getString("cityName","");
            final String temp = prefs.getString("temperature","");
            final String weatherInfo = prefs.getString("weatherInfo","");
            mNotification= (CheckBoxPreference) findPreference("notification");
            mNotification.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (mNotification.isChecked()){
//                        Toast.makeText(getActivity(), "选中", Toast.LENGTH_SHORT).show();
                        NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
                        Notification notification  = builder
                                .setContentTitle(cityName+"-"+weatherInfo)
                                .setContentText("当前温度:"+temp)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.bat)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.bat))
                                .build();
                        manager.notify(1,notification);
                    }else{
//                        Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();
                        NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                        manager.cancelAll();
                    }

                    return true;
                }
            });
        }
        private void initUpdate_time() {
            mUpdate_time=findPreference("update_time");
            mUpdate_time.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogLayout = inflater.inflate(R.layout.update_weather, (ViewGroup) getActivity().findViewById(
                            R.id.dialog_root));
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                            .setView(dialogLayout);
                    final AlertDialog alertDialog = builder.create();

                    final SeekBar mSeekBar = (SeekBar) dialogLayout.findViewById(R.id.time_seekBar);
                    final TextView tvShowHour = (TextView) dialogLayout.findViewById(R.id.tv_showhour);
                    TextView tvDone = (TextView) dialogLayout.findViewById(R.id.done);

                    mSeekBar.setMax(24);
//                    mSeekBar.setProgress(mSharedPreferenceUtil.getAutoUpdate());
//                    tvShowHour.setText(String.format("每%s小时", mSeekBar.getProgress()));
                    alertDialog.show();
                    tvDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                    return false;
                }
            });
        }
        private void initUpdate() {
            mUpdate=findPreference("update_version");
            mUpdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(getActivity(), "当前已是最新版本", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }

    }

}

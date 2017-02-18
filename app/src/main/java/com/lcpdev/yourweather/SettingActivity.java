package com.lcpdev.yourweather;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
        private CheckBoxPreference mNotification;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(settingui);
            initNotification();
            initUpdate();
        }


        private void initNotification() {
            mNotification= (CheckBoxPreference) findPreference("notification");
            mNotification.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                    Notification notification  = new NotificationCompat.Builder(getActivity())
                            .setContentTitle("你的天气")
                            .setContentText("厦门")
                            .setWhen(System.currentTimeMillis())
                            .build();
                    manager.notify(1,notification);
                    Toast.makeText(getActivity(), "通知栏", Toast.LENGTH_SHORT).show();
                    return true;
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

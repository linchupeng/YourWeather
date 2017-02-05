
package com.lcpdev.yourweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;



import static com.lcpdev.yourweather.R.id.masked;

public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar =getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        }
//        navigationView.setCheckedItem(R.id.add_city);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {
            if (!DoubleClickExit.check()) {
                Toast.makeText(MainActivity.this,"再按一次退出",Toast.LENGTH_SHORT).show();
            }else {
                finish();
            }
        }

    }
}

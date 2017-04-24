package com.lcpdev.yourweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lcpdev.yourweather.database.City;
import com.lcpdev.yourweather.database.County;
import com.lcpdev.yourweather.database.Province;
import com.lcpdev.yourweather.util.HttpUtil;
import com.lcpdev.yourweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by LCP on 2017/1/20.
 * @ Email:chuge94@163.com
 * GitHub:https://github.com/linchupeng/YourWeather
 */
public class ChooseCity extends BaseActivity {
    private RecyclerView recyclerView;
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
//    private TextView titleText;
    private ProgressDialog progressDialog;
    private List<String> dataList = new ArrayList<>();
    private CityAdapter mCityAdapter;
    /**
     * 省级 ，市级，县级列表
     */
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    /**
     * 选中的 省,市
     */
    private Province selectedProvince;
    private City selectedCity;
    /**
     * 当前选中的级别
     */
    private int currentLeveL;
    private Toolbar toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_city);
        initView();
        initToolbar();
        Log.d("LifeCycle","ChooseActivity_onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LifeCycle","ChooseActivity_onStart");
    }

    @Override
    protected void onResume() {
        super.onPostResume();
        Log.d("LifeCycle","ChooseActivity_onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LifeCycle","ChooseActivity_onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LifeCycle","ChooseActivity_onStop");
    }

    @Override
    public void onCreateSupportNavigateUpTaskStack(@NonNull TaskStackBuilder builder) {
        super.onCreateSupportNavigateUpTaskStack(builder);
    }

    private void initToolbar() {
        toolbarTitle = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarTitle);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbarTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLeveL ==LEVEL_PROVINCE){
                    finish();
                }
                else if (currentLeveL == LEVEL_COUNTY) {
                    queryCity();
                } else if (currentLeveL == LEVEL_CITY) {
                    queryProvince();
                }
            }
        });
        queryProvince();
    }
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mCityAdapter = new CityAdapter(dataList);
        recyclerView.setAdapter(mCityAdapter);
        mCityAdapter.setOnItemClickLitener(new CityAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (currentLeveL == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    Log.d("TAG", "QueryCity");
                    queryCity();
                } else if (currentLeveL == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounty();
                }else if (currentLeveL == LEVEL_COUNTY){
                    String weatherId = countyList.get(position).getWeatherId();
                    Intent intent =new Intent(ChooseCity.this,MainActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);
                    finish();
                }
            }
        });

//        queryProvince();
    }
    /**
     * 查询省份
     */
    private void queryProvince() {
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            mCityAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(0);
            currentLeveL = LEVEL_PROVINCE;
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }
    /**
     * 查询城市
     */
    private void queryCity() {
        toolbarTitle.setTitle(selectedProvince.getProvinceName());
        cityList = DataSupport.where("provinceid=?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            mCityAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(0);
            currentLeveL = LEVEL_CITY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    } /**
     * 查询县城
     */
    private void queryCounty() {
        toolbarTitle.setTitle(selectedCity.getCityName());
        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            mCityAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(0);
            currentLeveL = LEVEL_COUNTY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            Log.d("TAG", String.valueOf(cityCode));
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
            Log.d("queryCounty",address);

        }
    }
    /**
     * 在queryFromServer()方法中调用了HTTPsendOkHttpRequest()方法向服务器请求数据
     * 相应的数据会回调到onResponse()方法中 
     */
    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseCity.this, "加载失败...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //在onResponse()方法中 调用Utility.handleProvinceResponse()进行数据解析和处理

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, selectedCity.getId());
                }
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvince();
                            } else if ("city".equals(type)) {
                                queryCity();
                            } else if ("county".equals(type)) {
                                queryCounty();
                            }
                        }
                    });
                }
            }
        });
    }
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ChooseCity.this);
            progressDialog.setMessage("正在加载.....");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (currentLeveL ==LEVEL_PROVINCE){
            finish();
        }
        else if (currentLeveL == LEVEL_COUNTY) {
            queryCity();
        } else if (currentLeveL == LEVEL_CITY) {
            queryProvince();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LifeCycle","ChooseActivity_onDestroy");
    }
}




package com.lcpdev.yourweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
 * Created by 林楚鹏 on 2017/1/20.
 */

public class ChooseCity extends BaseActivity {
    private RecyclerView recyclerView;
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private TextView titleText;
    private Button backBtn;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_city);
        initView();
    }

    private void initView() {
        titleText = (TextView) findViewById(R.id.title_text);
        backBtn = (Button) findViewById(R.id.back_btn);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                    Intent intent =new Intent(ChooseCity.this,WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    Log.d("weatherID",weatherId);
                    startActivity(intent);

                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLeveL == LEVEL_COUNTY) {
                    queryCity();
                } else if (currentLeveL == LEVEL_CITY) {
                    queryProvince();
                }
            }
        });
        queryProvince();
    }

    private void queryProvince() {
        titleText.setText("中国");
        backBtn.setVisibility(View.GONE);
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

    private void queryCity() {
        titleText.setText(selectedProvince.getProvinceName());
        backBtn.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid=?", String.valueOf(selectedProvince.getId())).find(City.class);
        Log.d("TAG", "Ok");
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
            Log.d("TAG", "Ok1");
        }
    }

    private void queryCounty() {
        titleText.setText(selectedCity.getCityName());
        backBtn.setVisibility(View.VISIBLE);
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
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

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
}




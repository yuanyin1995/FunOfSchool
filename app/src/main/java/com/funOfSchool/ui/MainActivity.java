package com.funOfSchool.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.funOfSchool.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    /*  地图控件 */
    TextureMapView mMapView = null;
    /*  地图实例 */
    private BaiduMap mBaiduMap;
    /*  定位的客户端 */
    private LocationClient mLocationClient;
    /*  定位的监听器 */
    public MyLocationListener mMyLocationListener;
    /*  当前定位的模式 */
    private MyLocationConfiguration.LocationMode mCurrentMode
            = MyLocationConfiguration.LocationMode.NORMAL;
    /*  是否是第一次定位 */
    private volatile boolean isFristLocation = true;
    /*  最新一次的经纬度 */
    private double mCurrentLantitude;
    private double mCurrentLongitude;

    /* 当前学校的经纬度 */
    private double collegeLantitude;
    private double collegeLongitude;

    /*  搜索栏 */
    private AutoCompleteTextView etSearch;
    /*  按钮  */
    private ImageView btnStatus;
    /*  学校名称列表  */
    private List<String> collegeNameList = new ArrayList<String>();
    /*  所选学校名称  */
    String collegeName;
    private ArrayAdapter<String> ada;
    /*  消息  */
    private ImageView btnMsg;
    //侧拉菜单对象
    DrawerLayout drawerLayout;
    //个人中心按钮
    private ImageView btnme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  在使用SDK 各组件之前初始化context 信息，传入ApplicationContext
        //  注意该方法要再setContentView 方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //  初始化 BaiduMap 相关
        initBaiduMap();
        //  初始化百度定位客户端
        initMyLocation();
        //  获得各控件
        findView();
        //  搜索大学
        searchCollege();
    }


    private void searchCollege() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnStatus.setImageResource(R.mipmap.invite_index);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnStatus.setImageResource(R.mipmap.invite_index);
                // 根据关键词获取学校下拉列表
                AsyncHttpClient client = new AsyncHttpClient();
                String url = "http://192.168.178.2/api/college/searchCollege";
                // 请求参数：关键词
                RequestParams param = new RequestParams();
                param.put("keyWord",etSearch.getText());
                // 发送网络请求
                client.post(url, param, new JsonHttpResponseHandler() {
                    // 发出网络请求前绑定adapter
                    @Override
                    public void onStart() {
                        super.onStart();
                        ada = new ArrayAdapter<String>(
                                getApplicationContext(),
                                R.layout.college_dropdown_item,
                                collegeNameList);
                        etSearch.setAdapter(ada);
                        ada.notifyDataSetChanged();
                    }

                    @Override
                    public void onSuccess(int i, Header[] headers, JSONObject response) {
                        Log.e("SUCCESS","发送成功!");
                        // response为返回的JSON对象
                        Log.e("Response:", response.toString());

                        JSONObject collegeNameListJO = null;
                        try {
                            // 获取JSONObject
                            collegeNameListJO = new JSONObject(response.toString());
                            // 获取JSONArray
                            JSONArray collegeNameListJA = collegeNameListJO.getJSONArray("datum");
                            // 给学校名列表赋值
                            for (int j=0; j<collegeNameListJA.length();j++){
                                collegeNameList.add(j,collegeNameListJA.getJSONObject(j).getString("name"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // 更新列表
                        ada.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnStatus.setImageResource(R.mipmap.invite_index);
                // 获取最终学校名称
                collegeName = etSearch.getText().toString();
                // 清除上次请求的学校
                collegeNameList.clear();
            }
        });

        // 设置学校下拉列表项点击监听
        etSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // 根据学校名称，获得所选学校的经纬度
                AsyncHttpClient client = new AsyncHttpClient();
                String url = "http://192.168.178.2/api/college/searchLaAndLo";
                // 请求参数：学校名称
                RequestParams param = new RequestParams();
                param.put("collegeName",collegeName);
                // 发送网络请求
                client.post(url, param, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.e("SUCCESS","发送成功!");
                        Log.e("RC",response.toString());

                        JSONObject collegeNameJO = null;
                        try {
                            // 获取JSONObject
                            collegeNameJO = new JSONObject(response.toString());
                            // 获取JSONArray
                            JSONArray collegeNameJA = collegeNameJO.getJSONArray("datum");
                            // 给所选学校经纬度赋值
                            for (int j=0; j<collegeNameJA.length();j++){
                                collegeLantitude = collegeNameJA.getJSONObject(j).getDouble("lantitude");
                                collegeLongitude = collegeNameJA.getJSONObject(j).getDouble("longitude");
                            }

                            Log.e("LL:",collegeLantitude+"-"+collegeLongitude);

                            // 创建 LatLng 对象
                            LatLng ll = new LatLng(collegeLantitude, collegeLongitude);
                            // 设置所选学校位置为地图中心点，并移动到定位位置
                            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                            mBaiduMap.animateMapStatus(u);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //  改变状态图标
                btnStatus.setImageResource(R.mipmap.invite);
            }
        });
    }


    private void findView() {
        etSearch = (AutoCompleteTextView)findViewById(R.id.map_et_search);
        btnStatus = (ImageView)findViewById(R.id.map_change_status);
        btnMsg = (ImageView) findViewById(R.id.map_msg);
        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
        btnme = (ImageView) findViewById(R.id.me);
        btnme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    /**
     *  初始化百度地图
     */
    private void initBaiduMap() {
        //  获取地图控件引用
        mMapView = (TextureMapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //  设置比例尺
        MapStatusUpdate msu =
                MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
    }

    /**
     *  实现实位回调监听
     */
    public class MyLocationListener implements
            BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //  mapView  销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            //  构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    //  此处设置开发者获取到的方向信息，顺时针 0-360
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            //  设置 BaiduMap 的定位数据
            mBaiduMap.setMyLocationData(locData);
            //  记录位置信息
            mCurrentLantitude = location.getLatitude();
            mCurrentLongitude = location.getLongitude();
            //  第一次定位时，将地图位置移动到当前位置
            if (isFristLocation) {
                isFristLocation = false;
                center2myLoc();
            }
        }
    }

    /**
     *  初始化定位相关代码
     */
    private void initMyLocation() {
        //  定位 SDK 初始化
        mLocationClient = new
                LocationClient(getApplicationContext());
        //  设置定位的相关配置
        LocationClientOption option = new
                LocationClientOption();
        option.setOpenGps(true); //  打开 gps
        option.setCoorType("bd09ll"); //  设置坐标类型
        option.setScanSpan(1000); //  自动定位间隔
        option.setIsNeedAddress(true);//  是否需要地址
        option.setIsNeedLocationPoiList(true);
        //  定位模式
        option.setLocationMode(LocationClientOption.LocationMode.
                Hight_Accuracy);
        //  根据配置信息对定位客户端进行设置
        mLocationClient.setLocOption(option);
        //  注册定位监听
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
    }

    /**
     * BaiduMap 移动到我的位置
     */
    private void center2myLoc() {
        LatLng ll = new LatLng(mCurrentLantitude,
                mCurrentLongitude);
        //  设置当前定位位置为 BaiduMap 的中心点，并移动到定位位置
        MapStatusUpdate u =
                MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(u);
    }

    @Override
    protected void onStart() {
        //  开启图层定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
        {
            mLocationClient.start();
        }
        super.onStart();
    }
    @Override
    protected void onStop() {
        //  关闭图层定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  在activity 执行onDestroy 时执行mMapView.onDestroy() ，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //  在activity 执行onResume 时执行mMapView. onResume () ，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //  在activity 执行onPause 时执行mMapView. onPause () ，实现地图生命周期管理
        mMapView.onPause();
    }
}
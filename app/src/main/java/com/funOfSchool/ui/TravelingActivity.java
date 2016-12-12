package com.funOfSchool.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class TravelingActivity extends AppCompatActivity {

    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    //  是否是第一次定位
    private volatile boolean isFristLocation = true;
    //  最新一次的经纬度
    private double mCurrentLantitude;
    private double mCurrentLongitude;
    //  回退按钮
    private ImageView btnTravelingBack;
    //  结束旅程按钮
    private ImageView btnTravelingEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  在使用SDK 各组件之前初始化context 信息，传入ApplicationContext
        //  注意该方法要再setContentView 方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_traveling);

        //  初始化 BaiduMap 相关
        initBaiduMap();
        //  初始化百度定位客户端
        initMyLocation();
        //  获得各控件
        findView();
        //  为各按钮设置监听器
        setListener();
    }

    private void findView() {
        btnTravelingBack = (ImageView)findViewById(R.id.travling_back);
        btnTravelingEnd = (ImageView)findViewById(R.id.traveling_end);
    }

    private void setListener() {
        TravelingListener travelingListener = new TravelingListener();
        btnTravelingBack.setOnClickListener(travelingListener);
        btnTravelingEnd.setOnClickListener(travelingListener);
    }

    private class TravelingListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.traveling_end:
                    endTravel();
                    break;
            }
        }
    }


    /**
     * 用户点击结束旅程后，弹出对话框让用户二次确认
     */
    private void endTravel() {
        //  弹出框：确认开始旅程
        AlertDialog.Builder AdBuilder =
                new AlertDialog.Builder(TravelingActivity.this);
        AdBuilder.setMessage(R.string.confirm_end_travel);

        AdBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //  弹出框：是否发表评价
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(TravelingActivity.this);
                builder.setMessage(R.string.choose_comment);

                builder.setPositiveButton(R.string.to_comment, new DialogInterface.OnClickListener() {
                    // 选择是，进入评价页面
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toEvaluatePage();
                    }
                });
                builder.setNegativeButton(R.string.not_comment, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  返回上一页
                        TravelingActivity.this.finish();
                    }
                });
                builder.create();
                builder.show();
            }
        });
        AdBuilder.setNegativeButton(R.string.no, null);
        AdBuilder.create();
        AdBuilder.show();
    }

    /**
     * 用户确认发表评价时，进入评价页面
     */
    private void toEvaluatePage() {
        Intent intent =
                new Intent(TravelingActivity.this,EvaluateActivity.class);
        startActivity(intent);
    }


    /**
     *  初始化百度地图
     */
    private void initBaiduMap() {
        //  获取地图控件引用
        mMapView = (TextureMapView) findViewById(R.id.traveling_mapView);
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

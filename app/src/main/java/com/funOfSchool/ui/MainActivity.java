package com.funOfSchool.ui;

import android.app.Activity;
import android.os.Bundle;

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
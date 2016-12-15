package com.funOfSchool.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.LocationMode;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.Trace;
import com.baidu.trace.TraceLocation;
import com.funOfSchool.R;
import com.funOfSchool.util.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    //  轨迹服务ID
    long serviceId = 131124;
    //  设备名称
    String entityName = "myTrace";
    //  traceType = 2 : 建立socket长连接并上传位置数据
    int traceType = 2;

    // 采集周期
    int gatherInterval = 5;
    // 打包周期
    int packInterval = 10;
    // http协议类型
    int protocolType = 1;

    //  轨迹服务
    Trace mTrace = null;
    //  轨迹服务客户端
    private LBSTraceClient mTraceClient = null;

    //  开启轨迹服务监听器
    protected static OnStartTraceListener startTraceListener = null;

    //  停止轨迹服务监听器
    protected static OnStopTraceListener stopTraceListener = null;

    //  Entity监听器
    private static OnEntityListener entityListener = null;

    //  覆盖物
    private static Overlay overlay = null;
    protected static OverlayOptions overlayOptions;

    // 图标
    private static BitmapDescriptor realtimeBitmap;

    //  路线覆盖物
    private static PolylineOptions polyline = null;

    //  采集点集合
    private static List<LatLng> pointList = new ArrayList<LatLng>();

    /**
     * 刷新地图线程(获取实时点)
     */
    protected RefreshThread refreshThread = null;

    protected static MapStatusUpdate msUpdate = null;

    private boolean isTraceStarted = true;


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
        // initMyLocation();
        //  初始化鹰眼轨迹相关
        initMyTrace();
        // 初始化监听器
        initListener();
        //  获得各控件
        findView();
        //  为各按钮设置监听器
        setListener();

        startRefreshThread(true);
        Toast.makeText(getApplicationContext(),
                "正在开启轨迹服务，请稍候",
                Toast.LENGTH_LONG).show();
        startTrace();
    }

    /**
     * 初始化鹰眼轨迹相关
     */
    private void initMyTrace() {
        // 初始化轨迹服务
        mTrace = new Trace(getApplicationContext(), serviceId, entityName, traceType);

        // 实例化mTraceClient
        mTraceClient = new LBSTraceClient(getApplicationContext());

        // 设置采集和打包周期
        mTraceClient.setInterval(gatherInterval, packInterval);
        // 设置定位模式
        mTraceClient.setLocationMode(LocationMode.High_Accuracy);
        // 设置http协议类型
        mTraceClient.setProtocolType (protocolType);
    }


    protected void startRefreshThread(boolean isStart) {
        if (null == refreshThread) {
            refreshThread = new RefreshThread();
        }
        refreshThread.refresh = isStart;
        if (isStart) {
            if (!refreshThread.isAlive()) {
                Toast.makeText(getApplicationContext(),
                        "thread start",
                        Toast.LENGTH_LONG).show();
                refreshThread.start();
            }
        } else {
            refreshThread = null;
        }
    }


    /**
     * 开启轨迹服务
     */
    private void startTrace() {
        // 通过轨迹服务客户端client开启轨迹服务
        mTraceClient.startTrace(mTrace, startTraceListener);
    }

    /**
     * 停止轨迹服务
     */
    private void stopTrace() {
        // 通过轨迹服务客户端client停止轨迹服务
        mTraceClient.stopTrace(mTrace, stopTraceListener);
    }


    /**
     * 初始化OnStartTraceListener
     */
    private void initOnStartTraceListener() {
        // 初始化startTraceListener
        startTraceListener = new OnStartTraceListener() {

            // 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
            public void onTraceCallback(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),
                        "开启轨迹服务回调接口消息 [消息编码 : " + arg0 + "，消息内容 : " + arg1 + "]",
                        Toast.LENGTH_LONG).show();
            }

            // 轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
            public void onTracePushCallback(byte arg0, String arg1) {
                // TODO Auto-generated method stub
                if (0x03 == arg0 || 0x04 == arg0) {
                    try {
                        JSONObject dataJson = new JSONObject(arg1);
                        if (null != dataJson) {
                            String mPerson = dataJson.getString("monitored_person");
                            String action = dataJson.getInt("action") == 1 ? "进入" : "离开";
                            String date = DateUtils.getDate(dataJson.getInt("time"));
                            long fenceId = dataJson.getLong("fence_id");

                            Toast.makeText(getApplicationContext(),
                                    "监控对象[" + mPerson + "]于" + date + " [" + action + "][" + fenceId + "号]围栏",
                                    Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(getApplicationContext(),
                                "轨迹服务推送接口消息 [消息类型 : " + arg0 + "，消息内容 : " + arg1 + "]",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "轨迹服务推送接口消息 [消息类型 : " + arg0 + "，消息内容 : " + arg1 + "]",
                            Toast.LENGTH_LONG).show();
                    // mHandler.obtainMessage(-1, "轨迹服务推送接口消息 [消息类型 : " + arg0 + "，消息内容 : " + arg1 + "]").sendToTarget();
                }
            }
        };
    }

    /**
     * 初始化OnStopTraceListener
     */
    private void initOnStopTraceListener() {
        // 初始化stopTraceListener
        stopTraceListener = new OnStopTraceListener() {

            // 轨迹服务停止成功
            public void onStopTraceSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),
                        "停止轨迹服务成功",
                        Toast.LENGTH_LONG).show();

                startRefreshThread(false);
                mTraceClient.onDestroy();
            }

            // 轨迹服务停止失败（arg0 : 错误编码，arg1 : 消息内容，详情查看类参考）
            public void onStopTraceFailed(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),
                        "停止轨迹服务接口消息 [错误编码 : " + arg0 + "，消息内容 : " + arg1 + "]",
                        Toast.LENGTH_LONG).show();

                startRefreshThread(false);
            }
        };
    }

    /**
     * 初始化OnEntityListener
     */
    private void initOnEntityListener() {
        entityListener = new OnEntityListener() {

            // 请求失败回调接口
            @Override
            public void onRequestFailedCallback(String arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),
                        "entity请求失败回调接口消息 : " + arg0,
                        Toast.LENGTH_LONG).show();
                // trackApp.getmHandler().obtainMessage(0, "entity请求失败回调接口消息 : " + arg0).sendToTarget();
            }

            // 添加entity回调接口
            public void onAddEntityCallback(String arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),
                        "添加entity回调接口消息 : " + arg0,
                        Toast.LENGTH_LONG).show();
                // trackApp.getmHandler().obtainMessage(0, "添加entity回调接口消息 : " + arg0).sendToTarget();
            }

            // 查询entity列表回调接口
            @Override
            public void onQueryEntityListCallback(String message) {
                // TODO Auto-generated method stub
                TraceLocation entityLocation = new TraceLocation();
                try {
                    JSONObject dataJson = new JSONObject(message);
                    if (null != dataJson && dataJson.has("status") && dataJson.getInt("status") == 0
                            && dataJson.has("size") && dataJson.getInt("size") > 0) {
                        JSONArray entities = dataJson.getJSONArray("entities");
                        JSONObject entity = entities.getJSONObject(0);
                        JSONObject point = entity.getJSONObject("realtime_point");
                        JSONArray location = point.getJSONArray("location");
                        entityLocation.setLongitude(location.getDouble(0));
                        entityLocation.setLatitude(location.getDouble(1));
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(),
                            "解析entityList回调消息失败",
                            Toast.LENGTH_LONG).show();
                    // trackApp.getmHandler().obtainMessage(0, "解析entityList回调消息失败").sendToTarget();
                    return;
                }
                showRealtimeTrack(entityLocation);
            }

            @Override
            public void onReceiveLocation(TraceLocation location) {
                // TODO Auto-generated method stub
                showRealtimeTrack(location);
            }
        };
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        // 初始化开启轨迹服务监听器
        if (null == startTraceListener) {
            initOnStartTraceListener();
        }

        // 初始化停止轨迹服务监听器
        if (null == stopTraceListener) {
            initOnStopTraceListener();
        }

        // 初始化entity监听器
        if (null == entityListener) {
            initOnEntityListener();
        }
    }


    /**
     * 显示实时轨迹
     *
     * @param location
     */
    protected void showRealtimeTrack(TraceLocation location) {

        if (null == refreshThread || !refreshThread.refresh) {
            return;
        }

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        if (Math.abs(latitude - 0.0) < 0.000001 && Math.abs(longitude - 0.0) < 0.000001) {
            Toast.makeText(getApplicationContext(),
                    "当前查询无轨迹点",
                    Toast.LENGTH_LONG).show();
        } else {
            LatLng latLng = new LatLng(latitude, longitude);
            if (1 == location.getCoordType()) {
                LatLng sourceLatLng = latLng;
                CoordinateConverter converter = new
                        CoordinateConverter();
                converter.from(CoordinateConverter.CoordType.GPS);
                converter.coord(sourceLatLng);
                latLng = converter.convert();
            }
            pointList.add(latLng);

            // 绘制实时点
            drawRealtimePoint(latLng);
        }
    }

    /**
     * 绘制实时点
     *
     * @param point
     */
    private void drawRealtimePoint(LatLng point) {

        if (null != overlay) {
            overlay.remove();
        }

        MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(19).build();

        msUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);

        if (null == realtimeBitmap) {
            realtimeBitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.icon_gcoding);
        }

        overlayOptions = new MarkerOptions().position(point)
                .icon(realtimeBitmap).zIndex(9).draggable(true);

        if (pointList.size() >= 2 && pointList.size() <= 10000) {
            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(10)
                    .color(R.color.colorPrimary).points(pointList);
        }

        addMarker();
    }

    /**
     * 添加地图覆盖物
     */
    protected void addMarker() {

        if (null != msUpdate) {
            mBaiduMap.setMapStatus(msUpdate);
        }

        // 路线覆盖物
        if (null != polyline) {
            mBaiduMap.addOverlay(polyline);
        }

        // 实时点覆盖物
        if (null != overlayOptions) {
            overlay = mBaiduMap.addOverlay(overlayOptions);
        }
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
                stopTrace();
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
                        //TravelingActivity.this.finish();
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

    /**
     * 查询实时轨迹
     */
    private void queryRealtimeLoc() {
        mTraceClient.queryRealtimeLoc(serviceId, entityListener);
    }

    /**
     * 查询entityList
     */
    private void queryEntityList() {
        // 属性名称（格式为 : "key1=value1,key2=value2,....."）
        String columnKey = "";
        // 返回结果的类型（0 : 返回全部结果，1 : 只返回entityName的列表）
        int returnType = 0;
        // 活跃时间（指定该字段时，返回从该时间点之后仍有位置变动的entity的实时点集合）
        int activeTime = (int) (System.currentTimeMillis() / 1000 - packInterval);
        // 分页大小
        int pageSize = 10;
        // 分页索引
        int pageIndex = 1;

        mTraceClient.queryEntityList(serviceId, entityName, columnKey, returnType, activeTime,
                pageSize,
                pageIndex, entityListener);
    }

    /**
     * 刷新线程
     */
    protected class RefreshThread extends Thread {

        protected boolean refresh = true;

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Looper.prepare();
            while (refresh) {
                // 轨迹服务开启成功后，调用queryEntityList()查询最新轨迹；
                // 未开启轨迹服务时，调用queryRealtimeLoc()进行实时定位。
                if (isTraceStarted) {
                    Toast.makeText(getApplicationContext(),
                            "trace started",
                            Toast.LENGTH_LONG).show();
                    queryEntityList();
                } else {
                    queryRealtimeLoc();
                }

                try {
                    Thread.sleep(gatherInterval * 1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    System.out.println("线程休眠失败");
                }
            }
            Looper.loop();
        }
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

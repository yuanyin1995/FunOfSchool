package com.funOfSchool.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.LocationMode;
import com.baidu.trace.OnTrackListener;
import com.baidu.trace.Trace;
import com.funOfSchool.R;
import com.funOfSchool.util.DateUtils;
import com.funOfSchool.util.GsonService;
import com.funOfSchool.util.HistoryTrackData;
import com.funOfSchool.util.TitleHeight;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryTraceActivity extends AppCompatActivity {
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    //  标题栏
    private RelativeLayout rlTitle;
    //  回退按钮
    private ImageView btnQueryTraceBack;
    //  游玩时间
    String travelDate;
    //  轨迹服务ID
    long serviceId = 131124;
    //  设备名称
    String entityName = "myTrace";
    // 轨迹服务
    Trace mTrace;
    // 轨迹服务客户端
    LBSTraceClient mTraceClient;

    // 路径开始结束时间
    private int startTime = 0;
    private int endTime = 0;

    private int year = 0;
    private int month = 0;
    private int day = 0;

    // 起点图标
    private static BitmapDescriptor bmStart;
    // 终点图标
    private static BitmapDescriptor bmEnd;

    // 起点图标覆盖物
    private static MarkerOptions startMarker = null;
    // 终点图标覆盖物
    private static MarkerOptions endMarker = null;
    // 路线覆盖物
    public static PolylineOptions polyline = null;

    private static MarkerOptions markerOptions = null;

    // Track监听器
    protected static OnTrackListener trackListener = null;

    private MapStatusUpdate msUpdate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_query_trace);

        // 设置标题栏高度
        setTitleHeight();

        //  得到游玩时间
        getTravelTime();

        // 初始化地图配置
        initBaiduMap();

        // 初始化鹰眼轨迹相关
        initMyTrace();

        // 初始化OnTrackListener
        initOnTrackListener();

        // 查询轨迹
        queryTrack();
    }

    /**
     * 获取游玩时间
     */
    private void getTravelTime() {
        Intent i = getIntent();
        travelDate = i.getStringExtra("travelDate");
    }

    /**
     * 设置标题栏高度
     */
    private void setTitleHeight() {
        rlTitle = (RelativeLayout)findViewById(R.id.query_trace_rl_title);
        WindowManager wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int wHeight = wm.getDefaultDisplay().getHeight();
        int tHeight = wHeight /11;
        rlTitle.setMinimumHeight(tHeight);
    }

    /**
     *  初始化百度地图
     */
    private void initBaiduMap() {
        //  获取地图控件引用
        mMapView = (TextureMapView) findViewById(R.id.query_trace_mapView);
        mBaiduMap = mMapView.getMap();
        //  设置比例尺
        MapStatusUpdate msu =
                MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
    }

    /**
     * 初始化鹰眼轨迹相关
     */
    private void initMyTrace() {

        //  traceType = 2 : 建立socket长连接并上传位置数据
        int traceType = 2;

        // 采集周期
        int gatherInterval = 5;
        // 打包周期
        int packInterval = 10;
        // http协议类型
        int protocolType = 1;

        // 初始化轨迹服务
        mTrace = new Trace(getApplicationContext(), serviceId, entityName, traceType);

        // 实例化mTraceClient
        mTraceClient = new LBSTraceClient(getApplicationContext());

        // 设置采集和打包周期
        // mTraceClient.setInterval(gatherInterval, packInterval);
        // 设置定位模式
        mTraceClient.setLocationMode(LocationMode.High_Accuracy);
        // 设置http协议类型
        mTraceClient.setProtocolType (protocolType);
    }


    /**
     * 查询历史轨迹
     */
    private void queryHistoryTrack(String processOption) {

        // 是否返回精简的结果（0 : 否，1 : 是）
        int simpleReturn = 0;
        // 是否返回纠偏后轨迹（0 : 否，1 : 是）
        int isProcessed = 1;
        // 开始时间
        if (startTime == 0) {
            startTime = (int) (System.currentTimeMillis() / 1000 - 12 * 60 * 60);
        }
        if (endTime == 0) {
            endTime = (int) (System.currentTimeMillis() / 1000);
        }
        // 分页大小
        int pageSize = 1000;
        // 分页索引
        int pageIndex = 1;

        mTraceClient.queryHistoryTrack(serviceId, entityName, simpleReturn,
                isProcessed, processOption,
                startTime, endTime,
                pageSize,
                pageIndex,
                trackListener);
    }

    /**
     * 查询里程
     */
    private void queryDistance(String processOption) {

        // 是否返回纠偏后轨迹（0 : 否，1 : 是）
        int isProcessed = 1;

        // 里程补充
        String supplementMode = "driving";

        // 开始时间
        if (startTime == 0) {
            startTime = (int) (System.currentTimeMillis() / 1000 - 12 * 60 * 60);
        }
        // 结束时间
        if (endTime == 0) {
            endTime = (int) (System.currentTimeMillis() / 1000);
        }

        mTraceClient.queryDistance(serviceId, entityName, isProcessed, processOption,
                supplementMode, startTime, endTime, trackListener);
    }

    /**
     * 轨迹查询(先设置日期，再根据纠偏条件发送请求)
     */
    private void queryTrack() {
        // 设置日期
        year = Integer.parseInt(travelDate.substring(0,3));
        month = Integer.parseInt(travelDate.substring(5,6));
        day = Integer.parseInt(travelDate.substring(8,9));

        String st = year + "年" + month + "月" + day + "日0时0分0秒";
        String et = year + "年" + month + "月" + day + "日23时59分59秒";

        startTime = Integer.parseInt(DateUtils.getTimeToStamp(st));
        endTime = Integer.parseInt(DateUtils.getTimeToStamp(et));

        String queryOptions = "need_denoise=1,need_vacuate=1,need_mapmatch=1";
        Toast.makeText(getApplicationContext(),
                "正在查询游玩轨迹，请稍候",
                Toast.LENGTH_SHORT).show();
        queryHistoryTrack(queryOptions);
    }

    /**
     * 显示历史轨迹
     *
     * @param historyTrack
     */
    private void showHistoryTrack(String historyTrack) {

        HistoryTrackData historyTrackData = GsonService.parseJson(historyTrack,
                HistoryTrackData.class);

        List<LatLng> latLngList = new ArrayList<LatLng>();
        if (historyTrackData != null && historyTrackData.getStatus() == 0) {
            if (historyTrackData.getListPoints() != null) {
                latLngList.addAll(historyTrackData.getListPoints());
            }

            // 绘制历史轨迹
            drawHistoryTrack(latLngList, historyTrackData.distance);
        }
    }

    /**
     * 绘制历史轨迹
     *
     * @param points
     */
    private void drawHistoryTrack(final List<LatLng> points, final double distance) {
        // 绘制新覆盖物前，清空之前的覆盖物
        mBaiduMap.clear();

        if (points.size() == 1) {
            points.add(points.get(0));
        }

        if (points == null || points.size() == 0) {
            Toast.makeText(getApplicationContext(),
                    "当前查询无轨迹点",
                    Toast.LENGTH_LONG).show();
            resetMarker();
        } else if (points.size() > 1) {

            LatLng llC = points.get(0);
            LatLng llD = points.get(points.size() - 1);
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(llC).include(llD).build();

            msUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);

            bmStart = BitmapDescriptorFactory.fromResource(R.mipmap.icon_start);
            bmEnd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_end);

            // 添加起点图标
            startMarker = new MarkerOptions()
                    .position(points.get(points.size() - 1)).icon(bmStart)
                    .zIndex(9).draggable(true);

            // 添加终点图标
            endMarker = new MarkerOptions().position(points.get(0))
                    .icon(bmEnd).zIndex(9).draggable(true);

            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(10)
                    .color(R.color.colorPrimary).points(points);

            markerOptions = new MarkerOptions();
            markerOptions.flat(true);
            markerOptions.anchor(0.5f, 0.5f);
            markerOptions.icon(BitmapDescriptorFactory
                    .fromResource(R.mipmap.icon_gcoding));
            markerOptions.position(points.get(points.size() - 1));

            addMarker();

            Toast.makeText(getApplicationContext(),
                    "当前轨迹里程为 : " + (int) distance + "米",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 添加覆盖物
     */
    protected void addMarker() {

        if (null != msUpdate) {
            mBaiduMap.animateMapStatus(msUpdate, 2000);
        }

        if (null != startMarker) {
            mBaiduMap.addOverlay(startMarker);
        }

        if (null != endMarker) {
            mBaiduMap.addOverlay(endMarker);
        }

        if (null != polyline) {
            mBaiduMap.addOverlay(polyline);
        }

    }

    /**
     * 重置覆盖物
     */
    private void resetMarker() {
        startMarker = null;
        endMarker = null;
        polyline = null;
    }

    /**
     * 初始化OnTrackListener
     */
    private void initOnTrackListener() {

        trackListener = new OnTrackListener() {

            // 请求失败回调接口
            @Override
            public void onRequestFailedCallback(String arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),
                        "track请求失败回调接口消息 : " + arg0,
                        Toast.LENGTH_LONG).show();
                // trackApp.getmHandler().obtainMessage(0, "track请求失败回调接口消息 : " + arg0).sendToTarget();
            }

            // 查询历史轨迹回调接口
            @Override
            public void onQueryHistoryTrackCallback(String arg0) {
                // TODO Auto-generated method stub
                super.onQueryHistoryTrackCallback(arg0);
                showHistoryTrack(arg0);
            }

            @Override
            public void onQueryDistanceCallback(String arg0) {
                // TODO Auto-generated method stub
                try {
                    JSONObject dataJson = new JSONObject(arg0);
                    if (null != dataJson && dataJson.has("status") && dataJson.getInt("status") == 0) {
                        double distance = dataJson.getDouble("distance");
                        DecimalFormat df = new DecimalFormat("#.0");
                        Toast.makeText(getApplicationContext(),
                                "里程 : " + df.format(distance) + "米",
                                Toast.LENGTH_LONG).show();
                        // trackApp.getmHandler().obtainMessage(0, "里程 : " + df.format(distance) + "米").sendToTarget();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(),
                            "queryDistance回调消息 : " + arg0,
                            Toast.LENGTH_LONG).show();
                    // trackApp.getmHandler().obtainMessage(0, "queryDistance回调消息 : " + arg0).sendToTarget();
                }
            }

            @Override
            public Map<String, String> onTrackAttrCallback() {
                // TODO Auto-generated method stub
                System.out.println("onTrackAttrCallback");
                return null;
            }

        };
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

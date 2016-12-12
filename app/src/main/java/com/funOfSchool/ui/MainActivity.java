package com.funOfSchool.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    /*  学校名称列表  */
    private List<String> collegeNameList = new ArrayList<String>();
    /*  所选学校名称  */
    String collegeName;
    /*  所选学校ID  */
    int collegeId;
    private ArrayAdapter<String> ada;
    /*  消息  */
    private ImageView btnMsg;
    /*  个人信息 */
    private ImageView btnMe;
    /*  用户当前状态  */
    private int flag;
    //  侧拉菜单对象
    private DrawerLayout drawerLayout;

    //  个人资料、出游记录、卡券包、积分商城、设置
    private LinearLayout btnPersoninfo;
    private LinearLayout btnTravelist;
    private LinearLayout btnPrize;
    private LinearLayout btnMarket;
    private LinearLayout btnSet;

    //  不同状态的按钮
    private ImageView btnCannotInvite;
    private ImageView btnCanInvite;
    private ImageView btnMatchNow;
    private ImageView btnStartTravel;
    private ImageView btnTraveling;
    private ImageView btnEndTravel;

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
        //  根据用户当前状态设置首页的按钮
        setIndexBtn();
        //  为各按钮设置监听器
        setListener();
        //  搜索大学
        searchCollege();
    }

    /**
     * 获取用户当前状态，以设置首页按钮显示内容
     */
    private void setIndexBtn() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://172.16.17.39/api/account/getStatus";
        // 请求参数：关键词
        RequestParams param = new RequestParams();
        param.put("token","e0d734c840414242af709a67eb48fb38543PlI");
        // 发送网络请求
        client.post(url, param, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject flagJO = new JSONObject(response.toString());
                    // 获取JSONArray
                    JSONArray flagJA = flagJO.getJSONArray("datum");
                    // 获取flag
                    flag = flagJA.getJSONObject(0).getInt("flag");
                    Log.e("FLAG",flag+"");

                    switch (flag){
                        case 1:
                            btnCannotInvite.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            btnMatchNow.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            btnStartTravel.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            btnTraveling.setVisibility(View.VISIBLE);
                            break;
                        default:
                            btnCannotInvite.setVisibility(View.VISIBLE);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 根据用户输入的关键词获取大学列表
     */
    private void searchCollege() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnCannotInvite.setVisibility(View.VISIBLE);
                btnCanInvite.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnCannotInvite.setVisibility(View.VISIBLE);
                btnCanInvite.setVisibility(View.INVISIBLE);
                // 根据关键词获取学校下拉列表
                AsyncHttpClient client = new AsyncHttpClient();
                String url = "http://172.16.17.39/api/college/searchCollege";
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
                btnCannotInvite.setVisibility(View.VISIBLE);
                btnCanInvite.setVisibility(View.INVISIBLE);
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
                // 根据学校名称，获得所选学校的经纬度和ID
                AsyncHttpClient client = new AsyncHttpClient();
                String url = "http://10.141.228.233/api/college/searchLaAndLo";
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
                            // 给所选学校经纬度和ID赋值
                            collegeId = collegeNameJA.getJSONObject(0).getInt("coid");
                            collegeLantitude = collegeNameJA.getJSONObject(0).getDouble("lantitude");
                            collegeLongitude = collegeNameJA.getJSONObject(0).getDouble("longitude");

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
                btnCannotInvite.setVisibility(View.INVISIBLE);
                btnCanInvite.setVisibility(View.VISIBLE);
            }
        });
    }


    private void findView() {
        etSearch = (AutoCompleteTextView)findViewById(R.id.map_et_search);
        btnMsg = (ImageView) findViewById(R.id.index_msg);
        btnMe = (ImageView)findViewById(R.id.index_me);
        btnCannotInvite = (ImageView)findViewById(R.id.map_cannot_invite);
        btnCanInvite = (ImageView)findViewById(R.id.map_can_invite);
        btnMatchNow = (ImageView)findViewById(R.id.map_match);
        btnStartTravel = (ImageView)findViewById(R.id.map_start_travel);
        btnTraveling = (ImageView)findViewById(R.id.map_end_travel);
        btnEndTravel = (ImageView)findViewById(R.id.map_end_travel);
        btnPersoninfo = (LinearLayout)findViewById(R.id.me_personinfo);
        btnTravelist = (LinearLayout)findViewById(R.id.me_trvallist);
        btnPrize = (LinearLayout)findViewById(R.id.me_myprize);
        btnMarket = (LinearLayout)findViewById(R.id.me_market);
        btnSet = (LinearLayout)findViewById(R.id.me_set);
    }

    private void setListener() {
        MapListener mapListener = new MapListener();
        btnMe.setOnClickListener(mapListener);
        btnMsg.setOnClickListener(mapListener);
        btnCannotInvite.setOnClickListener(mapListener);
        btnCanInvite.setOnClickListener(mapListener);
        btnMatchNow.setOnClickListener(mapListener);
        btnStartTravel.setOnClickListener(mapListener);
        btnTraveling.setOnClickListener(mapListener);
        btnEndTravel.setOnClickListener(mapListener);
        btnPersoninfo.setOnClickListener(mapListener);
        btnTravelist.setOnClickListener(mapListener);
        btnPrize.setOnClickListener(mapListener);
        btnMarket.setOnClickListener(mapListener);
        btnSet.setOnClickListener(mapListener);
    }

    private class MapListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.index_me:
                    drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
                    drawerLayout.openDrawer(Gravity.LEFT);
                    break;
                case R.id.index_msg:
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    break;
                case R.id.map_cannot_invite:
                    Toast.makeText(MainActivity.this,
                            R.string.cannot_invite_warn,
                            Toast.LENGTH_SHORT).show();
                    break;
                case R.id.map_can_invite:
                    sendInvitation();
                    break;
                case R.id.map_match:
                    Toast.makeText(MainActivity.this,
                            R.string.match_now_warn,
                            Toast.LENGTH_SHORT).show();
                    break;
                case R.id.map_start_travel:
                    startTravel();
                    break;
                case R.id.map_traveling:
                    toTravelingPage();
                    break;
                case R.id.map_end_travel:
                    endTravel();
                    break;
                case R.id.me_personinfo:
                    Toast.makeText(MainActivity.this,"dianjilegeren",Toast.LENGTH_LONG).show();
                    Intent intent_info = new Intent(MainActivity.this,PersonInfoActivity.class);
                    startActivity(intent_info);
                    break;
                case R.id.me_trvallist:
                    Intent intent_Tlist = new Intent(MainActivity.this,TravelListActivity.class);
                    startActivity(intent_Tlist);
                    break;
                case R.id.me_myprize:
                    Intent intent_prz = new Intent(MainActivity.this,MyPrizeActivity.class);
                    startActivity(intent_prz);
                    break;
                case R.id.me_market:
                    Intent intent_mkt = new Intent(MainActivity.this,MarketActivity.class);
                    startActivity(intent_mkt);
                    break;
                case R.id.me_set:
                    Intent intent_st = new Intent(MainActivity.this,SetActivity.class);
                    startActivity(intent_st);
                    break;
            }
        }
    }

    /**
     * 如果当前为可发送邀请状态，则点击按钮可发送邀请
     */
    private void sendInvitation() {
        Intent intent = new Intent(MainActivity.this,SelectActivity.class);
        intent.putExtra("scid",collegeId);
        startActivity(intent);
    }

    /**
     * 用户点击开始旅程后，弹出对话框让用户二次确认
     */
    private void startTravel(){
        //  弹出框：确认开始旅程
        AlertDialog.Builder AdBuilder =
                new AlertDialog.Builder(MainActivity.this);
        AdBuilder.setMessage(R.string.confirm_start_travel);

        AdBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //  弹出框：是否记录路径
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.choose_record_route);

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    // 选择是，进入记录路径页面
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toTravelingPage();
                    }
                });
                builder.setNegativeButton(R.string.no, null);
                builder.create();
                builder.show();
                //  改变按钮显示
                btnStartTravel.setVisibility(View.INVISIBLE);
                btnEndTravel.setVisibility(View.VISIBLE);
            }
        });
        AdBuilder.setNegativeButton(R.string.no, null);
        AdBuilder.create();
        AdBuilder.show();
    }

    /**
     * 进入路径记录页面
     */
    private void toTravelingPage() {
        Intent intent =
            new Intent(MainActivity.this,TravelingActivity.class);
        startActivity(intent);
    }

    /**
     * 用户点击结束旅程后，弹出对话框让用户二次确认
     */
    private void endTravel() {
        //  弹出框：确认开始旅程
        AlertDialog.Builder AdBuilder =
                new AlertDialog.Builder(MainActivity.this);
        AdBuilder.setMessage(R.string.confirm_end_travel);

        AdBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //  弹出框：是否发表评价
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.choose_comment);

                builder.setPositiveButton(R.string.to_comment, new DialogInterface.OnClickListener() {
                    // 选择是，进入记录路径页面
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toEvaluatePage();
                    }
                });
                builder.setNegativeButton(R.string.not_comment, null);
                builder.create();
                builder.show();
                //  改变按钮显示
                btnEndTravel.setVisibility(View.INVISIBLE);
                btnCannotInvite.setVisibility(View.VISIBLE);
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
                new Intent(MainActivity.this,EvaluateActivity.class);
        startActivity(intent);
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
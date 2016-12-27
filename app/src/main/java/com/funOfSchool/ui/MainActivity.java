package com.funOfSchool.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
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
import android.widget.Button;
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
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.funOfSchool.R;
import com.funOfSchool.util.ApiUtils;
import com.funOfSchool.util.AppUtils;
import com.funOfSchool.util.CircleImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.funOfSchool.util.ApiUtils.API_ACCOUNT_PROFILE;
import static com.funOfSchool.util.AppUtils.HOST;

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

    /* 当前学校景点 */
    private String collegeScene;

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
    // 是否实时生成轨迹
    private boolean isNeedTrace;
    //头像控件
    private CircleImageView leftAvatar;
    //登陆、注册入口
    private TextView mainLogin;
    private TextView mainRegist;

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
        String url = AppUtils.HOST+ ApiUtils.API_USER_STATUS;
        // 请求参数：关键词
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.getToken(getApplicationContext()));
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
                        case 5:
                            btnEndTravel.setVisibility(View.VISIBLE);
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
                String url = AppUtils.HOST + ApiUtils.API_COLLEGE_NAMELIST;
                // 请求参数：关键词
                RequestParams param = new RequestParams();
                param.put("keyWord",etSearch.getText());
                // 发送网络请求
                client.post(url, param, new JsonHttpResponseHandler() {
                    // 发出网络请求前绑定adapter
                    @Override
                    public void onStart() {
                        super.onStart();
                        ada = new ArrayAdapter<>(
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
                // 根据学校名称，获得所选学校的经纬度和ID和景点
                AsyncHttpClient client = new AsyncHttpClient();
                String url = AppUtils.HOST + ApiUtils.API_COLLEGE_LALO_SCENE;
                // 请求参数：学校名称
                RequestParams param = new RequestParams();
                param.put("collegeName",collegeName);
                Log.e("aaaparam",param.toString());

                // 发送网络请求
                client.post(url, param, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.e("aaares",response.toString());

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
                            collegeScene = collegeNameJA.getJSONObject(0).getString("scene");

                            Log.e("LL:",collegeLantitude+"-"+collegeLongitude);
                            Log.e("scene:",collegeScene);

                            // 创建 LatLng 对象
                            LatLng ll = new LatLng(collegeLantitude, collegeLongitude);
                            // 设置所选学校位置为地图中心点，并移动到定位位置
                            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                            mBaiduMap.animateMapStatus(u);

                            // 构建Marker图标
                            BitmapDescriptor bitmapLogo = BitmapDescriptorFactory
                                    .fromResource(R.mipmap.college_logo);
                            // 添加附加信息
                            Bundle b = new Bundle();
                            b.putString("collegeInfo",
                                    collegeName+"好玩的地方有："+collegeScene
                                            +"快在这个学校找个同学带你玩吧~\n（点击可查看其它用户对此大学的评价）");
                            // 构建MarkerOption，用于在地图上添加Marker
                            OverlayOptions option = new MarkerOptions()
                                    .position(ll)
                                    .icon(bitmapLogo)
                                    .draggable(true)
                                    .title("大学")
                                    .extraInfo(b);
                            // 在地图上添加Marker，并显示
                            Marker marker = (Marker) mBaiduMap.addOverlay(option);
                            // 为图标设置监听器
                            mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    Toast.makeText(getApplicationContext(),
                                            "click marker",
                                            Toast.LENGTH_LONG).show();

                                    final String info = marker.getExtraInfo().getString("collegeInfo");

                                    InfoWindow infoWindow;
                                    // 动态生成一个Button对象，用户在地图中显示InfoWindow
                                    final Button textInfo = new Button(getApplicationContext());
                                    //设置布局属性
                                    /*int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                            30, getResources().getDisplayMetrics());
                                    textInfo.setLayoutParams(new DrawerLayout.LayoutParams(
                                            DrawerLayout.LayoutParams.WRAP_CONTENT, px
                                    ));*/
                                    textInfo.setHeight(300);
                                    textInfo.setWidth(500);
                                    textInfo.setBackgroundResource(R.drawable.marker_bg);
                                    textInfo.setPadding(12, 12, 12, 12);
                                    textInfo.setTextColor(getResources().getColor(R.color.firstText));
                                    textInfo.setTextSize(13);
                                    textInfo.setText(info);
                                    Log.i("infooooo",info);
                                    // 得到点击的覆盖物的经纬度
                                    LatLng ll = marker.getPosition();
                                    // 将marker所在的经纬度的信息转化成屏幕上的坐标
                                    Point p = mBaiduMap.getProjection().toScreenLocation(ll);
                                    p.x -= 100;
                                    p.y -= 100;
                                    LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
                                    // 初始化infoWindow，最后那个参数表示显示的位置相对于覆盖物的竖直偏移量，这里也可以传入一个监听器
                                    infoWindow = new InfoWindow(textInfo, llInfo, 0);
                                    // 显示此infoWindow
                                    mBaiduMap.showInfoWindow(infoWindow);
                                    // 点击按钮，跳转到学校评价列表页
                                    textInfo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getApplicationContext(),
                                                    "click",
                                                    Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(MainActivity.this,
                                                    CollegeCommentListActivity.class);
                                            intent.putExtra("collegeId",collegeId);
                                            Log.e("maincollegeid",collegeId+"");
                                            startActivity(intent);
                                        }
                                    });

                                    return true;
                                }
                            });

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
        btnTraveling = (ImageView)findViewById(R.id.map_traveling);
        btnEndTravel = (ImageView)findViewById(R.id.map_end_travel);
        btnPersoninfo = (LinearLayout)findViewById(R.id.me_personinfo);
        btnTravelist = (LinearLayout)findViewById(R.id.me_trvallist);
        btnPrize = (LinearLayout)findViewById(R.id.me_myprize);
        btnMarket = (LinearLayout)findViewById(R.id.me_market);
        btnSet = (LinearLayout)findViewById(R.id.me_set);
        leftAvatar = (CircleImageView)findViewById(R.id.leftavatar);
        mainLogin = (TextView)findViewById(R.id.mainLogin);
        mainRegist = (TextView)findViewById(R.id.mainRegist);
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
        mainLogin.setOnClickListener(mapListener);
        mainRegist.setOnClickListener(mapListener);
        leftAvatar.setOnClickListener(mapListener);
    }

    private class MapListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.index_me:
                    drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
                    drawerLayout.openDrawer(Gravity.LEFT);
                    getStarsAndPoint();
                    break;
                case R.id.index_msg:
                    startActivity(new Intent(MainActivity.this,ConversationListActivity.class));
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
                    Intent intent_mkt = new Intent(MainActivity.this,ShopActivity.class);
                    startActivity(intent_mkt);
                    break;
                case R.id.me_set:
                    Intent intent_st = new Intent(MainActivity.this,SetActivity.class);
                    startActivity(intent_st);
                    break;
                case R.id.mainLogin:
                    Intent intent_login = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent_login);
                    break;
                case R.id.mainRegist:
                    Intent intent_regist = new Intent(MainActivity.this,RegistActivity.class);
                    startActivity(intent_regist);
                    break;
                case R.id.leftavatar:
                    Intent intent_wantAltarAvatar = new Intent(MainActivity.this,PersonInfoActivity.class);
                    startActivity(intent_wantAltarAvatar);
                    break;
            }
        }
    }
    /**
     * 获取评分与积分的网络请求
     */
    private void getStarsAndPoint(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = HOST+API_ACCOUNT_PROFILE;
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.getToken(MainActivity.this));
        Log.e("url",url);
        client.get(url, param, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try {
                    JSONObject profile = null;
                    profile = new JSONObject(response.toString());
                    JSONObject profile1 = profile.getJSONObject("datum");
                    Log.i("profileImage",profile1.getString("profileImage"));
                    Glide.with(MainActivity.this).load(AppUtils.HOST+profile1.getString("profileImage")).into(leftAvatar);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 如果当前为可发送邀请状态，则点击按钮可发送邀请
     */
    private void sendInvitation() {
        //  验证token是否有效
        AsyncHttpClient client = new AsyncHttpClient();
        String url = AppUtils.HOST + ApiUtils.API_IS_TOKEN;
        // 请求参数
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.getToken(getApplicationContext()));
        Log.e("tokenparam",AppUtils.getToken(getApplicationContext()));
        client.post(url, param, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("tokenres",response.toString());

                try {
                    JSONObject isTokenJO = new JSONObject(response.toString());
                    int isTokenCode = isTokenJO.getInt("code");
                    if (isTokenCode == 1){
                        //  跳转到选择条件页面
                        Intent intent = new Intent(MainActivity.this,SelectActivity.class);
                        intent.putExtra("scid",collegeId);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),
                                "请先登录 :)",
                                Toast.LENGTH_LONG).show();
                        //  跳转到登录页面
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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
                        startTravelwithTrace();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
                    // 选择否
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startTravelwithoutTrace();
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
     * 开始旅程（记录路径）
     */
    private void startTravelwithTrace(){
        //  调用开始旅程接口（记录路径）
        AsyncHttpClient client = new AsyncHttpClient();
        String url = AppUtils.HOST + ApiUtils.API_MATCH_START_TRAVEL_WITH_TRACE;
        // 请求参数
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.getToken(getApplicationContext()));
        client.post(url, param, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }
        });
        toTravelingPage();
    }

    /**
     * 开始旅程（不记录路径）
     */
    private void startTravelwithoutTrace(){
        btnStartTravel.setVisibility(View.INVISIBLE);
        btnEndTravel.setVisibility(View.VISIBLE);
        //  调用开始旅程接口（不记录路径）
        AsyncHttpClient client = new AsyncHttpClient();
        String url = AppUtils.HOST + ApiUtils.API_MATCH_START_TRAVEL_WITHOUT_TRACE;
        // 请求参数
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.getToken(getApplicationContext()));
        client.post(url, param, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }
        });
    }

    /**
     * 进入路径记录页面
     */
    private void toTravelingPage() {

        Intent intent =
                new Intent(MainActivity.this,TravelingActivity.class);
        intent.putExtra("collegeId",collegeId);
        startActivity(intent);
    }

    /**
     * 用户点击结束旅程后，弹出对话框让用户二次确认
     */
    private void endTravel() {
        //  弹出框：确认结束旅程
        AlertDialog.Builder AdBuilder =
                new AlertDialog.Builder(MainActivity.this);
        AdBuilder.setMessage(R.string.confirm_end_travel);

        AdBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //  发送网络请求
                endTravelRequest();

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
            }
        });
        AdBuilder.setNegativeButton(R.string.no, null);
        AdBuilder.create();
        AdBuilder.show();
    }

    /**
     * 结束旅程网络请求
     */
    private void endTravelRequest() {
        //  调用结束旅程接口
        AsyncHttpClient client = new AsyncHttpClient();
        String url = AppUtils.HOST + ApiUtils.API_MATCH_END_TRAVEL;
        // 请求参数
        RequestParams param = new RequestParams();
        param.put("token",AppUtils.getToken(getApplicationContext()));
        client.post(url, param, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("end",response.toString());
            }
        });
        //  改变按钮显示
        btnEndTravel.setVisibility(View.INVISIBLE);
        btnCannotInvite.setVisibility(View.VISIBLE);
    }

    /**
     * 用户确认发表评价时，进入评价页面
     */
    private void toEvaluatePage() {
        Intent intent =
                new Intent(MainActivity.this,EvaluateActivity.class);
        intent.putExtra("collegeId",collegeId);
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
        //  设置首页按钮显示
        setIndexBtn();
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
package com.example.map;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.share.ShareSearch;
import com.example.map.overlay.DrivingRouteOverlay;

import static com.amap.api.services.route.RouteSearch.*;


public class RouteActivity extends AppCompatActivity implements OnRouteSearchListener,INaviInfoCallback {


    private AMap aMap;
    private Location myLocation;
    private String title;
    private MapView route;
    private LatLng latlon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        findView();
        initMapView(savedInstanceState);
        initIntent();
        initSearchDriveoute();
    }

    private void findView() {
        route = findViewById(R.id.Route_mapView);
    }
    //开始获取驾驶路线
    public void initSearchDriveoute(){
        //第一步,初始化routeSearch对象
        RouteSearch routeSearch = new RouteSearch(this);
        //第二部,设置数据回调监听
        routeSearch.setRouteSearchListener(this);
        //第三部设置搜索参数
        //参数1起始位置 参数2结束位置
        myLocation = MyApplication.getMyLocation();
        if (myLocation != null){
            //起始位置
            LatLonPoint startLatLonPoint = new LatLonPoint(myLocation.getLatitude(),myLocation.getLongitude());
            //结束位置
            LatLonPoint endLatLonPoint = new LatLonPoint(latlon.latitude,latlon.longitude);
            //从开始得到结束
            FromAndTo fromAndTo = new FromAndTo(startLatLonPoint,endLatLonPoint);
            DriveRouteQuery query = new DriveRouteQuery(fromAndTo, DRIVING_SINGLE_SHORTEST,null,null,"");
            //发送请求
            routeSearch.calculateDriveRouteAsyn(query);
        }
    }
    private void initIntent(){
        Intent intent = getIntent();
        if (intent!=null){
            title = intent.getStringExtra("title");
            latlon = intent.getParcelableExtra("latlon");
        }
    }
    private void initMapView(Bundle savedInstanceState){
        route.onCreate(savedInstanceState);
        aMap = route.getMap();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        route.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        route.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        route.onPause();
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }
    //驾车路线回调
    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        //5拿回调
        aMap.clear();//清除地图上的所有覆盖物
        if (i == AMapException.CODE_AMAP_SUCCESS){
            if (driveRouteResult != null && driveRouteResult.getPaths() != null){
                if (driveRouteResult.getPaths().size()>0){
                    DrivePath drivePath = driveRouteResult.getPaths().get(0);
                    if (drivePath == null){
                        return;
                    }
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(RouteActivity.this,aMap,drivePath,
                            driveRouteResult.getStartPos(),driveRouteResult.getTargetPos(),null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥挤情况,默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                }
            }
        }


    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }


    public void click(View view) {
        AMapLocation location = MyApplication.getMyLocation();
        Poi start = new Poi(location.getPoiName(),new LatLng(location.getLatitude(),location.getLongitude()),"");
        //终点传入的是北京站坐标,但是POI的ID "B000A83M61"对应的是北京西站，所以实际算路以北京西站作为终点
        Poi end = new Poi(title,latlon,"B000A83M61");
        AmapNaviPage.getInstance().showRouteActivity(this,new AmapNaviParams(start,null,end, AmapNaviType.DRIVER),this);
    }
    //导航失败
    @Override
    public void onInitNaviFailure() {

    }
    //拿到导航文字时
    @Override
    public void onGetNavigationText(String s) {

    }
    //定位变更的时候
    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }
    //到达目的地的时候
    @Override
    public void onArriveDestination(boolean b) {

    }
    //开始导航时
    @Override
    public void onStartNavi(int i) {

    }
    //计算路线成功时
    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }
    //计算路线失败时
    @Override
    public void onCalculateRouteFailure(int i) {

    }
    //停止讲话时
    @Override
    public void onStopSpeaking() {

    }
    //再次计算路线时
    @Override
    public void onReCalculateRoute(int i) {

    }
    //退出页面时
    @Override
    public void onExitPage(int i) {

    }
    //导航策略变化时
    @Override
    public void onStrategyChanged(int i) {

    }
    //获得导航底部view
    @Override
    public View getCustomNaviBottomView() {
        return null;
    }
    //获取自定义的导航view
    @Override
    public View getCustomNaviView() {
        return null;
    }
    //到达目的地时
    @Override
    public void onArrivedWayPoint(int i) {

    }
}

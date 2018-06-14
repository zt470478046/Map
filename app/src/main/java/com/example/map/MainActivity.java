package com.example.map;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

public class MainActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener{
    private MapView mapView;
    AMap aMap;
    private ImageView infowindow_icon;
    private TextView infowindow_title;
    private TextView infowindow_content;
    private TextView tv_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取控件
        mapView = findViewById(R.id.mapView);
        tv_location = findViewById(R.id.tv_location);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        amaps();
        //为北工商添加logo
        initLogo();

    }

    private void initLogo() {
        //获取坐标
        LatLng buba = new LatLng(40.233965,116.125824);
        BitmapDescriptor tor = BitmapDescriptorFactory.fromResource(R.drawable.zuo);
        final Marker marker = aMap.addMarker(new MarkerOptions().position(buba).icon(tor))  ;
        //创建一个自定义的infowindow
       // BubaInfoWindow bubaInfoWindow = new BubaInfoWindow(this,R.mipmap.ic_launcher_round,"北工商","小成子");
        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View view = View.inflate(MainActivity.this,R.layout.infowindow,null);
                infowindow_icon = view.findViewById(R.id.infoWindow_icon);
                infowindow_title = view.findViewById(R.id.infoWindow_title);
                infowindow_content = view.findViewById(R.id.infoWindow_content);
                infowindow_icon.setBackgroundResource(R.mipmap.ic_launcher_round);
                infowindow_title.setText("我是标题");
                infowindow_content.setText("我是小成子");
                return view;
            }
            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
        //infowidow的点击事件
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.isInfoWindowShown()){
                    marker.hideInfoWindow();
                }else{
                    marker.showInfoWindow();
                }
            }
        });
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker.isInfoWindowShown()){
                    marker.hideInfoWindow();
                }
            }
        });
        //Marker的点击事件
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown()){
                    marker.hideInfoWindow();
                }else{
                    marker.showInfoWindow();

                }
                return false;
            }
        });

    }

    private void amaps(){
        if (aMap == null){
            aMap = mapView.getMap();
        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(true);//设置是否显示小蓝点
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        //启动定位1秒钟之后去拿到定位
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Location location = aMap.getMyLocation();
                if (location == null || location.getExtras().get("city") == null){
                    tv_location.setText("定位中....");
                }else{

                }
                tv_location.setText(location.getExtras().get("city")+"");
            }
        },1000);


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity 执行onDestroy时执行mapview.onDestroy,销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMyLocationChange(Location location) {
        tv_location.setText(location.getExtras().get("city")+"");
    }
}

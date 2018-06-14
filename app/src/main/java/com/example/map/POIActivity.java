package com.example.map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;

public class POIActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener{
    private String sreachInfo;
    private EditText edit_poi;
    private MapView map;
    private AMap amap;
    private ImageView infowindow_icon;
    private TextView infowindow_title;
    private TextView infowindow_content;
    private ArrayList<Marker>markers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        findView(savedInstanceState);
    }

    private void findView(Bundle savedInstanceState) {
        edit_poi = findViewById(R.id.edit_poi);
        map = findViewById(R.id.map);
        amap = map.getMap();
        map.onCreate(savedInstanceState);

    }

    public void sreach(View view) {
        sreachInfo = edit_poi.getText().toString().trim();
        //判断是否合法
        if (!TextUtils.isEmpty(sreachInfo)){
            PoiSearch.Query query = new PoiSearch.Query(sreachInfo, "", "北京");
            //keyWord表示搜索字符串，
            //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
            //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
            query.setPageSize(10);// 设置每页最多返回多少条poiitem
            query.setPageNum(10);//设置查询页码
            //构造poisearch对象,并设置监听
            PoiSearch poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            //调用 PoiSearch 的 searchPOIAsyn() 方法发送请求。
            poiSearch.searchPOIAsyn();
        }else{
            Toast.makeText(this, "你输入的信息为空,请重新输入", Toast.LENGTH_SHORT).show();
        }
    }
    //实现回调函数
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        //当拿到poi搜索结果点的时候 怎么做 ：这的做法是吧marker标记出来，并且设置其InfoWindow
        ArrayList<PoiItem>pois = poiResult.getPois();
        initMarker(pois);
    }
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
    private void initMarker(ArrayList<PoiItem>pois) {
        for (PoiItem poi: pois){
            LatLonPoint paint = poi.getLatLonPoint();
            final LatLng latLng = new LatLng(paint.getLatitude(), paint.getLongitude());
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.zuo);
            Marker marker = amap.addMarker(new MarkerOptions().position(latLng).icon(bitmapDescriptor).title(poi.getTitle()).snippet(poi.getSnippet()));

            markers.add(marker);
            amap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    View view = View.inflate(POIActivity.this,R.layout.infowindow,null);
                    infowindow_icon = view.findViewById(R.id.infoWindow_icon);
                    infowindow_title = view.findViewById(R.id.infoWindow_title);
                    infowindow_content = view.findViewById(R.id.infoWindow_content);
                    infowindow_icon.setBackgroundResource(R.mipmap.ic_launcher_round);
                    infowindow_title.setText(marker.getTitle());
                    infowindow_content.setText(marker.getSnippet());
                    return view;
                }
                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });
            amap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (marker.isInfoWindowShown()){
                        marker.hideInfoWindow();
                    }else{
                        marker.showInfoWindow();
                        amap.moveCamera(CameraUpdateFactory.changeLatLng(marker.getPosition()));
                    }
                    return true;
                }
            });
            amap.setOnMapClickListener(new AMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    for (Marker marker:markers){
                        if (marker.isInfoWindowShown()){
                            marker.hideInfoWindow();
                        }
                    }
                }
            });
            //点击infowindow跳转页面
            amap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(POIActivity.this,ShopActivity.class);
                    //传递经纬度
                    intent.putExtra("title",marker.getTitle());
                    intent.putExtra("latlon",marker.getPosition());
                    intent.putExtra("des",marker.getSnippet());
                    startActivity(intent);
                }
            });
        }
    }
    
}




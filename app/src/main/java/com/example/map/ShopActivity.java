package com.example.map;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;

import java.util.ArrayList;
import java.util.IllegalFormatFlagsException;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    private TextView tv_dress;
    private String title;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        findView();
        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null){
            title = intent.getStringExtra("title");
            String des = intent.getStringExtra("des");
            latLng = intent.getParcelableExtra("latlon");
            tv_dress.setText(title);
            tv_dress.append("\n");
            tv_dress.append(des);
        }
    }

    private void findView() {
        tv_dress = findViewById(R.id.tv_dress);
    }

    public void click(View view) {
        Intent intent = new Intent(this,RouteActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("latlon",latLng);
        startActivity(intent);

           }
}

package com.example.map;

import android.app.Application;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;

public class MyApplication extends Application {
    private static AMapLocation myLocation = null;
    //声明AMapLocationClient类对象
    private AMapLocationClient myLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener locationListener = null;
    private AMapLocationClientOption locationClientOption = null;
    public static MyApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        this.app = this;
        locate();
    }

    private void locate() {
        locationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    myLocation = aMapLocation;
                    aMapLocation.toStr();
                    //停止定位
                    myLocationClient.stopLocation();

                }
            }
        };
        //初始化AMapLocationClientOption对象
        locationClientOption = new AMapLocationClientOption();
        //初始化定位
        myLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        myLocationClient.setLocationListener(locationListener);
        //设置定位模式
        locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //开始定位
        locationClientOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if (null != myLocationClient) {
            myLocationClient.setLocationOption(locationClientOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            myLocationClient.stopLocation();
            myLocationClient.startLocation();
        }
    }
    //提供获得applicaiton的方法
    public static  MyApplication getApp() {
        return app;
    }


    //提供获得当前位置的方法
    public static AMapLocation getMyLocation() {
        return myLocation;
    }
}
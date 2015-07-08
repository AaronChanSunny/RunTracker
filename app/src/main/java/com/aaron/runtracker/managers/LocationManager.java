package com.aaron.runtracker.managers;

import android.content.Context;

import com.aaron.runtracker.utils.LogUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by Aaron on 15/7/8.
 */
public class LocationManager {

    private static LocationManager mInstance;
    private Context mContext;
    private LocationClient mLocationClient;

    public synchronized static LocationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LocationManager(context);
        }

        return mInstance;
    }

    public LocationManager(Context context) {
        mContext = context;
        initLocationClient(context);
    }

    private void initLocationClient(Context context) {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向

        mLocationClient = new LocationClient(context.getApplicationContext(), option);

        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //Receive Location
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                sb.append(bdLocation.getTime());
                sb.append("\nerror code : ");
                sb.append(bdLocation.getLocType());
                sb.append("\nlatitude : ");
                sb.append(bdLocation.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(bdLocation.getLongitude());
                sb.append("\nradius : ");
                sb.append(bdLocation.getRadius());
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation){
                    sb.append("\nspeed : ");
                    sb.append(bdLocation.getSpeed());
                    sb.append("\nsatellite : ");
                    sb.append(bdLocation.getSatelliteNumber());
                    sb.append("\ndirection : ");
                    sb.append("\naddr : ");
                    sb.append(bdLocation.getAddrStr());
                    sb.append(bdLocation.getDirection());
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                    sb.append("\naddr : ");
                    sb.append(bdLocation.getAddrStr());
                    sb.append("\noperationers : ");
                    sb.append(bdLocation.getOperators());
                }
                LogUtil.i("BaiduLocationApiDem", sb.toString());
            }
        });
    }

    public void start() {
        mLocationClient.start();

        if (mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        }
    }

    public void stop() {
        mLocationClient.stop();
    }
}

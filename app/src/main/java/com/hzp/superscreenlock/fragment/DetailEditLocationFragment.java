package com.hzp.superscreenlock.fragment;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.hzp.superscreenlock.R;
import com.hzp.superscreenlock.manager.MyLocationManager;
import com.hzp.superscreenlock.utils.LogUtil;
import com.hzp.superscreenlock.utils.PreferencesUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailEditLocationFragment extends Fragment {

    public static final String TAG = DetailEditLocationFragment.class.getSimpleName();

    private ImageView markerView;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Button buttonOK;
    private MapStatus mMapStatus;

    private double longitude;
    private double latitude;

    public DetailEditLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_edit_location, container, false);

        markerView = (ImageView) view.findViewById(R.id.map_marker_view);
        markerView.setImageResource(R.drawable.marker);
        mMapView = (MapView) view.findViewById(R.id.map_baidu_view);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        Location location = MyLocationManager.getInstance().getCurrentLocation();
        //设定中心点坐标
        LatLng cenpt = new LatLng(location.getLatitude(),location.getLongitude());
        //定义地图状态
        mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                mMapStatus = mapStatus;
            }
        });

        buttonOK = (Button) view.findViewById(R.id.map_location_ok_button);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitude = mMapStatus.target.latitude;
                longitude = mMapStatus.target.longitude;
                LogUtil.i(TAG, "Location Selected latitude:" +
                        String.valueOf(latitude) +
                        ", longitude:" +
                        String.valueOf(longitude));

                PreferencesUtil.putDouble(
                        getContext(),
                        PreferencesUtil.KEY_ENV_LOCATION_LONGITUDE,
                        longitude
                );

                PreferencesUtil.putDouble(
                        getContext(),
                        PreferencesUtil.KEY_ENV_LOCATION_LATITUDE,
                        latitude
                );
                LogUtil.i(TAG,
                        "Put temp location item { longitude=" + longitude + " latitude=" + latitude +
                                "}");

                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

}

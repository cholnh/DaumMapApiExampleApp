package com.pomangam.exampleapp;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class CustomCurrentLocationEventListenerImpl implements MapView.CurrentLocationEventListener {
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }
}

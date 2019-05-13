package com.pomangam.exampleapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MapActivity extends AppCompatActivity {

    List<MapPoint> mapPoints = new ArrayList<>();
    MapView mapView;
    MapPOIItem porter;

    Random rand = new Random();
    double lat = 37.59989898737013;
    double lon = 126.86515864289788;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //create
        createDefaultMapView();
        createStoreMarker();
        createDsiteMarker();
        createPorterMarker();

        //service
        mapView.fitMapViewAreaToShowMapPoints(toArray(mapPoints));
        mapView.setZoomLevel(mapView.getZoomLevel()+1, true);
        new Timer().schedule(timerTask, 0, 1500);
    }

    private MapPoint[] toArray(List<MapPoint> list) {
        int size = list.size();
        MapPoint[] array = new MapPoint[size];
        for(int i=0; i<size; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    private void createDefaultMapView() {
        mapView = new MapView(this);
        ((ViewGroup) findViewById(R.id.map_view)).addView(mapView);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lon), true);
    }

    private void createStoreMarker() {
        MapPoint storeMapPoint = MapPoint.mapPointWithGeoCoord( 37.61859595460957, 126.84557972686093);
        MapPOIItem store = new MapPOIItem();
        store.setItemName("맘스터치");
        store.setTag(2);
        store.setMapPoint(storeMapPoint);
        store.setMarkerType(MapPOIItem.MarkerType.BluePin);
        store.setShowDisclosureButtonOnCalloutBalloon(false);
        mapView.addPOIItem(store);
        mapView.selectPOIItem(store, true);
        mapPoints.add(storeMapPoint);
    }

    private void createDsiteMarker() {
        MapPoint dsiteMapPoint = MapPoint.mapPointWithGeoCoord(37.600249662682124, 126.8644888270012);
        MapPOIItem dsite = new MapPOIItem();
        dsite.setItemName("학생회관 뒤");
        dsite.setTag(1);
        dsite.setMapPoint(dsiteMapPoint);
        dsite.setMarkerType(MapPOIItem.MarkerType.RedPin);
        dsite.setShowDisclosureButtonOnCalloutBalloon(false);
        mapView.addPOIItem(dsite);
        mapView.selectPOIItem(dsite, true);
        mapPoints.add(dsiteMapPoint);
    }

    private void createPorterMarker() {
        MapPoint porterMapPoint = MapPoint.mapPointWithGeoCoord(lat, lon);
        porter = new MapPOIItem();
        porter.setItemName("3분후 도착");
        porter.setTag(0);
        porter.setMapPoint(porterMapPoint);
        porter.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        porter.setCustomImageResourceId(R.drawable.porter);
        porter.setShowDisclosureButtonOnCalloutBalloon(false);
        mapView.addPOIItem(porter);
        mapPoints.add(porterMapPoint);
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            lat += rand.nextInt(10) * (rand.nextBoolean()?1:1) * 0.00001;
            lon += rand.nextInt(10) * (rand.nextBoolean()?1:1) * 0.00001;
            porter.setMapPoint(MapPoint.mapPointWithGeoCoord(lat, lon));
            porter.setRotation(rand.nextInt(360));
        }
    };
}

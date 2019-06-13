package com.pomangam.exampleapp.map.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pomangam.exampleapp.R;
import com.pomangam.exampleapp.common.api.RestService;
import com.pomangam.exampleapp.map.domain.MapDto;
import com.pomangam.exampleapp.map.service.MapService;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObserverActivity extends AppCompatActivity {

    List<MapPoint> mapPoints = new ArrayList<>();
    MapView mapView;
    MapPOIItem porter;

    Timer timer;
    Call<MapDto> call;

    double prevLat;
    double prevLon;
    boolean isPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observer);

        //create
        createDefaultMapView();
        createStoreMarker();
        createDsiteMarker();
        createPorterMarker();

        //service
        mapView.setZoomLevel(4, true);
        timer = new Timer();
        timer.schedule(timerTask, 0, 1500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
        timer.cancel();
    }

    @Override
    protected void onResume() {
        isPause = false;
        super.onResume();
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
        ((ViewGroup) findViewById(R.id.observer_view)).addView(mapView);
        //mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lon), true);
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
        mapPoints.add(dsiteMapPoint);
    }

    private void createPorterMarker() {
        MapPoint porterMapPoint = MapPoint.mapPointWithGeoCoord(37.600249662682124, 126.8644888270012);
        porter = new MapPOIItem();
        porter.setItemName("3분후 도착");
        porter.setTag(0);
        porter.setMapPoint(porterMapPoint);
        porter.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        porter.setCustomImageResourceId(R.drawable.porter);
        porter.setShowDisclosureButtonOnCalloutBalloon(false);
        mapView.addPOIItem(porter);
        mapView.selectPOIItem(porter, true);
        mapPoints.add(porterMapPoint);
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            setMapDto(1);
        }
    };

    int cnt = 0;
    boolean isFirst = true;
    private void setMapDto(Integer employeeIdx) {
        MapService service = RestService.of(MapService.class);
        call = service.getBy(employeeIdx);
        call.enqueue(new Callback<MapDto>() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    MapDto dto = (MapDto) response.body();

                    assert dto != null;
                    double lat = dto.getLatitude();
                    double lon = dto.getLongitude();
                    if(isPause || isSamePrev(lat, lon)) return;

                    Toast.makeText(getApplicationContext(),
                            dto+" " + cnt++ + " " + dto.getState() + " " + isPause,
                            Toast.LENGTH_SHORT).show();

                    if(isFirst) {
                        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lon), true);
                        isFirst = false;
                    }
                    if(dto.getState().intValue() == 1) {
                        porter.setAlpha(100);
                        porter.setMapPoint(MapPoint.mapPointWithGeoCoord(lat, lon));
                        porter.setRotation(dto.getDirection());
                    } else {
                        porter.setAlpha(0);
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "error",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        "Faild - " + t.getMessage() + " - " + t.getCause(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isSamePrev(double lat, double lon) {
        if(lat == prevLat && lon == prevLon) {
            return true;
        } else {
            prevLat = lat;
            prevLon = lon;
            return false;
        }
    }
}

package com.pomangam.exampleapp.map.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.pomangam.exampleapp.R;
import com.pomangam.exampleapp.common.api.RestService;
import com.pomangam.exampleapp.map.domain.MapDto;
import com.pomangam.exampleapp.map.service.MapService;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObservableActivity extends AppCompatActivity {

    MapService service = RestService.of(MapService.class);
    MapView mapView;
    TimerHandler timerHandler;

    double prevLat;
    double prevLon;
    boolean isPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observable);

        createDefaultMapView();
        setTimerHandler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
        timerHandler.stop();
        setMapState(Byte.valueOf("2"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
        timerHandler.start();
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

    private void createDefaultMapView() {
        mapView = new MapView(this);
        ((ViewGroup) findViewById(R.id.observable_view)).addView(mapView);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
    }

    private void setTimerHandler() {
        timerHandler = new TimerHandler(new Exec() {
            @Override
            public void exec() {
                double lat = mapView.getMapCenterPoint().getMapPointGeoCoord().latitude;
                double lon = mapView.getMapCenterPoint().getMapPointGeoCoord().longitude;
                float angle = mapView.getMapRotationAngle();
                if(isPause || isSamePrev(lat, lon)) return;

                MapDto dto = MapDto.builder()
                        .latitude(lat)
                        .longitude(lon)
                        .direction(angle)
                        .state(Byte.valueOf("1"))
                        .build();

                service.patch(1, dto).enqueue(new Callback<MapDto>() {
                    @Override
                    public void onResponse(Call<MapDto> call, Response<MapDto> response) {
                    }
                    @Override
                    public void onFailure(Call<MapDto> call, Throwable t) {
                        setMapState(Byte.valueOf("4"));
                    }
                });
            }
        });
        timerHandler.start();
    }

    private void setMapState(Byte state) {
        MapDto dto = MapDto.builder()
                .state(state)
                .build();
        service.patch(1, dto).enqueue(new Callback<MapDto>() {
            @Override
            public void onResponse(Call<MapDto> call, Response<MapDto> response) {
            }
            @Override
            public void onFailure(Call<MapDto> call, Throwable t) {
            }
        });
    }

    interface Exec {
        void exec();
    }
    private class TimerHandler extends Handler {
        private static final int MESSAGE_TIMER_START = 100;
        private static final int MESSAGE_TIMER_REPEAT = 101;
        private static final int MESSAGE_TIMER_STOP = 102;
        private static final int MESSAGE_TIMER_DELAY_MILLIS = 1500;

        private int count = 0;
        private Exec exec;

        public TimerHandler(Exec exec) {
            this.exec = exec;
        }

        public void start() {
            this.sendEmptyMessage(MESSAGE_TIMER_START);
        }

        public void stop() {
            this.sendEmptyMessage(MESSAGE_TIMER_STOP);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_TIMER_START :
                    // 초기화
                    count = 0;
                    this.removeMessages(MESSAGE_TIMER_REPEAT);
                    this.sendEmptyMessage(MESSAGE_TIMER_REPEAT);
                    break;
                case MESSAGE_TIMER_REPEAT :
                    // 반복
                    exec.exec();
                    this.sendEmptyMessageDelayed(MESSAGE_TIMER_REPEAT, MESSAGE_TIMER_DELAY_MILLIS);
                    break;
                case MESSAGE_TIMER_STOP :
                    this.removeMessages(MESSAGE_TIMER_REPEAT);
                    break;
            }
        }
    }
}

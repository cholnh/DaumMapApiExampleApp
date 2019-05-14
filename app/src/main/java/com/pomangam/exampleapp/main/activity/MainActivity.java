package com.pomangam.exampleapp.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pomangam.exampleapp.R;
import com.pomangam.exampleapp.common.api.RestService;
import com.pomangam.exampleapp.map.activity.ObservableActivity;
import com.pomangam.exampleapp.map.activity.ObserverActivity;
import com.pomangam.exampleapp.map.domain.MapDto;
import com.pomangam.exampleapp.map.service.MapService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView textViewInfo;
    Button buttonObserver;
    Button buttonObservable;
    Button buttonInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createElements();
        setEventElements();
    }

    private void createElements() {
        textViewInfo = findViewById(R.id.textView_info);
        buttonObserver = findViewById(R.id.button_toObserver);
        buttonObservable = findViewById(R.id.button_toObservable);
        buttonInfo = findViewById(R.id.button_info);
    }

    private void setEventElements() {
        buttonObserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ObserverActivity.class);
                startActivity(intent);
            }
        });
        buttonObservable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ObservableActivity.class);
                startActivity(intent);
            }
        });
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMapDto(1);
                //textViewInfo.setText(getMapDto(1)+"");
            }
        });
    }

    private MapDto getMapDto(Integer employeeIdx) {
        try {
            MapService service = RestService.of(MapService.class);
            Call<MapDto> call = service.getBy(employeeIdx);
            call.enqueue(new Callback<MapDto> () {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {

                        MapDto dto = (MapDto) response.body();
                        assert dto != null;
                        textViewInfo.setText(dto+"");

                    } else {
                        textViewInfo.setText("error");
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    //textViewInfo.setText("Faild - " + t.getMessage() + " - " + t.getCause());
                    Toast.makeText(getApplicationContext(),"Faild - " + t.getMessage() + " - " + t.getCause(),Toast.LENGTH_LONG).show();
                }
            });
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}

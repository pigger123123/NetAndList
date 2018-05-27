package com.example.dimension.netandlist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import weather.Weather;
import weather.parseWeatherJson;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener{

    TextView fl,wind_dir,tmp,cond_txt,wind_spd;
    ImageView Image;
    String data;

    Button back;

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
            {
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Intent intent=getIntent();
        data=intent.getStringExtra("extra");
        initWidget();
        sendRequestWithOkHttp(data);
    }
    private void initWidget()
    {
        fl=findViewById(R.id.weather_fl);
        wind_dir=findViewById(R.id.weather_wind_dir);
        Image=findViewById(R.id.Image);
        cond_txt=findViewById(R.id.condtxt);
        tmp=findViewById(R.id.tmp);
        back=findViewById(R.id.back);
        wind_spd=findViewById(R.id.weather_wind_spd);
    }
    private void sendRequestWithOkHttp(final String data)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Response response=new HttpResponseRequest().ReturnResponse("https://free-api.heweather.com/s6/weather/now?location=" + data + "&key=da3302c1f5c444b9b963ef6d0851d31f");
                    String weatherContent=response.body().string();
                    Weather weather= new parseWeatherJson().parseWeatherJSONWithGSON(weatherContent);
                    response=new HttpResponseRequest().ReturnResponse("http://10.0.2.2/cond_icon_heweather/"+weather.now.cond_code+".png");
                    byte[] bytes=response.body().bytes();
                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    show(weather,bitmap);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void show(final Weather weather,final Bitmap bitmap)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fl.setText(weather.now.fl+"℃");
                wind_dir.setText(weather.now.wind_dir);
                Image.setImageBitmap(bitmap);
                tmp.setText(weather.now.tmp+"℃");
                cond_txt.setText(weather.now.cond_txt);
                wind_spd.setText(weather.now.wind_spd+"km/h");
            }
        });
    }
}

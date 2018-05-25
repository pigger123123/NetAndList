package com.example.dimension.netandlist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import okhttp3.Response;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    TextView cityText;
    TextView foodText;
    String data;
    TextView condTxt;
    TextView tmp;
    private static final int LevelCity = 1;
    private static final int LevelFood = 2;
    final static String uri="http://20753yi414.iask.in:53064/city/";

    public void sendRequsetWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = new HttpResponseRequest().ReturnResponse("https://free-api.heweather.com/s6/weather/now?location=" + data + "&key=da3302c1f5c444b9b963ef6d0851d31f");
                    String weatherContent = response.body().string();
                    Weather weather = parseWeatherJSONWithGSON(weatherContent);
                    show(weather);
                    response = new HttpResponseRequest().ReturnResponse(uri + data + "/cityintroduce.txt");
                    String cityText = response.body().string();
                    show(cityText, LevelCity);
                    response = new HttpResponseRequest().ReturnResponse(uri + data + "/total.txt");
                    int total = Integer.parseInt(response.body().string());
                    Bitmap[] bitmaps = new Bitmap[total];
                    for (int i = 1; i <= total; i++) {
                        response = new HttpResponseRequest().ReturnResponse(uri + data + "/resources/pic_" + i + ".jpg");
                        byte[] pic = response.body().bytes();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
                        bitmaps[i - 1] = bitmap;
                    }
                    show(bitmaps);
                    response = new HttpResponseRequest().ReturnResponse(uri + data + "/foods.txt");
                    String foodText = response.body().string();
                    show(foodText, LevelFood);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void show(final Bitmap[] bitmaps) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout linearLayout = (LinearLayout) Main2Activity.this.findViewById(R.id.MyTable);
                ImageView[] imageViews = new ImageView[bitmaps.length];

                for (int i = 0; i < bitmaps.length; i++) {
                    imageViews[i] = new ImageView(Main2Activity.this);
                    imageViews[i].setImageBitmap(bitmaps[i]);
                    imageViews[i].setAdjustViewBounds(true);
                    imageViews[i].setScaleType(ImageView.ScaleType.FIT_XY);
                    linearLayout.addView(imageViews[i]);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmaps[i].compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    final byte[] data = byteArrayOutputStream.toByteArray();
                    imageViews[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(Main2Activity.this, ImageActivity.class);
                            intent.putExtra("extra", data);
                            startActivity(intent);
                        }
                    });

                }

            }
        });
    }

    private void show(final String s, final int Level) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (Level) {
                    case 1:
                        cityText.setText(s);
                        break;
                    case 2:
                        foodText.setText(s);
                        break;
                    default:
                        break;
                }
            }
        });
    }
    private void show(final Weather weather){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
              try{
                tmp.setText(weather.now.tmp+"℃");
                condTxt.setText(weather.now.cond_txt);}
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent=getIntent();
        data=intent.getStringExtra("extra_data");
        TextView textView=(TextView)findViewById(R.id.title_Text2);
        textView.setText(data);
        cityText=(TextView)findViewById(R.id.cityText);
        foodText=(TextView)findViewById(R.id.foodText);
        Button back=(Button)findViewById(R.id.back);
        back.setOnClickListener(this);
        sendRequsetWithOkHttp();
        Button refresh=(Button)findViewById(R.id.refresh);
        refresh.setOnClickListener(this);
        Button settings=(Button)findViewById(R.id.settings);
        settings.setOnClickListener(this);
        condTxt=(TextView)findViewById(R.id.condtxt);
        tmp=(TextView)findViewById(R.id.tmp);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
            {
                finish();
            }
            break;
            case R.id.refresh:
            {
                sendRequsetWithOkHttp();
            }
            break;
            case R.id.settings:
            {
                Intent intent=new Intent(Main2Activity.this,Main3Activity.class);
                startActivity(intent);
            }
            break;
        }
    }
    private static Weather parseWeatherJSONWithGSON(String data)
    {
        try{
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            Weather weather=new Gson().fromJson(weatherContent, Weather.class);
            return weather;

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}

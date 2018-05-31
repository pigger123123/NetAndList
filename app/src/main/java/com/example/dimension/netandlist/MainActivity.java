package com.example.dimension.netandlist;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;
import weather.Weather;
import weather.parseWeatherJson;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView SlidePanelListView;
    final static String uri="http://10.0.2.2/";
    final static String uriofCity="http://10.0.2.2/city/";
    TextView cityText;
    TextView foodText;
    String data="北京";
    TextView condTxt;
    TextView tmp,CityTitle;
    ImageView Image;
    private static final int LevelCity = 1;
    private static final int LevelFood = 2;
    DrawerLayout drawerLayout;
    public void sendCityListRequsetWithOkHttp()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Response response=new HttpResponseRequest().ReturnResponse(uri+"getdata.json");
                    String data=response.body().string();
                    parseCityJSONWithGSON(data);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();



    }
    private void parseCityJSONWithGSON(String data)
    {
        Gson gson=new Gson();
        List<gson> gsonlist=gson.fromJson(data,new TypeToken<List<gson>>(){}.getType());
        ArrayList<String> arrayList=new ArrayList<>();
        for(gson gson1:gsonlist) {
            arrayList.add(gson1.getName());
        }
        String[] strings=new String[arrayList.size()];
        arrayList.toArray(strings);
        showCityList(strings);
    }

    private void showCityList(final String[] Listdata)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,Listdata);
                SlidePanelListView.setAdapter(adapter);
                SlidePanelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        data=SlidePanelListView.getItemAtPosition(position).toString();
                        sendAllRequsetWithOkHttp();
                        drawerLayout.closeDrawers();
                    }
                });
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWidget();
        sendCityListRequsetWithOkHttp();
        sendAllRequsetWithOkHttp();
    }
    private void initWidget()
    {
        setContentView(R.layout.activity_main);
        SlidePanelListView=findViewById(R.id.listView);
        Button SlidePanelPower=findViewById(R.id.power);
        SlidePanelPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        drawerLayout=findViewById(R.id.drawerlayout);
        Button menu=findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        Image=findViewById(R.id.Image);
        cityText=findViewById(R.id.cityText);
        foodText=findViewById(R.id.foodText);
        Button refresh=findViewById(R.id.refresh);
        refresh.setOnClickListener(this);
        Button settings=findViewById(R.id.settings);
        settings.setOnClickListener(this);
        condTxt=findViewById(R.id.condtxt);
        tmp=findViewById(R.id.tmp);
        RelativeLayout relativeLayout=findViewById(R.id.layout_weather);
        relativeLayout.setOnClickListener(this);
        CityTitle=(TextView)findViewById(R.id.title_Text2);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.menu:
            {
                finish();
            }
            break;
            case R.id.refresh:
            {
                sendAllRequsetWithOkHttp();
            }
            break;
            case R.id.settings:
            {
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
            break;
            case R.id.layout_weather:
            {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("extra", data);
                startActivity(intent);
            }
        }
    }
    public void sendAllRequsetWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response= new HttpResponseRequest().ReturnResponse("https://free-api.heweather.com/s6/weather/now?location=" + data + "&key=da3302c1f5c444b9b963ef6d0851d31f");
                    String weatherContent = response.body().string();
                    Weather weather = new parseWeatherJson().parseWeatherJSONWithGSON(weatherContent);
                    show(weather,uri+"cond_icon_heweather/"+weather.now.cond_code+".png");

                    response = new HttpResponseRequest().ReturnResponse(uriofCity + data + "/cityintroduce.txt");
                    String cityText = response.body().string();
                    show(cityText, LevelCity);

                    response = new HttpResponseRequest().ReturnResponse(uriofCity + data + "/total.txt");
                    int total = Integer.parseInt(response.body().string());
                    String[] picPath=new String[total];
                    for (int i = 1; i <= total; i++) {
                        picPath[i-1]=uriofCity + data + "/resources/pic_" + i + ".jpg";
                    }
                    show(picPath);

                    response = new HttpResponseRequest().ReturnResponse(uriofCity + data + "/foods.txt");
                    String foodText = response.body().string();
                    show(foodText, LevelFood);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void show(final String[] picPath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LinearLayout linearLayout = (LinearLayout) MainActivity.this.findViewById(R.id.MyTable);
                ImageView[] imageViews = new ImageView[picPath.length];
                RequestOptions options = new RequestOptions().override(500).circleCrop();

                for (int i = 0; i < picPath.length; i++) {
                    imageViews[i] = new ImageView(MainActivity.this);
                    Glide.with(getApplicationContext()).load(picPath[i]).apply(options).into(imageViews[i]);
                    linearLayout.addView(imageViews[i]);

                    final String temp=picPath[i];
                    imageViews[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ImageActivity.class);
                            intent.putExtra("extra", temp);
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
                CityTitle.setText(data);
            }
        });
    }
    private void show(final Weather weather,final String path){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    tmp.setText(weather.now.tmp+"℃");
                    condTxt.setText(weather.now.cond_txt);
                    Glide.with(getApplicationContext()).load(path).into(Image);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}

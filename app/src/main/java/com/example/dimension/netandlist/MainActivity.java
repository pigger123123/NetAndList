package com.example.dimension.netandlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{
    private ListView listView;
    public String dataOfcity;
    public String dataOfweather;
    public String[] stringsOfcity;
    public String[] stringsOfweather;
    public void sendRequsetWithOkHttp()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Response response=new HttpResponseRequest().ReturnResponse("http://20753yi414.iask.in:53064/getdata.json");
                    dataOfcity=response.body().string();
                    stringsOfcity=parseCityJSONWithGSON(dataOfcity);
                    stringsOfweather=new String[stringsOfcity.length];
                    Log.d("da", "run: "+stringsOfcity.length);
                    for(int i=0;i<stringsOfcity.length;i++)
                    {
                        response= new HttpResponseRequest().ReturnResponse("https://free-api.heweather.com/s6/weather/now?location="+stringsOfcity[i]+"&key=da3302c1f5c444b9b963ef6d0851d31f");
                        dataOfweather=response.body().string();
                        Weather weather=parseWeatherJSONWithGSON(dataOfweather);
                        stringsOfweather[i]=weather.now.cond_txt+"  "+weather.now.tmp;
                        show(stringsOfcity,stringsOfweather);
                        //Log.d("s", "run: "+ stringsOfcity[i]+"  "+stringsOfweather[i]);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();



    }
    private String[] parseCityJSONWithGSON(String data)
    {
        Gson gson=new Gson();
        List<gson> gsonlist=gson.fromJson(data,new TypeToken<List<gson>>(){}.getType());
        ArrayList<String> arrayList=new ArrayList<>();
        for(gson gson1:gsonlist) {
            arrayList.add(gson1.getName());
        }
        String[] strings=new String[arrayList.size()];
        arrayList.toArray(strings);
        return strings;
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
    private void show(final String[] stringsOfcity,final String[] stringsOfweather)
    {
        final String[] convert=new String[stringsOfcity.length];
        for(int i=0;i<convert.length;i++)
        {
            convert[i]=stringsOfcity[i]+"   {天气情况："+stringsOfweather[i]+"℃}";
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,convert);
                listView.setAdapter(adapter);
               listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String data=stringsOfcity[listView.getCheckedItemCount()];
                        Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                        intent.putExtra("extra_data",data);
                        startActivity(intent);
                    }
                });
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.listView);
        sendRequsetWithOkHttp();
    }

}

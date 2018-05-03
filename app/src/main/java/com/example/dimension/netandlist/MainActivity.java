package com.example.dimension.netandlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity{
    private ListView listView;
    public void sendRequsetWithOkHttp()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .build();
                    Request request=new Request.Builder().url("http://20753yi414.iask.in:53064/getdata.json").build();
                    Response response=okHttpClient.newCall(request).execute();
                    String data=response.body().string();
                    parseJSONWithGSON(data);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    };
    private void parseJSONWithGSON(String data)
    {
        Gson gson=new Gson();
        List<gson> gsonlist=gson.fromJson(data,new TypeToken<List<gson>>(){}.getType());
        ArrayList<String> arrayList=new ArrayList<>();
        for(gson gson1:gsonlist) {
            arrayList.add(gson1.getName());

        }
        String[] strings=new String[arrayList.size()];
        arrayList.toArray(strings);
        show(strings);
    }
    private void show(final String[] strings)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,strings);
                listView.setAdapter(adapter);
               listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String data=listView.getItemAtPosition(position).toString();
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

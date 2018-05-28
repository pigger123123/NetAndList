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
    final static String uri="http://10.0.2.2/";

    public void sendRequsetWithOkHttp()
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
        show(strings);
    }

    private void show(final String[] data)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,data);
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
        initWidget();
        sendRequsetWithOkHttp();
    }
    private void initWidget()
    {
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listView);
        Button power=findViewById(R.id.power);
        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

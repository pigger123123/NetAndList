package com.example.dimension.netandlist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
    TextView cityText;
    TextView foodText;
    String data;
    private static final int LevelCity=1;
    private static final int LevelFood=2;
    public void sendRequsetWithOkHttp()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient okHttpClient=new OkHttpClient();
                    Request request;
                    Response response;

                    request=new Request.Builder().url("http://20753yi414.iask.in:53064/"+data+"/cityintroduce.txt").build();
                    response = okHttpClient.newCall(request).execute();
                    String cityText=response.body().string();
                    show(cityText,1);
                    request=new Request.Builder().url("http://20753yi414.iask.in:53064/"+data+"/total.txt").build();
                    response= okHttpClient.newCall(request).execute();
                    int total=Integer.parseInt(response.body().string());
                    Bitmap[] bitmaps=new Bitmap[total];
                    Response[] responses=new Response[total];
                    for(int i=1;i<=total;i++){
                        request = new Request.Builder().url("http://20753yi414.iask.in:53064/"+data+"/resources/pic_"+i+".jpg").build();
                        responses[i-1] = okHttpClient.newCall(request).execute();
                        byte[] pic = responses[i-1].body().bytes();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
                        bitmaps[i-1]=bitmap;

                    }
                    show(bitmaps);
                    request=new Request.Builder().url("http://20753yi414.iask.in:53064/"+data+"/foods.txt").build();
                    response= okHttpClient.newCall(request).execute();
                    String foodText=response.body().string();
                    show(foodText,2);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    };
    private void show(final Bitmap[] bitmaps)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(Bitmap bitmap:bitmaps){
                    LinearLayout linearLayout=(LinearLayout)Main2Activity.this.findViewById(R.id.MyTable);
                    ImageView imageView=new ImageView(Main2Activity.this);
                    imageView.setImageBitmap(bitmap);
                    LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.gravity= Gravity.CENTER_VERTICAL;
                    imageView.setLayoutParams(params);
                    linearLayout.addView(imageView);
                }
            }
        });
    }
    private void show(final String s,final int Level)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (Level)
                {
                    case 1:
                        cityText.setText(s);break;
                    case 2:
                        foodText.setText(s);break;
                        default:break;
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
        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(this);
        sendRequsetWithOkHttp();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button:
            {
                finish();
            }
            break;
        }
    }
}

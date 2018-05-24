package com.example.dimension.netandlist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import okhttp3.Response;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
    TextView cityText;
    TextView foodText;
    String data;
    private static final int LevelCity=1;
    private static final int LevelFood=2;
    private static final int LevelVideo=3;
    public void sendRequsetWithOkHttp()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{


                    Response response = new HttpResponseRequest().ReturnResponse("http://20753yi414.iask.in:53064/"+data+"/cityintroduce.txt");
                    String cityText=response.body().string();
                    show(cityText,1);
                    response=new HttpResponseRequest().ReturnResponse("http://20753yi414.iask.in:53064/"+data+"/total.txt");
                    int total=Integer.parseInt(response.body().string());
                    Bitmap[] bitmaps=new Bitmap[total];
                    for(int i=1;i<=total;i++){
                        response = new HttpResponseRequest().ReturnResponse("http://20753yi414.iask.in:53064/"+data+"/resources/pic_"+i+".jpg");
                        byte[] pic = response.body().bytes();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
                        bitmaps[i-1]=bitmap;
                    }
                    show(bitmaps);
                    response= new HttpResponseRequest().ReturnResponse("http://20753yi414.iask.in:53064/"+data+"/foods.txt");
                    String foodText=response.body().string();
                    show(foodText,2);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void show(final Bitmap[] bitmaps)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout linearLayout=(LinearLayout)Main2Activity.this.findViewById(R.id.MyTable);
                ImageView[] imageViews=new ImageView[bitmaps.length];

                for(int i=0;i<bitmaps.length;i++)
                {
                    imageViews[i]=new ImageView(Main2Activity.this);
                    imageViews[i].setImageBitmap(bitmaps[i]);
                    imageViews[i].setAdjustViewBounds(true);
                    imageViews[i].setScaleType(ImageView.ScaleType.FIT_XY);
                    linearLayout.addView(imageViews[i]);
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    bitmaps[i].compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                    final byte[] data=byteArrayOutputStream.toByteArray();
                    imageViews[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent=new Intent(Main2Activity.this,ImageActivity.class);
                            intent.putExtra("extra",data);
                            startActivity(intent);
                        }
                    });

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
        Button back=(Button)findViewById(R.id.back);
        back.setOnClickListener(this);
        sendRequsetWithOkHttp();
        Button refresh=(Button)findViewById(R.id.refresh);
        refresh.setOnClickListener(this);
        Button settings=(Button)findViewById(R.id.settings);
        settings.setOnClickListener(this);
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

}

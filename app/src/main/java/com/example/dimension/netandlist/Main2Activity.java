package com.example.dimension.netandlist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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


                    Response response = new HttpResponseRequest().ReturnResponse("http://10.0.2.2/"+data+"/cityintroduce.txt");
                    String cityText=response.body().string();
                    show(cityText,1);
                    response=new HttpResponseRequest().ReturnResponse("http://10.0.2.2/"+data+"/total.txt");
                    int total=Integer.parseInt(response.body().string());
                    Bitmap[] bitmaps=new Bitmap[total];
                    for(int i=1;i<=total;i++){
                        response = new HttpResponseRequest().ReturnResponse("http://10.0.2.2/"+data+"/resources/pic_"+i+".jpg");
                        byte[] pic = response.body().bytes();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
                        bitmaps[i-1]=bitmap;
                    }
                    show(bitmaps);
                    response= new HttpResponseRequest().ReturnResponse("http://10.0.2.2/"+data+"/foods.txt");
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
                for(int i=0;i<bitmaps.length;i++)
                {
                    ImageView[] imageViews=new ImageView[bitmaps.length];
                    imageViews[i]=new ImageView(Main2Activity.this);
                    imageViews[i].setImageBitmap(bitmaps[i]);
                    imageViews[i].setAdjustViewBounds(true);
                    imageViews[i].setScaleType(ImageView.ScaleType.FIT_XY);
                    linearLayout.addView(imageViews[i]);
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
        //sendRequsetWithOkHttp();
        Button refrsh=(Button)findViewById(R.id.refresh);
        refrsh.setOnClickListener(this);


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
        }
    }
}

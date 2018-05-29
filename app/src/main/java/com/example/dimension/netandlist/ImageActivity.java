package com.example.dimension.netandlist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        Intent intent=getIntent();
        String picPath=intent.getStringExtra("extra");
        ImageView imageView=findViewById(R.id.ImageViewShow);
        RequestOptions options=new RequestOptions().centerCrop();
        Glide.with(ImageActivity.this).load(picPath).apply(options).into(imageView);
        Button back=findViewById(R.id.backset);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

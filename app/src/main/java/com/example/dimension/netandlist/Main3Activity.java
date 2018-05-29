package com.example.dimension.netandlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener{
    String[] strings={"About"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set);
        Button back=findViewById(R.id.backset);
        back.setOnClickListener(this);
        setListView(strings);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case  R.id.backset:
            {
                finish();
            }
            break;
        }
    }
    private void setListView(String[] strings)
    {
        ListView listView=findViewById(R.id.listViewset);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(Main3Activity.this,android.R.layout.simple_list_item_1,strings);
        listView.setAdapter(adapter);
    }
}

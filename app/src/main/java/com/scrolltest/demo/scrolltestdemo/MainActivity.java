package com.scrolltest.demo.scrolltestdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScrollLayout scrollLayout = (ScrollLayout) findViewById(R.id.scrollLayout);
        //创造3个listview
        for (int i = 0; i < 3; i++) {
            ListView listView = new ListView(this);
            ArrayList<String> list = new ArrayList<>();
            for (int i1 = 0; i1 < 50; i1++) {
                list.add("page"+i+":"+i1+",name"+i+":"+i1);
            }
            //创建adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);
            scrollLayout.addView(listView);
        }
    }
}

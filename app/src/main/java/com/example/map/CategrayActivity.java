package com.example.map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CategrayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categray);
    }

    public void bt1(View view) {
        //点击bt1将跳转到MainActivity页面
        Intent intent = new Intent(CategrayActivity.this,MainActivity.class);
        startActivity(intent);

    }

    public void bt2(View view) {
        //点击bt2将跳转到POIActivity页面
        Intent intent = new Intent(CategrayActivity.this,POIActivity.class);
        startActivity(intent);
    }
}

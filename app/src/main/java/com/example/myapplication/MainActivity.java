package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startActivity(new Intent(this,));
        initListener();
    }

    private void initListener() {
        //点击测试事件
        findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, saySomething(), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.tv_test_wework).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "测试企业微信", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //将被hook的方法
    public String saySomething() {
        return "我是一个好人";
    }
}

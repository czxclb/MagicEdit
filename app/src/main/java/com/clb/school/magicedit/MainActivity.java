package com.clb.school.magicedit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click1(View view){
        MagicView2 magicView = new MagicView2(this);
        magicView.setBackgroundResource(R.mipmap.ic_launcher_round);
        magicView.setLayoutParams(new ViewGroup.LayoutParams(500,500));
        ViewGroup parent = (ViewGroup)view.getParent();
        parent.addView(magicView);
    }

    public void click2(View view){
        MagicView1 magicView2 = new MagicView1(this);
        magicView2.setBackgroundResource(R.mipmap.ic_launcher);
        magicView2.setLayoutParams(new ViewGroup.LayoutParams(700,700));
        ViewGroup parent = (ViewGroup)view.getParent();
        parent.addView(magicView2);
    }
}

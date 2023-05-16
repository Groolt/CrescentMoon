package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ListView;

import com.example.damb_qlnh.adapter.KHAdapter;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.khachHang;

import java.util.ArrayList;
import java.util.List;

public class QLKHActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlkh);

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //list khach hang
        ListView lv = findViewById(R.id.lv);
        lv.setAdapter(new KHAdapter(this, R.layout.item_person, getlistKH()));
    }

    private List<khachHang> getlistKH() {
        List<khachHang> l = new ArrayList<>();
        l.add( new khachHang("123", "Nguyễn Bá Hảo", "Nam", "0963618637", "Diamond", "30/04/1975", 1));
        l.add( new khachHang("123", "Nguyễn Bá Hảo", "Nam", "0963618637", "Diamond", "30/04/1975", 1));
        l.add( new khachHang("123", "Nguyễn Bá Hảo", "Nam", "0963618637", "Diamond", "30/04/1975", 1));
        l.add( new khachHang("123", "Nguyễn Bá Hảo", "Nam", "0963618637", "Diamond", "30/04/1975", 1));
        l.add( new khachHang("123", "Nguyễn Bá Hảo", "Nam", "0963618637", "Diamond", "30/04/1975", 1));
        l.add( new khachHang("123", "Nguyễn Bá Hảo", "Nam", "0963618637", "Diamond", "30/04/1975", 1));
        l.add( new khachHang("123", "Nguyễn Bá Hảo", "Nam", "0963618637", "Diamond", "30/04/1975", 1));
        l.add( new khachHang("123", "Nguyễn Bá Hảo", "Nam", "0963618637", "Diamond", "30/04/1975", 1));
        l.add( new khachHang("123", "Nguyễn Bá Hảo", "Nam", "0963618637", "Diamond", "30/04/1975", 1));
        l.add( new khachHang("123", "Hảo Hảo 100", "Nữ", "0963618637", "Diamond", "30/04/1975", 1));
        return l;
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
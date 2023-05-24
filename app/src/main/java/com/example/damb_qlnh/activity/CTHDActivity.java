package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ListView;

import com.example.damb_qlnh.adapter.monAdapterCTBan;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.monAn;

import java.util.ArrayList;
import java.util.List;

public class CTHDActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cthdactivity);

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //list mon an
        ListView lv = findViewById(R.id.lv);
        monAdapterCTBan adapter = new monAdapterCTBan(this,R.layout.mon, getlistMon());
        lv.setAdapter(adapter);
    }
    private ArrayList<CTHD> getlistMon() {
        ArrayList<CTHD> l = new ArrayList<>();
//        l.add(new CTHD((new monAn("1","Pizza", "Main Course", "1", R.drawable.test)), 2,"1"));
//        l.add(new CTHD((new monAn("2","Pizza", "Main Course", "1", R.drawable.test)), 1,"1"));
//        l.add(new CTHD((new monAn("3","Pizza", "Main Course", "1", R.drawable.test)), 2,"1"));
//        l.add(new CTHD((new monAn("4","Pizza", "Main Course", "1", R.drawable.test)), 3,"1"));
//        l.add(new CTHD((new monAn("5","Pizza", "Main Course", "1", R.drawable.test)), 2,"1"));
//        l.add(new CTHD((new monAn("6","Pizzajj", "Main Course", "1", R.drawable.test)), 2,"1"));
        return l;
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
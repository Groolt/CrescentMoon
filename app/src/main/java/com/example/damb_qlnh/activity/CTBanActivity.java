package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.damb_qlnh.adapter.monAdapterCTBan;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.monAn;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CTBanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctban);
        ListView lv = findViewById(R.id.lv_mon);
        monAdapterCTBan adapter = new monAdapterCTBan(this,R.layout.mon, getlistMon());
        lv.setAdapter(adapter);

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tv = findViewById(R.id.toolbar_title);
        tv.setText("Bàn 111.2");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
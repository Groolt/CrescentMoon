package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.monAn;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QLDTActivity extends AppCompatActivity {
    ArrayList barEntriesArrayList;
    BarDataSet barDataSet;
    BarData barData;
    private ViewGroup mLinearLayoutBanChay;
    private ViewGroup mLinearLayoutBanIt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qldtactivity);

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //bar chart
        BarChart barChart = findViewById(R.id.idBarChart);
        getBarEntries();
        barDataSet = new BarDataSet(barEntriesArrayList, "Đơn vị: triệu vnđ");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(Color.rgb(249, 155, 70));

        //Trung binh doanh thu
        Spinner spn = findViewById(R.id.spn);
        spn.setAdapter(new ArrayAdapter<String>(this, R.layout.style_spinner, Arrays.asList("1 năm", "1 quý", "30 ngày", "7 ngày")));

        //DS ban chay(lay cac mon an ban nhieu nhat trong bang cthd + sl)
        ArrayList<monAn> dsMonBanChay = new ArrayList<>();
//        dsMonBanChay.add(new monAn("1","Pizza", "Pudding", "1", R.drawable.test));
//        dsMonBanChay.add(new monAn("2","Pizza", "Pudding", "1", R.drawable.test));
//        dsMonBanChay.add(new monAn("3","Pizza", "Pudding", "1", R.drawable.test));
//        dsMonBanChay.add(new monAn("4","Pizza", "Pudding", "1", R.drawable.test));
        mLinearLayoutBanChay = (ViewGroup) findViewById(R.id.linear_banchay);
        for (monAn mon : dsMonBanChay){
            addToView(mon, mLinearLayoutBanChay);
        }

        //DS ban it
        ArrayList<monAn> dsMonBanit = new ArrayList<>();
//        dsMonBanit.add(new monAn("1","Pizza", "Pudding", "1", R.drawable.test));
//        dsMonBanit.add(new monAn("2","Pizza", "Pudding", "1", R.drawable.test));
//        dsMonBanit.add(new monAn("3","Pizza", "Pudding", "1", R.drawable.test));
//        dsMonBanit.add(new monAn("4","Pizza", "Pudding", "1", R.drawable.test));
        mLinearLayoutBanIt = findViewById(R.id.linear_banit);
        for (monAn mon : dsMonBanit){
            addToView(mon, mLinearLayoutBanIt);
        }
    }

    private void addToView(monAn mon, ViewGroup viewGroup) {
        View view = LayoutInflater.from(this).inflate(R.layout.mon_ko_gia, viewGroup, false);
        TextView tv = view.findViewById(R.id.tenmon);
        TextView sl = view.findViewById(R.id.editable);
        tv.setText(mon.getTenMA());
        sl.setText("SL: " + String.valueOf(10)); // chua xu ly nen de mac dinh la 10
        viewGroup.addView(view);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private void getBarEntries() {
        // creating a new array list
        barEntriesArrayList = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        barEntriesArrayList.add(new BarEntry(19, 10.2f));
        barEntriesArrayList.add(new BarEntry(20, 15.5f));
        barEntriesArrayList.add(new BarEntry(21, 35));
        barEntriesArrayList.add(new BarEntry(22, 22));
        barEntriesArrayList.add(new BarEntry(23, 4.5f));
        barEntriesArrayList.add(new BarEntry(24, 28.4f));
    }
}
package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.datBan;

import java.util.ArrayList;

public class QLLichDatActivity extends AppCompatActivity {

    private ViewGroup viewDSLich;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qllich_dat);

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //ds lich dat
        viewDSLich = findViewById(R.id.lich);
        ArrayList<datBan> dsLichDat = new ArrayList<>();
        dsLichDat.add(new datBan("07:30 - 09:30", "113.2", "Bá Hảo"));
        dsLichDat.add(new datBan("07:30 - 09:30", "113.2", "Bá Hảo"));
        dsLichDat.add(new datBan("07:30 - 09:30", "113.2", "Bá Hảo"));
        dsLichDat.add(new datBan("07:30 - 09:30", "113.2", "Bá Hảo"));
        dsLichDat.add(new datBan("07:30 - 09:30", "113.2", "Bá Hảo"));
        dsLichDat.add(new datBan("07:30 - 09:30", "113.2", "Bá Hảo"));
        dsLichDat.add(new datBan("07:30 - 09:30", "113.4", "Bá Hảo"));
        for (datBan lich : dsLichDat) {
            addToView(lich, viewDSLich);
        }

    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private void addToView(datBan lich, ViewGroup viewGroup) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_lichdat, viewGroup, false);
        TextView giodat = view.findViewById(R.id.giodat);
        TextView ban = view.findViewById(R.id.ban);
        TextView ten = view.findViewById(R.id.ten);
        CardView xoa = view.findViewById(R.id.xoa);

        giodat.setText(lich.getTime());
        ban.setText("Bàn " + lich.getMaBan());
        ten.setText(lich.getName());

        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(QLLichDatActivity.this, "Xóa " + ban.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        viewGroup.addView(view);
    }
}
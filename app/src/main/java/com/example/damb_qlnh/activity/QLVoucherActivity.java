package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.damb_qlnh.adapter.VoucherAdapter;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.vouCher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QLVoucherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlvoucher);

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //list voucher
        ListView lv = findViewById(R.id.lv);
        lv.setAdapter(new VoucherAdapter(this, R.layout.item_voucher, getListVoucher()));

        //them voucher
        ImageView addbtn = findViewById(R.id.addbtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(QLVoucherActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.form_them_voucher);
                Window window = dialog.getWindow();
                if (window == null) return;
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                CardView cancel = dialog.findViewById(R.id.cancel_button);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private ArrayList<vouCher> getListVoucher() {
        ArrayList<vouCher> l = new ArrayList<>();
        l.add(new vouCher("123", "2/9/1945", "30/4/1975",23, 50));
        l.add(new vouCher("123", "2/9/1945", "30/4/1975",23, 20));
        l.add(new vouCher("123", "2/9/1945", "30/4/1975",23, 50));
        l.add(new vouCher("123", "2/9/1945", "30/4/1975",23, 75));
        return l;
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
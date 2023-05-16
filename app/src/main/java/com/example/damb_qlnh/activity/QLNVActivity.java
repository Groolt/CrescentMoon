package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.monAn;
import com.example.damb_qlnh.models.nhanVien;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class QLNVActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlnv);

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        // add nhan vien bao ve
        ArrayList<nhanVien> listBV = new ArrayList<>();
        listBV.add(new nhanVien("Bảo vệ", 1, "Nguyễn Bá Hảo", "123","Nam", "0946871257", "068203002609"));
        listBV.add(new nhanVien("Bảo vệ", 1, "Nguyễn Bá Hảo", "123","Nam", "0946871257", "068203002609"));

        for(nhanVien nv : listBV){
            addToView(nv, (ViewGroup) findViewById(R.id.layout_baove));
        }

        // add nhan vien phuc vu
        ArrayList<nhanVien> listPV = new ArrayList<>();
        listPV.add(new nhanVien("Phục vụ", 1, "Nguyễn Bá Hảo", "123","Nam", "0946871257", "068203002609"));
        listPV.add(new nhanVien("Phục vụ", 1, "Nguyễn Bá Hảo", "123","Nam", "0946871257", "068203002609"));
        for(nhanVien nv : listPV){
            addToView(nv, (ViewGroup) findViewById(R.id.layout_phucvu));
        }

        // add nhan vien thu ngan
        ArrayList<nhanVien> listTN = new ArrayList<>();
        listTN.add(new nhanVien("Thu ngân", 1, "Nguyễn Bá Hảo", "123","Nam", "0946871257", "068203002609"));
        listTN.add(new nhanVien("Thu ngân", 1, "Nguyễn Bá Hảo", "123","Nam", "0946871257", "068203002609"));
        listTN.add(new nhanVien("Thu ngân", 1, "Nguyễn Bá Hảo", "123","Nam", "0946871257", "068203002609"));

        for(nhanVien nv : listTN){
            addToView(nv, (ViewGroup) findViewById(R.id.layout_thungan));
        }

        // them 1 nhan vien moi
        ImageView addBtn = findViewById(R.id.addbtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(QLNVActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.form_them_nv);
                Window window = dialog.getWindow();
                if (window == null) return;
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Spinner spn = dialog.findViewById(R.id.gender);
                spn.setAdapter(new ArrayAdapter<>(QLNVActivity.this, R.layout.style_spinner_form, Arrays.asList("Nam", "Nữ", "Khác")));

                Spinner spn2 = dialog.findViewById(R.id.vitri);
                spn2.setAdapter(new ArrayAdapter<>(QLNVActivity.this, R.layout.style_spinner_form, Arrays.asList("Bảo vệ", "Thu ngân", "Phục vụ")));

                Spinner spn3 = dialog.findViewById(R.id.loai);
                spn3.setAdapter(new ArrayAdapter<>(QLNVActivity.this, R.layout.style_spinner_form, Arrays.asList("Part-time", "Full-time")));

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

    private void addToView(nhanVien nv, ViewGroup viewGroup) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_person, viewGroup, false);
        TextView ten = view.findViewById(R.id.ten);
        CircleImageView avatar = view.findViewById(R.id.avatar);
        ten.setText(nv.getTenNV());
        switch (nv.getViTri()){
            case "Bảo vệ": avatar.setImageResource(R.drawable.bv); break;
            case "Phục vụ": avatar.setImageResource(R.drawable.pv); break;
            case "Thu ngân": avatar.setImageResource(R.drawable.thungan); break;
            default: avatar.setImageResource(R.drawable.person); break;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QLNVActivity.this, CTNVActivity.class));
            }
        });
        viewGroup.addView(view);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
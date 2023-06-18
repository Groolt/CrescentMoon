package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.khachHang;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CTKHActivity extends AppCompatActivity {
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctkh);
        db = FirebaseFirestore.getInstance();

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //
        TextView ten = findViewById(R.id.ten);
        TextView rank = findViewById(R.id.rank);
        TextView gender = findViewById(R.id.gender);
        TextView phone = findViewById(R.id.phone);
        TextView ngaysinh = findViewById(R.id.ngaysinh);
        ImageView avatar = findViewById(R.id.circleImageView);

        db.collection("khachHang")
                .document(getIntent().getStringExtra("idKH"))
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    khachHang kh = documentSnapshot.toObject(khachHang.class);
                    ten.setText(kh.getTenKH());
                    rank.setText(kh.getXepHang());
                    gender.setText(kh.getGioiTinh());
                    phone.setText(kh.getSDT());
                    ngaysinh.setText(kh.getDob());
                    Glide.with(this).load(kh.getImg()).into(avatar);
                });
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
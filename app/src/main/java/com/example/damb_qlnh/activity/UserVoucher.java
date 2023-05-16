package com.example.damb_qlnh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.adapter.VoucherAdapter;
import com.example.damb_qlnh.adapter.uservoucherAdapter;
import com.example.damb_qlnh.models.vouCher;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class UserVoucher extends AppCompatActivity {
    private Button btnBack;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_voucher);
        btnBack = findViewById(R.id.btn_back_vch);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        recyclerView = findViewById(R.id.gdvch_rcv);
        ArrayList<vouCher> l = new ArrayList<>();
        l.add(new vouCher("123", "2/9/1945", "30/4/1975",23, 50));
        l.add(new vouCher("123", "2/9/1945", "30/4/1975",23, 20));
        l.add(new vouCher("123", "2/9/1945", "30/4/1975",23, 50));
        l.add(new vouCher("123", "2/9/1945", "30/4/1975",23, 75));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        uservoucherAdapter voucherAdapter = new uservoucherAdapter(l, this);
        recyclerView.setAdapter(voucherAdapter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        startActivity(new Intent(UserVoucher.this, UserHome.class));
                        break;
                    case R.id.action_history:
                        startActivity(new Intent(UserVoucher.this, UserHistory.class));
                        break;
                    case R.id.action_order:
                        startActivity(new Intent(UserVoucher.this, UserOrder.class));
                        break;
                    case R.id.action_profile:
                        startActivity(new Intent(UserVoucher.this, UserProfile.class));
                        break;
                    case R.id.action_QR:
                        Toast.makeText(UserVoucher.this, "QR",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserVoucher.this, UserHome.class));
    }
}
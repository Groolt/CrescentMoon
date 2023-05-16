package com.example.damb_qlnh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.adapter.HistoryAdapter;
import com.example.damb_qlnh.models.hoaDon;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class UserHistory extends AppCompatActivity {
    private Button btnBack;
    private RecyclerView recyclerView;
    private ArrayList<hoaDon> hoaDons;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_user_history);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.action_history);
        btnBack = findViewById(R.id.btn_back_history);
        recyclerView = findViewById(R.id.gdhistory_rcv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(UserHistory.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        hoaDons = new ArrayList<>();
        hoaDons.add(new hoaDon("1","1", "1", "1", "1", "1", "1", "1", new ArrayList<>()));
        HistoryAdapter historyAdapter = new HistoryAdapter(hoaDons,UserHistory.this);
        recyclerView.setAdapter(historyAdapter);
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
                        startActivity(new Intent(UserHistory.this, UserHome.class));
                        break;
                    case R.id.action_order:
                        startActivity(new Intent(UserHistory.this, UserOrder.class));
                        break;
                    case R.id.action_profile:
                        startActivity(new Intent(UserHistory.this, UserProfile.class));
                        break;
                    case R.id.action_QR:
                        Toast.makeText(UserHistory.this, "QR",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserHistory.this, UserHome.class));
    }
}
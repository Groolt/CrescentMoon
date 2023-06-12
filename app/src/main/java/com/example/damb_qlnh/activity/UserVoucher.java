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
import com.example.damb_qlnh.models.monAn;
import com.example.damb_qlnh.models.vouCher;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class UserVoucher extends AppCompatActivity {
    private Button btnBack;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private ArrayList<vouCher> l;
    FirebaseFirestore db;
    private uservoucherAdapter voucherAdapter;
    private ArrayList<String> lused;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_voucher);
        l = new ArrayList<>();
        lused = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        btnBack = findViewById(R.id.btn_back_vch);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        recyclerView = findViewById(R.id.gdvch_rcv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        voucherAdapter = new uservoucherAdapter(l, this);
        recyclerView.setAdapter(voucherAdapter);
        getdsVoucher();
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
    public void getdsVoucher(){
        lused.clear();
        l.clear();
        db.collection("dsvouCher")
                .whereEqualTo("maKH", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            lused.add(document.getId());
                        }
                    } else {
                        Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
        db.collection("Voucher")
                .whereEqualTo("is_deleted", false)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (!lused.contains(document.getId())){
                                l.add(new vouCher(document.getId(), document.getString("ngayBD"), document.getString("ngayKT"),
                                         document.getLong("soLuong").intValue(), document.getLong("giaTri").intValue()));
                            }
                        }
                        voucherAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
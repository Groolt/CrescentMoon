package com.example.damb_qlnh.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.adapter.HistoryAdapter;
import com.example.damb_qlnh.models.hoaDon;
import com.example.damb_qlnh.models.vouCher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class UserHistory extends AppCompatActivity {
    private Button btnBack;
    private RecyclerView recyclerView;
    private ArrayList<hoaDon> hoaDons;
    private BottomNavigationView bottomNavigationView;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private SharedPreferences prefs;
    private HistoryAdapter historyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_user_history);
        Init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(UserHistory.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        hoaDons = new ArrayList<>();
        historyAdapter = new HistoryAdapter(hoaDons,UserHistory.this);
        recyclerView.setAdapter(historyAdapter);
        getHoaDon();
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
                        scanCode();
                        break;
                }
                return true;
            }
        });
    }
    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }
    private ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() !=null) {
            UserHome.setmaBan(result.getContents().toString().trim());
        }
    });
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserHistory.this, UserHome.class));
    }

    public void getHoaDon(){
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        db.collection("HoaDon").whereEqualTo("maKH", prefs.getString("maKH", ""))
                        .whereEqualTo("tinhTrang", 1)
                                .get() .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                        hoaDons.add(new hoaDon(document.getString("maHD"), document.getString("thoiGian"),
                                document.getString("maKH"), document.getString("maBan"), document.getString("maVoucher"),
                                String.valueOf(document.get("tongTien_T")), String.valueOf(document.get("tongTien_S"))));
                        }
                        historyAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
        progressDialog.dismiss();
    }
    public void Init(){
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(UserHistory.this);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.action_history);
        btnBack = findViewById(R.id.btn_back_history);
        prefs = getSharedPreferences("dba", Context.MODE_PRIVATE);
        recyclerView = findViewById(R.id.gdhistory_rcv);
    }
}
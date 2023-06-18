package com.example.damb_qlnh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damb_qlnh.adapter.KHAdapter;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.adapter.monAdapterQLmon;
import com.example.damb_qlnh.models.datBan;
import com.example.damb_qlnh.models.khachHang;
import com.example.damb_qlnh.models.monAn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class QLKHActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    FirebaseFirestore db;
    ListView lv;
    KHAdapter adapter;
    List<khachHang> listKH;
    List<khachHang> listTop3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlkh);
        db = FirebaseFirestore.getInstance();

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //list khach hang
        lv = findViewById(R.id.lv);
        listKH = new ArrayList<>();
        adapter = new KHAdapter(this, R.layout.item_person, listKH);
        lv.setAdapter(adapter);
        progressDialog = new ProgressDialog(QLKHActivity.this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        getlistKH();

        //get TOP 3
        CollectionReference hoaDonRef = db.collection("HoaDon");
        hoaDonRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Double> maKHSumMap = new HashMap<>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    String maKH = document.getString("maKH");
                    Double tongTienS = document.getDouble("tongTien_S");

                    if (maKH != null && tongTienS != null) {
                        maKHSumMap.put(maKH, maKHSumMap.getOrDefault(maKH, 0.0) + tongTienS);
                    }
                }

                // Sort the maKHSumMap by values in descending order
                List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(maKHSumMap.entrySet());
                sortedEntries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

                // Get the top 3 maKH values with highest sum
                List<String> top3MaKH = new ArrayList<>();
                for (int i = 0; i < Math.min(sortedEntries.size(), 3); i++) {
                    top3MaKH.add(sortedEntries.get(i).getKey());
                }

                // Query the top 3 maKH directly in Firestore
                db.collection("khachHang")
                        .whereIn("maKH", top3MaKH)
                        .get()
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                List<khachHang> listTop3 = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task1.getResult()) {
                                    khachHang kh = document.toObject(khachHang.class);
                                    kh.setId(document.getId());
                                    listTop3.add(kh);
                                }
                                // Update UI with the results
                                updateUIWithTop3(listTop3, maKHSumMap);
                            } else {
                                Log.e("test", "Error getting documents: ", task1.getException());
                            }
                        });
            } else {
                Log.e("test", "Error getting documents: ", task.getException());
            }
        });
    }

    private void updateUIWithTop3(List<khachHang> listTop3, Map<String, Double> maKHSumMap) {
        ImageView logo1 = findViewById(R.id.imageView6);
        TextView ten1 = findViewById(R.id.tentop1);
        TextView tien1 = findViewById(R.id.mota);

        ImageView logo2 = findViewById(R.id.imgtop2);
        TextView ten2 = findViewById(R.id.tentop2);
        TextView tien2 = findViewById(R.id.mota2);

        ImageView logo3 = findViewById(R.id.imgtop3);
        TextView ten3 = findViewById(R.id.tentop3);
        TextView tien3 = findViewById(R.id.mota3);

        if (!listTop3.isEmpty()) {
            khachHang top1 = listTop3.get(0);
            logo1.setVisibility(View.VISIBLE);
            ten1.setVisibility(View.VISIBLE);
            ten1.setText(top1.getTenKH());
            ten1.setOnClickListener(view -> {
                Intent i = new Intent(this, CTKHActivity.class);
                i.putExtra("idKH", top1.getId());
                startActivity(i);
            });
            tien1.setVisibility(View.VISIBLE);
            Double sum1 = maKHSumMap.get(top1.getMaKH());
            String tienFormatted1 = formatCurrency(sum1);
            tien1.setText(tienFormatted1);

            if (listTop3.size() > 1) {
                khachHang top2 = listTop3.get(1);
                logo2.setVisibility(View.VISIBLE);
                ten2.setVisibility(View.VISIBLE);
                ten2.setText(top2.getTenKH());
                ten2.setOnClickListener(view -> {
                    Intent i = new Intent(this, CTKHActivity.class);
                    i.putExtra("idKH", top2.getId());
                    startActivity(i);
                });
                tien2.setVisibility(View.VISIBLE);
                Double sum2 = maKHSumMap.get(top2.getMaKH());
                String tienFormatted2 = formatCurrency(sum2);
                tien2.setText(tienFormatted2);
            }

            if (listTop3.size() > 2) {
                khachHang top3 = listTop3.get(2);
                logo3.setVisibility(View.VISIBLE);
                ten3.setVisibility(View.VISIBLE);
                ten3.setText(top3.getTenKH());
                ten3.setOnClickListener(view -> {
                    Intent i = new Intent(this, CTKHActivity.class);
                    i.putExtra("idKH", top3.getId());
                    startActivity(i);
                });
                tien3.setVisibility(View.VISIBLE);
                Double sum3 = maKHSumMap.get(top3.getMaKH());
                String tienFormatted3 = formatCurrency(sum3);
                tien3.setText(tienFormatted3);
            }
            if (progressDialog.isShowing()) progressDialog.dismiss();
        }
    }
    private String formatCurrency(double amount) {
        String numString = String.valueOf((int)amount);
        String tien = "";
        for (int i = 0; i < numString.length() ; i++){
            if((numString.length() - i - 1) % 3 == 0 && i < numString.length()-1){
                tien += Character.toString(numString.charAt(i)) + ".";
            }else{
                tien += Character.toString(numString.charAt(i));
            }
        }
        return tien + " vnđ";
    }

    private void getlistKH() {
        List<khachHang> tmp = new ArrayList<>();
        db.collection("khachHang")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            khachHang kh = document.toObject(khachHang.class);
                            kh.setId(document.getId());
                            tmp.add(kh);
                        }
                        listKH = tmp;
                        adapter = new KHAdapter(this, R.layout.item_person, listKH);
                        lv.setAdapter(adapter);
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                    } else {
                        Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
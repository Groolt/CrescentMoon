package com.example.damb_qlnh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.adapter.CateAdapter;
import com.example.damb_qlnh.models.monAn;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class UserCategories extends AppCompatActivity {
    private Button btnBack;
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private BottomNavigationView bottomNavigationView;
    private ImageButton imageButton;
    private SearchView searchView;
    private CateAdapter cateAdapter;
    private ArrayList<monAn> monAns;
    private FirebaseFirestore db;
    private String loaiMA;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_user_categories);
        Init();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(UserCategories.this, 2, GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        btnBack.setText(loaiMA);
        cateAdapter = new CateAdapter(this, monAns);
        recyclerView.setAdapter(cateAdapter);
        getListMon();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UserCategories.this, UserCart.class);
                startActivity(intent1);
            }

        });
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        startActivity(new Intent(UserCategories.this, UserHome.class));
                        break;
                    case R.id.action_history:
                        startActivity(new Intent(UserCategories.this, UserHistory.class));
                        break;
                    case R.id.action_order:
                        startActivity(new Intent(UserCategories.this, UserOrder.class));
                        break;
                    case R.id.action_profile:
                        startActivity(new Intent(UserCategories.this, UserProfile.class));
                        break;
                    case R.id.action_QR:
                        Toast.makeText(UserCategories.this, "QR",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
    private void filterList(String newText) {
        ArrayList<monAn> filteredList = new ArrayList<>();
        for (monAn mon : monAns) {
            if (mon.getTenMA().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(mon);
            }
        }
        if (filteredList.isEmpty()) {
            relativeLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            relativeLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            cateAdapter.setFilteredList(filteredList);
        }
    }
    public void Init(){
        btnBack = findViewById(R.id.btn_back_categories);
        recyclerView = findViewById(R.id.gdcategories_rcv);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        searchView = findViewById(R.id.gdcategories_searchview);
        relativeLayout = findViewById(R.id.gdcategories_rl);
        imageButton = findViewById(R.id.gdcategories_imgcart);
        db = FirebaseFirestore.getInstance();
        loaiMA = getIntent().getStringExtra("loaiMA");
        monAns = new ArrayList<>();
        progressDialog = new ProgressDialog(UserCategories.this);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserCategories.this, UserHome.class));
    }
    private void getListMon() {
        monAns.clear();
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        db.collection("monAn")
                .whereEqualTo("is_deleted", false)
                .whereEqualTo("loai", loaiMA)
                .orderBy("ten")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            monAns.add(new monAn(
                                    document.getId(),
                                    document.getString("ten"),
                                    document.getString("loai"),
                                    String.valueOf(document.getDouble("gia").intValue()),
                                    document.getString("img")));
                        }
                        cateAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
        progressDialog.dismiss();
    }
}
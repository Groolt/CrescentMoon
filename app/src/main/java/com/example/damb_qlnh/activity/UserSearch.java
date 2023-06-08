package com.example.damb_qlnh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.adapter.SearchAdapter;
import com.example.damb_qlnh.models.monAn;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class UserSearch extends AppCompatActivity {
    private Button btnBack;
    private RelativeLayout relativeLayout;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ArrayList<monAn> monAns;
    private SearchAdapter searchAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);
        btnBack = findViewById(R.id.btn_back_search);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        recyclerView = findViewById(R.id.gdsearch_rcv);
        searchView = findViewById(R.id.gdsearch_searchview);
        relativeLayout = findViewById(R.id.gdsearch_rl);
        db = FirebaseFirestore.getInstance();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(UserSearch.this, 2, GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        monAns = new ArrayList<>();
        searchAdapter = new SearchAdapter(this, monAns);
        recyclerView.setAdapter(searchAdapter);

        getListMon(); // lay tu db
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        startActivity(new Intent(UserSearch.this, UserHome.class));
                        break;
                    case R.id.action_history:
                        startActivity(new Intent(UserSearch.this, UserHistory.class));
                        break;
                    case R.id.action_order:
                        startActivity(new Intent(UserSearch.this, UserOrder.class));
                        break;
                    case R.id.action_profile:
                        startActivity(new Intent(UserSearch.this, UserProfile.class));
                        break;
                    case R.id.action_QR:
                        Toast.makeText(UserSearch.this, "QR",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserSearch.this, UserHome.class));
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
    }
    private void filterList(String newText) {
        ArrayList<monAn> filteredList = new ArrayList<>();
        for (monAn mon : monAns) {
            if (mon.getTenMA().toLowerCase().contains(newText.toLowerCase())
                    || mon.getLoaiMA().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(mon);
            }
        }
        if (filteredList.isEmpty()) {
            relativeLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            relativeLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            searchAdapter.setFilteredList(filteredList);
        }
    }
    private void getListMon() {
        monAns.clear();
        db.collection("monAn")
                .whereEqualTo("is_deleted", false)
                .orderBy("loai").orderBy("ten")
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
                        Toast.makeText(this, String.valueOf(monAns.size()), Toast.LENGTH_SHORT).show();
                        searchAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
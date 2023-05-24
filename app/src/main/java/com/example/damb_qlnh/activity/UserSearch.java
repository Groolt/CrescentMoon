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

import java.util.ArrayList;

public class UserSearch extends AppCompatActivity {
    private Button btnBack;
    private RelativeLayout relativeLayout;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ArrayList<monAn> monAns;
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);
        btnBack = findViewById(R.id.btn_back_search);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        recyclerView = findViewById(R.id.gdsearch_rcv);
        searchView = findViewById(R.id.gdsearch_searchview);
        relativeLayout = findViewById(R.id.gdsearch_rl);
        monAns = new ArrayList<>();
        monAns = getListMon(); // lay tu db
        GridLayoutManager gridLayoutManager = new GridLayoutManager(UserSearch.this, 2, GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        searchAdapter = new SearchAdapter(this, monAns);
        recyclerView.setAdapter(searchAdapter);
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
    private ArrayList<monAn> getListMon() {
        ArrayList<monAn> list = new ArrayList<>();
//        list.add(new monAn("1","Pizza", "Starter", "1", R.drawable.test));
//        list.add(new monAn("2","Pizza", "Starter", "1", R.drawable.test));
//        list.add(new monAn("3","Pizza", "Starter", "1", R.drawable.test));
//        list.add(new monAn("4","Pizza", "Starter", "1", R.drawable.test));
//        list.add(new monAn("5","Pizza", "Starter", "1", R.drawable.test));
//        list.add(new monAn("6","Pizzata", "Starter", "1", R.drawable.test));
        return list;
    }
}
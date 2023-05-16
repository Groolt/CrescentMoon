package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.damb_qlnh.adapter.monAdapterQLmon;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.hoaDon;
import com.example.damb_qlnh.models.monAn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QLMonActivity extends AppCompatActivity {

    ArrayList<monAn> monList;
    monAdapterQLmon adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlmon);

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //list mon an
        monList = getListMon();
        adapter = new monAdapterQLmon(this, R.layout.mon, monList);
        ListView lv = findViewById(R.id.lv);
        lv.setAdapter(adapter);

        //search
        SearchView searchView = findViewById(R.id.searchview);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        // Them mon an
        ImageView addbtn = findViewById(R.id.addbtn);
        addbtn.setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.them_mon_an);
            Window window = dialog.getWindow();
            if (window == null) return;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            CardView cancel = dialog.findViewById(R.id.cancel_button);
            cancel.setOnClickListener(view1 -> dialog.dismiss());
            dialog.show();
        });

    }

    private void filterList(String newText) {
        ArrayList<monAn> filteredList = new ArrayList<>();
        for (monAn mon : monList) {
            if (mon.getTenMA().toLowerCase().contains(newText.toLowerCase())
                    || mon.getLoaiMA().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(mon);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setFilteredList(filteredList);
        }
    }

    private ArrayList<monAn> getListMon() {
        ArrayList<monAn> list = new ArrayList<>();
        list.add(new monAn("1","Pizza", "Starter", "1", R.drawable.test));
        list.add(new monAn("2","Pizza", "Starter", "1", R.drawable.test));
        list.add(new monAn("3","Pizza", "Starter", "1", R.drawable.test));
        list.add(new monAn("4","Pizza", "Starter", "1", R.drawable.test));
        list.add(new monAn("5","Pizza", "Starter", "1", R.drawable.test));
        list.add(new monAn("6","Pizzata", "Starter", "1", R.drawable.test));
        return list;
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
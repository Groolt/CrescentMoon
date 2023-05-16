package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.damb_qlnh.adapter.BillAdapter;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.hoaDon;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TraCuuHDActivity extends AppCompatActivity {

    ArrayList<hoaDon> billList;
    BillAdapter billAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra_cuu_hdactivity);

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //list bill
        billList = getListHD();
        ListView lv = findViewById(R.id.lv);
        billAdapter = new BillAdapter(this, R.layout.item_hd, billList);
        lv.setAdapter(billAdapter);

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
    }

    private void filterList(String newText) {
        ArrayList<hoaDon> filteredList = new ArrayList<>();
        for (hoaDon bill : billList) {
            if (bill.getMaHD().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(bill);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            billAdapter.setFilteredList(filteredList);
        }
    }

    private ArrayList<hoaDon> getListHD() {
        ArrayList<hoaDon> list = new ArrayList<>();
        ArrayList<CTHD> cthds = new ArrayList<>();
        list.add(new hoaDon("HD01", "30/4/1975", "KH1", "NV1", "MB1", "", "100", "100", cthds));
        list.add(new hoaDon("HD02", "30/4/1975", "KH1", "NV1", "MB1", "", "100", "100", cthds));
        list.add(new hoaDon("HD03", "30/4/1975", "KH1", "NV1", "MB1", "", "100", "100", cthds));
        return list;
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
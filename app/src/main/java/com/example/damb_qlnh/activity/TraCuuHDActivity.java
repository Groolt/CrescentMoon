package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damb_qlnh.adapter.BillAdapter;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.hoaDon;
import com.example.damb_qlnh.models.vouCher;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TraCuuHDActivity extends AppCompatActivity {
    FirebaseFirestore db;
    final Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat formatDay = new SimpleDateFormat("dd/MM/yyyy");
    TextView sodon;
    ArrayList<hoaDon> billList;
    BillAdapter billAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tra_cuu_hdactivity);
        db = FirebaseFirestore.getInstance();

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //date picker
        TextView date = findViewById(R.id.date);
        DatePickerDialog.OnDateSetListener datePicker = (view1, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            if (formatDay.format(myCalendar.getTime()).contains(formatDay.format(Calendar.getInstance().getTime())))
                date.setText("Hôm nay");
            else
                date.setText(formatDay.format(myCalendar.getTime()));
            getListHD();
        };
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TraCuuHDActivity.this, datePicker, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        sodon = findViewById(R.id.sodon);

        //list bill
        billList = new ArrayList<>();
        ListView lv = findViewById(R.id.lv);
        billAdapter = new BillAdapter(this, R.layout.item_hd, billList);
        lv.setAdapter(billAdapter);
        getListHD();


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

    private void getListHD() {
        billList.clear();
        db.collection("HoaDon")
                .whereEqualTo("tinhTrang", 1)
                .orderBy("maHD")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getString("thoiGian").contains(formatDay.format(myCalendar.getTime()))){
                                hoaDon hoaDon = new hoaDon(
                                        document.getString("maHD"),
                                        document.getString("thoiGian"),
                                        document.getString("maKH"),
                                        document.getString("maBan"),
                                        String.valueOf(document.getDouble("tongTien_T").intValue()),
                                        String.valueOf(document.getDouble("tongTien_S").intValue())
                                );
                                if (document.get("maVoucher") != null)
                                    hoaDon.setMaVoucher(document.getString("maVoucher"));
                                billList.add(hoaDon);
                            }
                        }
                        billAdapter.notifyDataSetChanged();
                        sodon.setText(String.valueOf(billList.size()));
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
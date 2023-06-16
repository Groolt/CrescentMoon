package com.example.damb_qlnh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damb_qlnh.adapter.monAdapterCTBan;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.Phong;
import com.example.damb_qlnh.models.monAn;
import com.example.damb_qlnh.models.vouCher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class CTBanActivity extends AppCompatActivity {
    String maBan;
    String idBan;
    String phong;
    String maHD = null;
    FirebaseFirestore db;
    ListView lv;
    monAdapterCTBan adapter;
    TextView tv_total;
    ArrayList<CTHD> listCTHD1;
    ArrayList<CTHD> listCTHD2;
    int total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctban);
        db = FirebaseFirestore.getInstance();
        maBan = getIntent().getStringExtra("maBan");
        idBan = getIntent().getStringExtra("idBan");
        phong = getIntent().getStringExtra("phong");

        lv = findViewById(R.id.lv_mon);
        listCTHD1 = new ArrayList<>();
        listCTHD2 = new ArrayList<>();
        adapter = new monAdapterCTBan(this,R.layout.mon_ctban, listCTHD1);
        lv.setAdapter(adapter);
        getMaHD();

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tv = findViewById(R.id.toolbar_title);
        tv.setText("Bàn " + maBan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //switch tab
        CardView tab1 = findViewById(R.id.dangdat);
        CardView tab2 = findViewById(R.id.dapv);
        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tab1.getBackgroundTintList() ==
                        ColorStateList.valueOf(ContextCompat.getColor(CTBanActivity.this, R.color.white)))
                {
                    tab1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#fad745")));
                    tab2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                    adapter.setList(listCTHD1);
                }
            }
        });

        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tab2.getBackgroundTintList() ==
                        ColorStateList.valueOf(ContextCompat.getColor(CTBanActivity.this, R.color.white)))
                {
                    tab2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#fad745")));
                    tab1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                    adapter.setList(listCTHD2);
                }
            }
        });

        //
        db.collection("CTHD")
                .addSnapshotListener((snapshots, error) -> {
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        getlistMon();
                    }
                });

        //thanh toan
        CardView thanhtoan = findViewById(R.id.thanhtoan);
        thanhtoan.setOnClickListener(view -> {
            db.collection("HoaDon")
                    .whereEqualTo("maHD", maHD)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentReference d = db.collection("HoaDon").document(document.getId());
                                d.update("tinhTrang", 1);
                                d.update("thoiGian", new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime()));
                                d.update("tongTien_S", document.getDouble("tongTien_T").intValue());
                            }
                        } else {
                            Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                        }
                    });
            db.collection("banAn")
                    .whereEqualTo("phong", phong)
                    .get()
                    .addOnCompleteListener(task -> {
                        boolean b = false;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getString("maBan").equals(maBan)) {
                                db.collection("banAn").document(idBan).update("tinhTrang", 0);
                            }
                            else if (document.getDouble("tinhTrang").intValue() != 0) {
                                b = true;
                            }
                        }
                        if (!b) db.collection("phong").document(phong).update("tinhTrang", 0);
                    });
            onBackPressed();
        });
    }

    private void getMaHD() {
        db.collection("HoaDon")
                .whereEqualTo("tinhTrang", 0)
                .whereEqualTo("maBan", maBan)
                .get()
                .addOnCompleteListener(task -> {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        maHD = document.getString("maHD");
                    }
                    getlistMon();
                });
    }

    private void getlistMon() {
        total = 0;
        listCTHD1.clear();
        listCTHD2.clear();
        db.collection("CTHD")
                .whereEqualTo("maHD", maHD)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CTHD cthd = document.toObject(CTHD.class);
                            total += cthd.getSoLuong()*Integer.parseInt(cthd.getMonAn().getGiaTien());
                            if(cthd.getTinhTrang() == 0) {
                                if(listCTHD1.contains(cthd)){
                                    int index = listCTHD1.indexOf(cthd);
                                    cthd.setSoLuong(listCTHD1.get(index).getSoLuong() + cthd.getSoLuong());
                                    listCTHD1.remove(index);
                                }
                                listCTHD1.add(cthd);
                            } else {
                                if(listCTHD2.contains(cthd)){
                                    int index = listCTHD2.indexOf(cthd);
                                    cthd.setSoLuong(listCTHD2.get(index).getSoLuong() + cthd.getSoLuong());
                                    listCTHD2.remove(index);
                                }
                                listCTHD2.add(cthd);
                            }
                        }
                        String numString = String.valueOf(total);
                        String str = "";
                        for (int i = 0; i < numString.length() ; i++){
                            if((numString.length() - i - 1) % 3 == 0 && i < numString.length()-1){
                                str += Character.toString(numString.charAt(i)) + ".";
                            }else{
                                str += Character.toString(numString.charAt(i));
                            }
                        }
                        tv_total = findViewById(R.id.tongtien);
                        tv_total.setText(str + " vnđ");
                        adapter.notifyDataSetChanged();
                        adapter.setMaHD(maHD);
                    } else {
                        Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damb_qlnh.adapter.monAdapterCTBan;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.hoaDon;
import com.example.damb_qlnh.models.monAn;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CTHDActivity extends AppCompatActivity {
    FirebaseFirestore db;
    monAdapterCTBan adapter;
    ArrayList<CTHD> listCTHD;
    int total;
    TextView tv_total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cthdactivity);
        db = FirebaseFirestore.getInstance();

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //list mon an
        ListView lv = findViewById(R.id.lv);
        listCTHD = new ArrayList<>();
        adapter = new monAdapterCTBan(this,R.layout.mon_cthd, listCTHD);
        lv.setAdapter(adapter);
        getList();

        //Thong tin hoa don
        getInfoHD();
    }

    private void getInfoHD() {
        TextView info = findViewById(R.id.info);
        db.collection("khachHang")
                .whereEqualTo("maKH", getIntent().getExtras().getString("maKH"))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String tenKH = "";
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            tenKH = document.getString("tenKH");
                            break;
                        }
                        info.setText(getIntent().getExtras().getString("maHD")+"\n"
                                +getIntent().getExtras().getString("maKH")+"\n"
                                +tenKH+"\n"
                                +getIntent().getExtras().getString("ngayTT"));
                    } else {
                        Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getList() {
        total = 0;
        db.collection("CTHD")
                .whereEqualTo("maHD", getIntent().getExtras().getString("maHD"))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CTHD cthd = document.toObject(CTHD.class);
                            Map<String, Object> map = (Map<String, Object>) document.getData().get("monAn");
                            total += cthd.getSoLuong()*Integer.parseInt(cthd.getMonAn().getGiaTien());
                            if(listCTHD.contains(cthd)){
                                int index = listCTHD.indexOf(cthd);
                                cthd.setSoLuong(listCTHD.get(index).getSoLuong() + cthd.getSoLuong());
                                listCTHD.remove(index);
                            }
                            listCTHD.add(cthd);

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
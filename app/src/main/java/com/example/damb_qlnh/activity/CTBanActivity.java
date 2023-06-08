package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damb_qlnh.adapter.monAdapterCTBan;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.monAn;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CTBanActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ctban);
        ListView lv = findViewById(R.id.lv_mon);
      //  monAdapterCTBan adapter = new monAdapterCTBan(this,R.layout.mon, getlistMon());
        //lv.setAdapter(adapter);

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tv = findViewById(R.id.toolbar_title);
        tv.setText("Bàn 111.2");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);
    }

//    private ArrayList<CTHD> getlistMon() {
//        ArrayList<monAn> list = new ArrayList<>();
//        list.clear();
//        db.collection("monAn")
//                .whereEqualTo("is_deleted", false)
//                .orderBy("loai").orderBy("ten")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            list.add(new monAn(
//                                    document.getId(),
//                                    document.getString("ten"),
//                                    document.getString("loai"),
//                                    String.valueOf(document.getDouble("gia").intValue()),
//                                    document.getString("img")));
//                        }
//                    } else {
//                        Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
//                    }
//                });
//        return list;
//    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.monAn;
import com.example.damb_qlnh.models.nhanVien;
import com.example.damb_qlnh.models.vouCher;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class QLNVActivity extends AppCompatActivity {
    ArrayList<nhanVien> listBV;
    ArrayList<nhanVien> listPV;
    ArrayList<nhanVien> listTN;
    FirebaseFirestore db;
    public static boolean isBackFromCTNV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlnv);
        db = FirebaseFirestore.getInstance();
        isBackFromCTNV = false;

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //get list
        listBV = new ArrayList<>();
        listTN = new ArrayList<>();
        listPV = new ArrayList<>();
        renderListNV();

        // them 1 nhan vien moi
        ImageView addBtn = findViewById(R.id.addbtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(QLNVActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.form_them_nv);
                Window window = dialog.getWindow();
                if (window == null) return;
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Spinner spn = dialog.findViewById(R.id.gender);
                spn.setAdapter(new ArrayAdapter<>(QLNVActivity.this, R.layout.style_spinner_form, Arrays.asList("Nam", "Nữ", "Khác")));

                Spinner spn2 = dialog.findViewById(R.id.vitri);
                spn2.setAdapter(new ArrayAdapter<>(QLNVActivity.this, R.layout.style_spinner_form, Arrays.asList("Bảo vệ", "Thu ngân", "Phục vụ")));

                CardView cancel = dialog.findViewById(R.id.cancel_button);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                EditText ten = dialog.findViewById(R.id.ten);
                EditText sdt = dialog.findViewById(R.id.sdt);
                EditText luong = dialog.findViewById(R.id.luong);
                EditText cccd = dialog.findViewById(R.id.cccd);

                CardView confirm = dialog.findViewById(R.id.add_button);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("CCCD", cccd.getText().toString().trim());
                        data.put("Ten", ten.getText().toString().trim());
                        data.put("GioiTinh", spn.getSelectedItem().toString().trim());
                        data.put("ViTri", spn2.getSelectedItem().toString().trim());
                        data.put("SDT", sdt.getText().toString().trim());
                        data.put("Luong", Integer.parseInt(luong.getText().toString().trim()));
                        data.put("is_deleted", false);
                        db.collection("NhanVien").add(data);
                        dialog.dismiss();
                        recreate();
                    }
                });

                dialog.show();
            }
        });
    }

    private void renderListNV() {
        db.collection("NhanVien")
                .whereEqualTo("is_deleted", false)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            nhanVien nv = new nhanVien(
                                    document.getString("ViTri"),
                                    document.getString("Ten"),
                                    document.getId(),
                                    document.getString("GioiTinh"),
                                    document.getString("SDT"),
                                    document.getString("CCCD"),
                                    document.getDouble("Luong").intValue()
                            );
                            if (nv.getViTri().contains("Bảo vệ")) listBV.add(nv);
                            else if (nv.getViTri().contains( "Thu ngân")) listTN.add(nv);
                            else if (nv.getViTri().contains("Phục vụ")) listPV.add(nv);
                        }
                        for(nhanVien nv : listTN){
                            addToView(nv, (ViewGroup) findViewById(R.id.layout_thungan));
                        }
                        for(nhanVien nv : listPV){
                            addToView(nv, (ViewGroup) findViewById(R.id.layout_phucvu));
                        }
                        for(nhanVien nv : listBV){
                            addToView(nv, (ViewGroup) findViewById(R.id.layout_baove));
                        }
                    } else {
                        Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addToView(nhanVien nv, ViewGroup viewGroup) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_person, viewGroup, false);
        TextView ten = view.findViewById(R.id.ten);
        CircleImageView avatar = view.findViewById(R.id.avatar);
        ten.setText(nv.getTenNV());
        switch (nv.getViTri()){
            case "Bảo vệ": avatar.setImageResource(R.drawable.bv); break;
            case "Phục vụ": avatar.setImageResource(R.drawable.pv); break;
            case "Thu ngân": avatar.setImageResource(R.drawable.thungan); break;
            default: avatar.setImageResource(R.drawable.person); break;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QLNVActivity.this, CTNVActivity.class);
                intent.putExtra("nhanVien", nv);
                startActivity(intent);
            }
        });
        viewGroup.addView(view);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isBackFromCTNV) recreate();
    }
}
package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.nhanVien;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CTNVActivity extends AppCompatActivity {
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctnv);
        db = FirebaseFirestore.getInstance();
        QLNVActivity.isBackFromCTNV = true;
        //show info
        Intent intent = getIntent();
        nhanVien nhanVien = (nhanVien) intent.getSerializableExtra("nhanVien");
        ImageView avatar = findViewById(R.id.circleImageView);
        switch (nhanVien.getViTri()){
            case "Bảo vệ": avatar.setImageResource(R.drawable.bv); break;
            case "Phục vụ": avatar.setImageResource(R.drawable.pv); break;
            case "Thu ngân": avatar.setImageResource(R.drawable.thungan); break;
            default: avatar.setImageResource(R.drawable.person); break;
        }
        TextView ten = findViewById(R.id.ten);
        ten.setText(nhanVien.getTenNV());
        TextView viTri = findViewById(R.id.vitri);
        viTri.setText(nhanVien.getViTri());
        TextView luong = findViewById(R.id.luong);

        String numString = String.valueOf(nhanVien.getLuong());
        String strLuong ="";
        for (int i = 0; i < numString.length() ; i++){
            if((numString.length() - i - 1) % 3 == 0 && i < numString.length()-1){
                strLuong += Character.toString(numString.charAt(i)) + ".";
            }else{
                strLuong += Character.toString(numString.charAt(i));
            }
        }
        luong.setText(strLuong + " đ");
        TextView gender = findViewById(R.id.gender);
        gender.setText(nhanVien.getGioiTinh());
        TextView sdt = findViewById(R.id.sdt);
        sdt.setText(nhanVien.getSdt());
        TextView cccd = findViewById(R.id.cccd);
        cccd.setText(nhanVien.getCccd());

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //edit NV
        ImageView editBtn = findViewById(R.id.btn_edit);
        editBtn.setOnClickListener(view -> {
            Dialog dialog = new Dialog(CTNVActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.form_sua_nv);
            Window window = dialog.getWindow();
            if (window == null) return;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            Spinner spn = dialog.findViewById(R.id.gender);
            spn.setAdapter(new ArrayAdapter<>(CTNVActivity.this, R.layout.style_spinner_form, Arrays.asList("Nam", "Nữ", "Khác")));
            spn.setSelection(Arrays.asList("Nam", "Nữ", "Khác").indexOf(gender.getText()));

            Spinner spn2 = dialog.findViewById(R.id.vitri);
            spn2.setAdapter(new ArrayAdapter<>(CTNVActivity.this, R.layout.style_spinner_form, Arrays.asList("Bảo vệ", "Thu ngân", "Phục vụ")));
            spn2.setSelection(Arrays.asList("Bảo vệ", "Thu ngân", "Phục vụ").indexOf(viTri.getText()));

            CardView cancel = dialog.findViewById(R.id.cancel_button);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            EditText tenDialog = dialog.findViewById(R.id.ten);
            tenDialog.setText(ten.getText());
            EditText sdtDialog = dialog.findViewById(R.id.sdt);
            sdtDialog.setText(sdt.getText());
            EditText luongDialog = dialog.findViewById(R.id.luong);
            luongDialog.setText(luong.getText().toString().replaceAll("[^0-9]", ""));
            EditText cccdDialog = dialog.findViewById(R.id.cccd);
            cccdDialog.setText(cccd.getText());
            CardView confirm = dialog.findViewById(R.id.add_button);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String, Object> newdata = new HashMap<>();
                    newdata.put("CCCD", cccdDialog.getText().toString().trim());
                    newdata.put("Ten", tenDialog.getText().toString().trim());
                    newdata.put("GioiTinh", spn.getSelectedItem().toString().trim());
                    newdata.put("ViTri", spn2.getSelectedItem().toString().trim());
                    newdata.put("SDT", sdtDialog.getText().toString().trim());
                    newdata.put("Luong", Integer.parseInt(luongDialog.getText().toString().trim()));
                    newdata.put("is_deleted", false);
                    DocumentReference documentRef = db.collection("NhanVien").document(nhanVien.getMaNV());
                    documentRef.update(newdata)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    switch (spn2.getSelectedItem().toString().trim()){
                                        case "Bảo vệ": avatar.setImageResource(R.drawable.bv); break;
                                        case "Phục vụ": avatar.setImageResource(R.drawable.pv); break;
                                        case "Thu ngân": avatar.setImageResource(R.drawable.thungan); break;
                                        default: avatar.setImageResource(R.drawable.person); break;
                                    }
                                    String numString = luongDialog.getText().toString().trim();
                                    String strLuong = "";
                                    for (int i = 0; i < numString.length() ; i++){
                                        if((numString.length() - i - 1) % 3 == 0 && i < numString.length()-1){
                                            strLuong += Character.toString(numString.charAt(i)) + ".";
                                        }else{
                                            strLuong += Character.toString(numString.charAt(i));
                                        }
                                    }
                                    luong.setText(strLuong + " đ");
                                    gender.setText(spn.getSelectedItem().toString().trim());
                                    sdt.setText(sdtDialog.getText().toString().trim());
                                    cccd.setText(cccdDialog.getText().toString().trim());
                                    ten.setText(tenDialog.getText().toString().trim());
                                    viTri.setText(spn2.getSelectedItem().toString().trim());
                                    Toast.makeText(CTNVActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CTNVActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                    dialog.dismiss();
                }
            });
            dialog.show();
        });

        //xoa NV
        ImageView delBtn = findViewById(R.id.btn_delete);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CTNVActivity.this);
                builder.setTitle("Xóa nhân viên");
                builder.setMessage("Bạn có đồng ý xóa nhân viên này?");
                builder.setPositiveButton("Đồng ý", (dialog, which) -> {
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    DocumentReference documentRef = firestore.collection("NhanVien").document(nhanVien.getMaNV());

                    documentRef.update("is_deleted", true)
                            .addOnSuccessListener(aVoid -> {
                                onBackPressed();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(CTNVActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                            });
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
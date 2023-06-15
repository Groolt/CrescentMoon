package com.example.damb_qlnh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.adapter.CartAdapter;
import com.example.damb_qlnh.adapter.CateAdapter;
import com.example.damb_qlnh.adapter.uservoucherAdapter;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.banAn;
import com.example.damb_qlnh.models.hoaDon;
import com.example.damb_qlnh.models.monAn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UserCart extends AppCompatActivity {
    private Button btnBack;
    private RecyclerView recyclerView;
    private CardView cardViewOrder, cardViewPayment;
    private BottomNavigationView bottomNavigationView;
    private ArrayList<CTHD> cthds;
    private ImageButton imageButton;
    private FirebaseFirestore db;
    ProgressDialog progressDialog;
    private String maHD;
    private String maKH;
    private com.example.damb_qlnh.models.banAn banAn;
    private CartAdapter cartAdapter;
    private static int totalMoney = 0;
    public static int getTotalMoney(){
        return totalMoney;
    }
    final Calendar myCalendar= Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_user_cart);
        init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        cartAdapter = new CartAdapter(UserCart.this, cthds);
        recyclerView.setAdapter(cartAdapter);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cardViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertCTHDHoaDon();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserCart.this, UserVoucher.class));
            }
        });
        cardViewPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalMoney != 0)
                {
                    showDialog();
                }
                else{
                    Toast.makeText(UserCart.this, "Vui lòng gọi món trước khi thanh toán", Toast.LENGTH_SHORT).show();
                }

            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        startActivity(new Intent(UserCart.this, UserHome.class));
                        break;
                    case R.id.action_history:
                         startActivity(new Intent(UserCart.this, UserHistory.class));
                        break;
                    case R.id.action_order:
                        startActivity(new Intent(UserCart.this, UserOrder.class));
                        break;
                    case R.id.action_profile:
                        startActivity(new Intent(UserCart.this, UserProfile.class));
                        break;
                    case R.id.action_QR:
                          Toast.makeText(UserCart.this, "QR",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
    public void showDialog(){
        SharedPreferences prefs = getSharedPreferences("dba", MODE_PRIVATE);
        Dialog dialog = new Dialog(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custome_dialog_payment);
        Button btnCheckout = dialog.findViewById(R.id.diaopayment_btnCheck);
        Button btnVoucher = dialog.findViewById(R.id.diaopay_btnvoucher);
        TextView txtTotal_T = dialog.findViewById(R.id.diapay_txtsub);
        TextView txtTotal_S = dialog.findViewById(R.id.diapay_txttotal);
        TextView txtTax = dialog.findViewById(R.id.diapay_txttax);
        TextView txtServiceFee = dialog.findViewById(R.id.diapay_txtserfee);
        TextView txtuseVoucer = dialog.findViewById(R.id.diapay_txtvoucher);
            Double tax = totalMoney*0.1;
            txtTax.setText(formatMoney(String.valueOf(tax.intValue())));
            txtServiceFee.setText(formatMoney("50000"));
            Double money = totalMoney + tax + 50000;
            txtTotal_T.setText(formatMoney(String.valueOf(totalMoney)));
            txtTotal_S.setText(formatMoney(String.valueOf(money.intValue())));
            if(uservoucherAdapter.getVouCher().getGiaTri() != -1){
                btnVoucher.setVisibility(View.GONE);
                txtuseVoucer.setVisibility(View.VISIBLE);
                txtuseVoucer.setText("-" + uservoucherAdapter.getVouCher().getGiaTri() + "%");
                int newMoney = money.intValue() * (100 - uservoucherAdapter.getVouCher().getGiaTri())/100;
                txtTotal_S.setText(formatMoney(String.valueOf(newMoney)));
            }
            btnVoucher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserCart.this, UserVoucher.class));
                    }
            });
            btnCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //gui yeu cau thanh toan, up tong hoa don len firebase
                    prefs.edit().remove("GTVC").commit();
                    dialog.dismiss();
                    // set lai tinh trang ban`, cap nhat lai hoa don
                    // them voucher da su dung vao danh sach voucher
                    // cap nhat thong tin khach hang
                }
            });
        dialog.show();
    }
    public void init(){
        btnBack = findViewById(R.id.btn_back_cart);
        recyclerView = findViewById(R.id.gdcart_rcv);
        cardViewOrder = findViewById(R.id.gdcart_btnorder);
        cardViewPayment = findViewById(R.id.gdcart_btnpayment);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        imageButton = findViewById(R.id.gdcart_imgcart);
        cthds = CateAdapter.spSelected();
        progressDialog = new ProgressDialog(UserCart.this);
        SharedPreferences prefs = getSharedPreferences("dba", MODE_PRIVATE);
        maKH = prefs.getString("maKH", "#KH");
        banAn = UserHome.getBanAn();
        db = FirebaseFirestore.getInstance();
        maHD = prefs.getString("maHD", "#HD");
    }
    public String formatMoney(String input){
        StringBuilder result = new StringBuilder();
        int length = input.length();
        int dotCount = length / 3;

        int remainingDigits = length % 3;
        if (remainingDigits != 0) {
            result.append(input, 0, remainingDigits);
            if (dotCount > 0) {
                result.append(".");
            }
        }

        for (int i = 0; i < dotCount; i++) {
            int startIndex = remainingDigits + i * 3;
            result.append(input.substring(startIndex, startIndex + 3));
            if (i < dotCount - 1) {
                result.append(".");
            }
        }
        return result.toString();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserCart.this, UserHome.class));
    }
    public void InsertCTHDHoaDon() {
        db.collection("HoaDon")
                .whereEqualTo("maKH", maKH)
                .whereEqualTo("tinhTrang", 0)
                .count()
                .get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Count fetched successfully
                            AggregateQuerySnapshot snapshot = task.getResult();
                            if (snapshot.getCount() == 0) {
                                InsertHD();
                            }
                            for (CTHD cthd : cthds){
                                cthd.setMaHD(maHD);
                                totalMoney += cthd.getSoLuong() * Integer.parseInt(cthd.getMonAn().getGiaTien());
                                db.collection("CTHD").add(cthd);
                            }
                            db.collection("phong").document(banAn.getPhong()).update("tinhTrang", 1);
                            db.collection("banAn").document(banAn.getId()).update("tinhTrang", 2);
                            cthds.clear();
                            cartAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    public void InsertHD(){
        Map<String, Object> data = new HashMap<>();
        data.put("maBan", banAn.getMaBan());
        data.put("maHD", maHD);
        data.put("maKH", maKH);
        data.put("maVoucher", null);
        data.put("thoiGian", new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
        data.put("tongTien_S", totalMoney);
        data.put("tongTien_T", totalMoney);
        data.put("tinhTrang", 0);
        db.collection("HoaDon").add(data);
    }
}
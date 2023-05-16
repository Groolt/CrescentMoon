package com.example.damb_qlnh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
import com.example.damb_qlnh.models.CTHD;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class UserCart extends AppCompatActivity {
    private Button btnBack;
    private RecyclerView recyclerView;
    private CardView cardViewOrder, cardViewPayment;
    private BottomNavigationView bottomNavigationView;
    private ArrayList<CTHD> cthds;
    private ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_user_cart);
        init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        CartAdapter cartAdapter = new CartAdapter(UserCart.this, cthds);
        recyclerView.setAdapter(cartAdapter);
//        Gson gson = new Gson();
//        SharedPreferences prefs = getSharedPreferences("dba", Context.MODE_PRIVATE);
//        String json = prefs.getString("CTHD", "");
//        CTHD cthd = gson.fromJson(json, CTHD.class);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cardViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //lay tat ca cthd tu firebase tinh trang la chua dat, sau do tao goiMon(cthd, maBan), set tinh trang goi mon cua cthd la da goi mon
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
                showDialog();
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
        //lay tat ca cthd xuong -> setdata
        btnVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int GTVC = prefs.getInt("GTVC", -1);
                if(GTVC == -1){
                    startActivity(new Intent(UserCart.this, UserVoucher.class));
                }
                else{
                    btnVoucher.setVisibility(View.GONE);
                    txtuseVoucer.setVisibility(View.VISIBLE);
                    txtuseVoucer.setText("-" + Integer.toString(GTVC) + "%");
                }
            }
        });
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gui yeu cau thanh toan, up tong hoa don len firebase
                prefs.edit().remove("GTVC").commit();
                dialog.dismiss();
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
        cthds = new ArrayList<>(); // lay tat ca cthd tren firesbase tinh trang la chua dat xuong
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserCart.this, UserHome.class));
    }
}
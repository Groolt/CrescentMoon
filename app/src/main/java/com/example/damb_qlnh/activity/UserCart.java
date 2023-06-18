package com.example.damb_qlnh.activity;

import androidx.activity.result.ActivityResultLauncher;
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
import android.util.Log;
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
import com.example.damb_qlnh.models.vouCher;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import vn.momo.momo_partner.AppMoMoLib;

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
    private SharedPreferences prefs;
    private com.example.damb_qlnh.models.banAn banAn;
    private CartAdapter cartAdapter;
    private static int totalMoney = 0;
    public static int getTotalMoney(){
        return totalMoney;
    }
    final Calendar myCalendar= Calendar.getInstance();
    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "Crescent Moon";
    private String merchantCode = "SCB01";
    private String merchantNameLabel = "Dịch vụ";
    private String description = "Thanh toán hóa đơn";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_user_cart);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
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
                          scanCode();
                        break;
                }
                return true;
            }
        });
    }
    public void showDialog(){

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
            int tax = Math.round(totalMoney/10000 ) * 1000;
            txtTax.setText(formatMoney(String.valueOf(tax)));
            txtServiceFee.setText(formatMoney("50000"));
            int money = (tax + totalMoney + 50000);
            txtTotal_T.setText(formatMoney(String.valueOf(totalMoney)));
            int newMoney = (tax + totalMoney + 50000);
            txtTotal_S.setText(formatMoney(String.valueOf(money)));
            if(uservoucherAdapter.getVouCher().getGiaTri() != -1){
                btnVoucher.setVisibility(View.GONE);
                txtuseVoucer.setVisibility(View.VISIBLE);
                txtuseVoucer.setText("- " + uservoucherAdapter.getVouCher().getGiaTri() + "%");
                newMoney = Math.round(money/1000 * (100 - uservoucherAdapter.getVouCher().getGiaTri())/100) * 1000;
                txtTotal_S.setText(formatMoney(String.valueOf(newMoney)));
            }
            btnVoucher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserCart.this, UserVoucher.class));
                    }
            });
        int finalNewMoney = newMoney;
        btnCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestPayment();
                    dialog.dismiss();
                    db.collection("HoaDon").document(prefs.getString("idHD", "")).update("tongTien_T", totalMoney);
                    db.collection("HoaDon").document(prefs.getString("idHD", "")).update("tongTien_S", finalNewMoney);
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
        prefs = getSharedPreferences("dba", MODE_PRIVATE);
        maKH = UserHome.getKhachHang().getMaKH();
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
        db.collection("HoaDon").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                prefs.edit().putString("idHD", documentReference.getId()).commit();
            }
        });
    }
    public void updateRank(){
        db.collection("HoaDon").whereEqualTo("maKH", maKH).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int Money = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                           if(document.exists()){
                               Money += document.getLong("tongTien_S").intValue();
                           }
                        }
                        if(Money > 100000000){
                            db.collection("khachHang").document(UserHome.getKhachHang().getId()).update("xepHang", "Ruby");
                        } else if (Money > 50000000) {
                            db.collection("khachHang").document(UserHome.getKhachHang().getId()).update("xepHang", "Diamond");
                        }else if (Money > 30000000) {
                            db.collection("khachHang").document(UserHome.getKhachHang().getId()).update("xepHang", "Gold");
                        }else if (Money > 50000000) {
                            db.collection("khachHang").document(UserHome.getKhachHang().getId()).update("xepHang", "Silver");
                        }
                    } else {
                        Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }
    private ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() !=null) {
            UserHome.setmaBan(result.getContents().toString().trim());
        }
    });
    private void requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amount); //Kiểu integer
        eventValue.put("orderId", prefs.getString("idHD", "")); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", maHD); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", fee); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description
        //client extra data
        eventValue.put("requestId",  merchantCode +"merchant_billId_"+ System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);

    }
    //Get token callback from MoMo app an submit to server side
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    Log.e("Thành công", data.getStringExtra("message"));
                    String token = data.getStringExtra("data"); //Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if(env == null){
                        env = "app";
                    }
                    db.collection("banAn").document(banAn.getId()).update("tinhTrang", 0);
                    db.collection("HoaDon").document(prefs.getString("idHD", "")).update("tinhTrang", 1);
                    if(uservoucherAdapter.getVouCher().getGiaTri() != -1){
                        db.collection("HoaDon").document(prefs.getString("idHD", "")).update("maVoucher", uservoucherAdapter.getVouCher().getMaVoucher());
                        db.collection("HoaDon").document(prefs.getString("idHD", "")).update("soLuong", uservoucherAdapter.getVouCher().getSoLuong() - 1);
                        Map<String, String> data1 = new HashMap<>();
                        data1.put("maKH", maKH);
                        data1.put("maVoucher", uservoucherAdapter.getVouCher().getMaVoucher());
                        db.collection("dsvouCher").add(data1);
                    }
                    updateRank();
                    UserHome.setBanAn();
                    uservoucherAdapter.setVouCher();
                    prefs.edit().remove("maHD").commit();
                    if(token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                    } else {
                        Log.e("2", "thất bại");
                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    Log.e("2", "thất bại");
                } else if(data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    Log.e("2", "thất bại");
                } else {
                    //TOKEN FAIL
                    Log.e("2", "thất bại");
                }
            } else {
                Log.e("2", "thất bại");
            }
        } else {
            Log.e("2", "thất bại");
        }
    }
}
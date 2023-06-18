package com.example.damb_qlnh.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.datBan;
import com.example.damb_qlnh.models.hoaDon;
import com.example.damb_qlnh.models.khachHang;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UserOrder extends AppCompatActivity {
    private Button btnBack;
    private EditText txtName, txtPhone, txtDate, txtNote, txtNum;
    private Spinner spinner;
    final Calendar myCalendar= Calendar.getInstance();
    String timeChoice;
    private BottomNavigationView bottomNavigationView;
    private CardView cardViewSend;
    private ProgressDialog progressDialog;
    private datBan datBan;
    private ArrayList<String> maBan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);
        init();
        setData(UserHome.getKhachHang());
        ArrayList<String> opening = new ArrayList<>();
        opening.add("9:00 - 15:00");
        opening.add("19:00 -- 23:00");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeChoice = opening.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       ArrayAdapter<String> adapter = new ArrayAdapter<>(UserOrder.this, android.R.layout.simple_spinner_dropdown_item, opening);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinner.setAdapter(adapter);
       DatePickerDialog.OnDateSetListener date = (view1, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            txtDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
        };
       txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UserOrder.this, date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
       btnBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onBackPressed();
           }
       });
       cardViewSend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //gui thong bao nha hang
               datBan = new datBan(txtName.getText().toString().trim(), txtPhone.getText().toString().trim(), txtDate.getText().toString().trim(),
                        Integer.parseInt(txtNum.getText().toString().trim()), timeChoice, txtNote.getText().toString().trim(),
                       "", UserHome.getKhachHang().getId(), UserHome.getKhachHang().getImg());
               progressDialog.setTitle("Loading...");
               progressDialog.show();
               FirebaseFirestore db = FirebaseFirestore.getInstance();
               db.collection("datBan").whereEqualTo("date", datBan.getDate())
                       .whereEqualTo("time", datBan.getTime())
                       .get() .addOnCompleteListener(task -> {
                           if (task.isSuccessful()) {
                               for (QueryDocumentSnapshot document : task.getResult()) {
                                   maBan.add(document.get("maBan").toString());
                               }
                               datBan();
                           }
                           else {
                               Toast.makeText(UserOrder.this, "Can't get data", Toast.LENGTH_SHORT).show();
                           }
                       });
               progressDialog.dismiss();
           }
       });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        startActivity(new Intent(UserOrder.this, UserHome.class));
                        break;
                    case R.id.action_history:
                        startActivity(new Intent(UserOrder.this, UserHistory.class));
                        break;
                    case R.id.action_profile:
                        startActivity(new Intent(UserOrder.this, UserProfile.class));
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
        dialog.setContentView(R.layout.custome_dialog_order);
        Button btnOkay = dialog.findViewById(R.id.diaorder_btnOkay);
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
    public void init(){
        btnBack = findViewById(R.id.btn_back_order);
        txtName = findViewById(R.id.gdord_txtname);
        txtDate = findViewById(R.id.gdord_txtdmy);
        txtPhone = findViewById(R.id.gdord_txtphone);
        txtNote = findViewById(R.id.gdord_txtnote);
        txtNum = findViewById(R.id.gdord_txtnum);
        spinner = findViewById(R.id.gdord_spinner);
        cardViewSend = findViewById(R.id.gdord_btnsend);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        maBan = new ArrayList<>();
        maBan.add("test");
        bottomNavigationView.setSelectedItemId(R.id.action_order);
        progressDialog = new ProgressDialog(UserOrder.this);
    }
    public void setData(khachHang khachHang){
        txtName.setText(khachHang.getTenKH().toString().trim());
        txtPhone.setText(khachHang.getSDT().toString().trim());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY");
        Date date = new Date();
        txtDate.setText(format.format(date));
        txtNum.setText("0");
        txtNote.setText("");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserOrder.this, UserHome.class));
    }

    public void datBan(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("banAn").whereGreaterThanOrEqualTo("loaiBan", datBan.getNum())
                .orderBy("loaiBan")
                .orderBy("maBan")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.exists()){
                                if (!maBan.contains(document.get("maBan")))
                                {
                                    datBan.setMaBan(document.get("maBan").toString());
                                    db.collection("datBan").add(datBan);
                                    break;
                                }
                            }
                        }
                        showDialog();
                        setData(UserHome.getKhachHang());
                    }
                    else {
                        Toast.makeText(UserOrder.this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
package com.example.damb_qlnh.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.adapter.CategoriesAdapter;
import com.example.damb_qlnh.adapter.PopularAdapter;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.Categories;
import com.example.damb_qlnh.models.banAn;
import com.example.damb_qlnh.models.hoaDon;
import com.example.damb_qlnh.models.khachHang;
import com.example.damb_qlnh.models.monAn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserHome extends AppCompatActivity {
    private TextView txtName, txtID;
    private SearchView searchView;
    private ImageButton imageButton;
    private CircleImageView circleImageView;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView rcvCategories, rcvPopular;
    private ArrayList<Categories> categories;
    private ArrayList<monAn> monAns;
    private static khachHang khachHang;
    public static khachHang getKhachHang(){
        return khachHang;
    }
    private static com.example.damb_qlnh.models.banAn banAn;
    public static banAn getBanAn(){
        return banAn;
    }
    private FirebaseFirestore db;
    ProgressDialog progressDialog;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    private PopularAdapter popularAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(UserHome.this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(UserHome.this, RecyclerView.HORIZONTAL, false);
        rcvCategories.setLayoutManager(layoutManager);
        rcvPopular.setLayoutManager(layoutManager1);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(UserHome.this, UserProfile.class);
                startActivity(intent2);
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserHome.this, UserCart.class));
            }
        });
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(this, categories);
        rcvCategories.setAdapter(categoriesAdapter);
        popularAdapter = new PopularAdapter(this, monAns);
        rcvPopular.setAdapter((popularAdapter));
        getCTHD();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_history:
                        startActivity(new Intent(UserHome.this, UserHistory.class));
                        break;
                    case R.id.action_order:
                        startActivity(new Intent(UserHome.this, UserOrder.class));
                        break;
                    case R.id.action_profile:
                        Intent intent1 = new Intent(UserHome.this, UserProfile.class);
                        intent1.putExtra("khachHang", khachHang);
                        startActivity(intent1);
                        break;
                    case R.id.action_QR:
                        scanCode();
                        break;
                }
                return true;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtID.getText().toString().isEmpty()){
                    Toast.makeText(UserHome.this, "Vui lòng chọn mã bàn trước khi thực hiện các thao tác khác", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(UserHome.this, UserSearch.class));
            }
        });
        txtID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                db.collection("banAn").whereEqualTo("maBan", txtID.getText().toString().trim())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    banAn.setMaBan(document.get("maBan").toString());
                                    banAn.setPhong(document.get("phong").toString());
                                    banAn.setTang(document.getLong("tang").intValue());
                                    banAn.setId(document.getId());
                                }
                            } else {
                                Toast.makeText(UserHome.this, "Can't get data", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
    public void init(){
        progressDialog = new ProgressDialog(UserHome.this);
        txtName = findViewById(R.id.gdhome_txtname);
        txtID = findViewById(R.id.gdhome_txtid);
        searchView = findViewById(R.id.gdhome_searchview);
        circleImageView = findViewById(R.id.gdhome_imgUser);
        imageButton = findViewById(R.id.gdhome_cart);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        rcvCategories = findViewById(R.id.gdhome_rcv1);
        rcvPopular = findViewById(R.id.gdhome_rcv2);
        categories = new ArrayList<>();
        monAns = new ArrayList<>();
        categories.add(Categories.CATEGORIES1);
        categories.add(Categories.CATEGORIES2);
        categories.add(Categories.CATEGORIES3);
        categories.add(Categories.CATEGORIES4);
        categories.add(Categories.CATEGORIES5);
        db = FirebaseFirestore.getInstance();
        khachHang = new khachHang();
        getUser();
        if(banAn == null){
            banAn = new banAn("", -1, -1, "", -1, "");
        }
        txtID.setText(banAn.getMaBan());
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserHome.this);
        builder.setTitle("Đăng xuất");
        builder.setMessage("Bạn có đồng ý đăng xuất không?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserHome.this, MainActivity.class));
                finish();
            }
        });
       builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               dialog.cancel();
           }
       });
       AlertDialog alertDialog = builder.create();
       alertDialog.show();
    }
    public void getUser(){
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        db.collection("khachHang").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                khachHang.setTenKH(document.get("tenKH").toString());
                                khachHang.setMaKH(document.get("maKH").toString());
                                khachHang.setSDT(document.get("sdt").toString());
                                khachHang.setXepHang(document.get("xepHang").toString());
                                khachHang.setGioiTinh(document.get("gioiTinh").toString());
                                khachHang.setDob(document.get("dob").toString());
                                khachHang.setImg(document.get("img").toString());
                                txtName.setText("Hi " + khachHang.getTenKH().toString().trim());
                                SharedPreferences prefs = getSharedPreferences("dba", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("maKH", khachHang.getMaKH());
                                editor.commit();
                                Glide.with(UserHome.this).load(khachHang.getImg()).into(circleImageView);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        progressDialog.dismiss();
    }
    public void getCTHD(){
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        monAns.clear();
        db.collection("CTHD")
                .get() .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        HashMap<monAn, Integer> data = new HashMap<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CTHD cthd = document.toObject(CTHD.class);
                            int n = 0;
                            if(data.containsKey(cthd.getMonAn()))
                            {
                                n = data.get(cthd.getMonAn()).intValue();
                            }
                            data.put(cthd.getMonAn(), n + cthd.getSoLuong());
                        }
                        List<Map.Entry<monAn, Integer>> entryList = new ArrayList<>(data.entrySet());
                        Collections.sort(entryList, new Comparator<Map.Entry<monAn, Integer>>() {
                            @Override
                            public int compare(Map.Entry<monAn, Integer> entry1, Map.Entry<monAn, Integer> entry2) {
                                return entry2.getValue().compareTo(entry1.getValue());
                            }
                        });
                        int cnt = 0;
                        for (Map.Entry<monAn, Integer> entry : entryList) {
                            if (cnt == 6){
                                break;
                            }
                            if(!monAns.contains(entry.getKey())){
                                cnt++;
                                monAns.add(entry.getKey());
                                Log.e("1", entry.getValue().toString());
                            }
                        }
                        popularAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(UserHome.this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
        progressDialog.dismiss();
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
            txtID.setText(result.getContents().toString().trim());
        }
    });
}

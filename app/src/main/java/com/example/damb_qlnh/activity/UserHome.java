package com.example.damb_qlnh.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.damb_qlnh.models.Categories;
import com.example.damb_qlnh.models.khachHang;
import com.example.damb_qlnh.models.monAn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
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
    private com.example.damb_qlnh.models.khachHang khachHang;
    private FirebaseFirestore db;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        init();
        getUser();
        LinearLayoutManager layoutManager = new LinearLayoutManager(UserHome.this, RecyclerView.HORIZONTAL, false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(UserHome.this, RecyclerView.HORIZONTAL, false);
        rcvCategories.setLayoutManager(layoutManager);
        rcvPopular.setLayoutManager(layoutManager1);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(UserHome.this, UserProfile.class);
                intent2.putExtra("khachHang", khachHang);
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
        PopularAdapter popularAdapter = new PopularAdapter(this, monAns);
        rcvPopular.setAdapter((popularAdapter));
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
                startActivity(new Intent(UserHome.this, UserSearch.class));
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

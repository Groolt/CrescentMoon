package com.example.damb_qlnh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.adapter.CategoriesAdapter;
import com.example.damb_qlnh.adapter.PopularAdapter;
import com.example.damb_qlnh.models.Categories;
import com.example.damb_qlnh.models.monAn;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

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
                startActivity(new Intent(UserHome.this, UserProfile.class));
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
                        startActivity(new Intent(UserHome.this, UserProfile.class));
                        break;
                    case R.id.action_QR:
                        Toast.makeText(UserHome.this, "QR",Toast.LENGTH_SHORT).show();
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
//        monAns.add(new monAn("1","Pizza", "Main Course", "1", R.drawable.test));
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
}
package com.example.damb_qlnh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.damb_qlnh.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeAdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        mAuth = FirebaseAuth.getInstance();
        //set up navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        MenuItem homeItem = navigationView.getMenu().findItem(R.id.nav_home);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.nav_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setItemIconTintList(null);

        //custom logout item when start
        MenuItem logoutItem = navigationView.getMenu().findItem(R.id.nav_logout);
        Drawable wrapped = DrawableCompat.wrap(logoutItem.getIcon());
        DrawableCompat.setTint(wrapped, Color.rgb(201,57,57));
        logoutItem.setIcon(wrapped);

        //custom home item when start
        homeItem.setChecked(true);
        wrapped = DrawableCompat.wrap(homeItem.getIcon());
        DrawableCompat.setTint(wrapped, Color.rgb(2,128,127));
        homeItem.setIcon(wrapped);

        navigationView.setNavigationItemSelectedListener(this);

        //******     menu_item_event     *******

        //QL ban
        CardView qlban = findViewById(R.id.item_qlBan);
        qlban.setOnClickListener(view -> {
            Intent i = new Intent(HomeAdminActivity.this, QLBanActivity.class);
            startActivity(i);
        });

        //QL doanh thu
        CardView qlDoanhThu = findViewById(R.id.item_qldt);
        qlDoanhThu.setOnClickListener(view -> {
            Intent i = new Intent(HomeAdminActivity.this, QLDTActivity.class);
            startActivity(i);
        });

        //QL hoa don
        CardView tcHD = findViewById(R.id.item_traCuuHD);
        tcHD.setOnClickListener(view -> {
            Intent i = new Intent(HomeAdminActivity.this, TraCuuHDActivity.class);
            startActivity(i);
        });

        //QL mon an
        CardView qlmon = findViewById(R.id.item_qlmon);
        qlmon.setOnClickListener(view -> {
            Intent i = new Intent(this, QLMonActivity.class);
            startActivity(i);
        });

        //QL lich dat
        CardView qllich = findViewById(R.id.item_qllichdat);
        qllich.setOnClickListener(view -> {
            Intent i = new Intent(this, QLLichDatActivity.class);
            startActivity(i);
        });

        //QL khach hang
        CardView qlkh = findViewById(R.id.item_qlkh);
        qlkh.setOnClickListener(view -> {
            startActivity(new Intent(this, QLKHActivity.class));
        });

        //QL NV
        CardView qlnv = findViewById(R.id.item_qlnv);
        qlnv.setOnClickListener(view -> {
            startActivity(new Intent(this, QLNVActivity.class));
        });

        //QL voucher
        CardView qlvc = findViewById(R.id.item_qlvc);
        qlvc.setOnClickListener(view -> {
            startActivity(new Intent(this, QLVoucherActivity.class));
        });
    }
    private void setColorSelectedItem(MenuItem item, int rgb) {
        NavigationView navigationView = findViewById(R.id.nav_view);

        for (int i = 0; i < navigationView.getMenu().size()-1; i++){
            MenuItem tmp_item = navigationView.getMenu().getItem(i);
            Drawable wrapped = DrawableCompat.wrap(tmp_item.getIcon());
            if (tmp_item == item) {
                DrawableCompat.setTint(wrapped, rgb);
            } else {
                DrawableCompat.setTint(wrapped, Color.rgb(241, 187, 59));
            }
            tmp_item.setIcon(wrapped);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                setColorSelectedItem(item, Color.rgb(2,128,127));
                break;
            case R.id.nav_password:
                setColorSelectedItem(item, Color.rgb(2,128,127));
                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.change_password_dialog);
                Window window = dialog.getWindow();
                if (window == null) break;
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                CardView cancel = dialog.findViewById(R.id.cancel_button);
                CardView okay = dialog.findViewById(R.id.confirm_button);
                EditText newPass, confirmPass;
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                newPass = dialog.findViewById(R.id.dialog_new_pass);
                confirmPass = dialog.findViewById(R.id.dialog_confirm_pass);
                okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!confirmPass.getText().toString().trim().equals(newPass.getText().toString().trim())){
                            Toast.makeText(HomeAdminActivity.this, "Those passwords didn’t match. Try again.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String newPassword = confirmPass.getText().toString().trim();
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(HomeAdminActivity.this, "Successful update", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                return_home_item();
                                            }
                                        }
                                    });
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        return_home_item();
                    }
                });
                dialog.show();
                break;
            case R.id.nav_info:
                setColorSelectedItem(item, Color.rgb(2,128,127));

                break;
            case R.id.nav_report:
                setColorSelectedItem(item, Color.rgb(2,128,127));
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"dev@crescentmoon.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Báo lỗi");
                i.putExtra(Intent.EXTRA_TEXT   , "Ứng dụng bị lỗi: ");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(HomeAdminActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeAdminActivity.this, MainActivity.class));
                finish();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void return_home_item() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        MenuItem homeItem = navigationView.getMenu().findItem(R.id.nav_home);
        homeItem.setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        return_home_item();
    }
}

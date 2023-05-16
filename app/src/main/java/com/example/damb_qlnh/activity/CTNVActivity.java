package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.damb_qlnh.R;

import java.util.Arrays;

public class CTNVActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctnv);

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //edit NV
        ImageView editBtn = findViewById(R.id.btn_edit);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(CTNVActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.form_sua_nv);
                Window window = dialog.getWindow();
                if (window == null) return;
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Spinner spn = dialog.findViewById(R.id.gender);
                spn.setAdapter(new ArrayAdapter<>(CTNVActivity.this, R.layout.style_spinner_form, Arrays.asList("Nam", "Nữ", "Khác")));

                Spinner spn2 = dialog.findViewById(R.id.vitri);
                spn2.setAdapter(new ArrayAdapter<>(CTNVActivity.this, R.layout.style_spinner_form, Arrays.asList("Bảo vệ", "Thu ngân", "Phục vụ")));

                Spinner spn3 = dialog.findViewById(R.id.loai);
                spn3.setAdapter(new ArrayAdapter<>(CTNVActivity.this, R.layout.style_spinner_form, Arrays.asList("Part-time", "Full-time")));

                CardView cancel = dialog.findViewById(R.id.cancel_button);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
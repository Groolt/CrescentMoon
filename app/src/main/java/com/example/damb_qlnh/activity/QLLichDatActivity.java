package com.example.damb_qlnh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.adapter.KHAdapter;
import com.example.damb_qlnh.models.datBan;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class QLLichDatActivity extends AppCompatActivity {
    FirebaseFirestore db;
    SimpleDateFormat formatDay = new SimpleDateFormat("dd/MM/yyyy");
    final Calendar myCalendar = Calendar.getInstance();
    private ViewGroup viewDSLich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qllich_dat);
        db = FirebaseFirestore.getInstance();
        viewDSLich = findViewById(R.id.lich);

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //ds lich dat
        getListLD();

        //chọn ngày
        TextView date = findViewById(R.id.date);
        DatePickerDialog.OnDateSetListener datePicker = (view1, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            if (formatDay.format(myCalendar.getTime()).contains(formatDay.format(Calendar.getInstance().getTime())))
                date.setText("Hôm nay");
            else
                date.setText(formatDay.format(myCalendar.getTime()));
            viewDSLich.removeAllViews();
            getListLD();
        };
        date.setOnClickListener(view -> new DatePickerDialog(QLLichDatActivity.this, datePicker, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void getListLD() {
        db.collection("datBan")
                .whereEqualTo("date", formatDay.format(myCalendar.getTime()))
                .get()
                .addOnCompleteListener(task -> {
                    int cnt = 0;
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            cnt++;
                            datBan db = document.toObject(datBan.class);
                            db.setId(document.getId());
                            addToView(db, viewDSLich);
                        }
                        TextView sl = findViewById(R.id.soluong);
                        sl.setText("" + cnt);
                    } else {
                        Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private void addToView(datBan lich, ViewGroup viewGroup) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_lichdat, viewGroup, false);
        TextView giodat = view.findViewById(R.id.giodat);
        TextView ban = view.findViewById(R.id.ban);
        TextView ten = view.findViewById(R.id.ten);
        CardView xoa = view.findViewById(R.id.xoa);
        ImageView avatar = view.findViewById(R.id.avatar);
        Glide.with(this).load(lich.getAvatar()).into(avatar);
        giodat.setText(lich.getTime());
        ban.setText("Bàn " + lich.getMaBan());
        ten.setText(lich.getName());

        ten.setOnClickListener(view1 -> {
            Intent i = new Intent(QLLichDatActivity.this, CTKHActivity.class);
            i.putExtra("idKH", lich.getMaKH());
            QLLichDatActivity.this.startActivity(i);
        });
        avatar.setOnClickListener(view1 -> {
            Intent i = new Intent(QLLichDatActivity.this, CTKHActivity.class);
            i.putExtra("idKH", lich.getMaKH());
            QLLichDatActivity.this.startActivity(i);
        });

        xoa.setOnClickListener(view12 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(QLLichDatActivity.this);
            builder.setTitle("Hủy lịch đặt");
            builder.setMessage("Xác nhận hủy lịch đặt bàn");
            builder.setPositiveButton("Xác nhận", (dialog, which) -> {
               db.collection("datBan")
                       .document(lich.getId()).delete()
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               viewDSLich.removeAllViews();
                               getListLD();
                               Toast.makeText(QLLichDatActivity.this, "Hủy lịch thành công", Toast.LENGTH_SHORT).show();
                           }
                       })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(QLLichDatActivity.this, "Hủy không thành công", Toast.LENGTH_SHORT).show();
                           }
                       });
            });
            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        viewGroup.addView(view);
    }
}
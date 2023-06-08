package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.damb_qlnh.adapter.VoucherAdapter;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.vouCher;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class QLVoucherActivity extends AppCompatActivity {
    FirebaseFirestore db;
    final Calendar myCalendar = Calendar.getInstance();
    ArrayList<vouCher> ListVoucherDDR;
    ArrayList<vouCher> ListVoucherSDR;
    VoucherAdapter adapter;
    SimpleDateFormat formatDay = new SimpleDateFormat("dd/MM/yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlvoucher);
        db = FirebaseFirestore.getInstance();

        //switch tab
        CardView tabDDR = findViewById(R.id.materialCardView);
        CardView tabSDR = findViewById(R.id.cardView3);
        tabDDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabDDR.getBackgroundTintList() ==
                        ColorStateList.valueOf(ContextCompat.getColor(QLVoucherActivity.this, R.color.white)))
                {
                    tabDDR.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#fad745")));
                    tabSDR.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                    renderList();
                    adapter.setList(ListVoucherDDR);
                }
            }
        });

        tabSDR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabSDR.getBackgroundTintList() ==
                        ColorStateList.valueOf(ContextCompat.getColor(QLVoucherActivity.this, R.color.white)))
                {
                    tabSDR.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#fad745")));
                    tabDDR.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                    renderList();
                    adapter.setList(ListVoucherSDR);
                }
            }
        });


        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //list voucher
        ListVoucherDDR = new ArrayList<>();
        ListVoucherSDR = new ArrayList<>();
        adapter = new VoucherAdapter(this, R.layout.item_voucher, ListVoucherDDR);
        ListView lv = findViewById(R.id.lv);
        lv.setAdapter(adapter);
        renderList();

        //them voucher
        ImageView addbtn = findViewById(R.id.addbtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(QLVoucherActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.form_them_voucher);
                Window window = dialog.getWindow();
                if (window == null) return;
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                EditText batdau = dialog.findViewById(R.id.batdau);
                EditText ketthuc = dialog.findViewById(R.id.ketthuc);
                DatePickerDialog.OnDateSetListener dateBD = (view1, year, month, day) -> {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH,month);
                    myCalendar.set(Calendar.DAY_OF_MONTH,day);
                    batdau.setText(formatDay.format(myCalendar.getTime()));
                };
                DatePickerDialog.OnDateSetListener dateKT = (view1, year, month, day) -> {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH,month);
                    myCalendar.set(Calendar.DAY_OF_MONTH,day);
                    ketthuc.setText(formatDay.format(myCalendar.getTime()));
                };
                batdau.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(QLVoucherActivity.this, dateBD, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                ketthuc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(QLVoucherActivity.this, dateKT, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                CardView cancel = dialog.findViewById(R.id.cancel_button);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                EditText discount = dialog.findViewById(R.id.discount);
                EditText sl = dialog.findViewById(R.id.sl);
                CardView confirm = dialog.findViewById(R.id.add_button);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Integer.valueOf(discount.getText().toString().trim()) <= 0
                                || Integer.valueOf(discount.getText().toString().trim()) > 100)
                        {
                            Toast.makeText(QLVoucherActivity.this, "Discount phải <= 100 và > 0", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            if (formatDay.parse(batdau.getText().toString())
                                    .after(formatDay.parse(ketthuc.getText().toString())))
                            {
                                Toast.makeText(QLVoucherActivity.this, "Ngày bắt đầu phải < ngày kết thúc", Toast.LENGTH_SHORT).show();
                            }

                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        Map<String, Object> data = new HashMap<>();
                        data.put("giaTri",Integer.valueOf(discount.getText().toString().trim()) );
                        data.put("soLuong", Integer.valueOf(sl.getText().toString().trim()) );
                        data.put("ngayBD", batdau.getText().toString());
                        data.put("ngayKT", ketthuc.getText().toString());
                        data.put("is_deleted", false);
                        db.collection("Voucher").add(data);
                        dialog.dismiss();
                        renderList();
                    }
                });

                dialog.show();
            }
        });
    }

    private void renderList() {
        ListVoucherSDR.clear();
        ListVoucherDDR.clear();
        db.collection("Voucher")
                .whereEqualTo("is_deleted", false)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                if (formatDay.parse(document.getString("ngayKT")).after(Calendar.getInstance().getTime())){
                                    vouCher vouCher = new vouCher(
                                            document.getId(),
                                            document.getString("ngayBD"),
                                            document.getString("ngayKT"),
                                            document.getDouble("soLuong").intValue(),
                                            document.getDouble("giaTri").intValue()
                                    );
                                    if (formatDay.parse(document.getString("ngayBD")).before(Calendar.getInstance().getTime()))
                                        ListVoucherDDR.add(vouCher);
                                    else
                                        ListVoucherSDR.add(vouCher);
                                }
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }

                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
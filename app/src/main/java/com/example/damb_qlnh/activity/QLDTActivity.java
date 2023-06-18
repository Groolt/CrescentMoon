package com.example.damb_qlnh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.adapter.monAdapterQLmon;
import com.example.damb_qlnh.models.monAn;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QLDTActivity extends AppCompatActivity {
    private static final int MODE_DEFAULT = 1;
    private static final int CHART_DATA_COUNT = 7;
    private int mode = MODE_DEFAULT;
    private int spinnerSelector = 0;
    private BarChart barChart;
    private BarData barData;
    private Handler handler;
    private ViewGroup mLinearLayoutBanChay;
    private ViewGroup mLinearLayoutBanIt;
    private final Calendar myCalendar = Calendar.getInstance();
    private final SimpleDateFormat formatDay = new SimpleDateFormat("dd/MM/yyyy");
    private FirebaseFirestore db;
    private TextView tongDoanhThu;
    private TextView soDon;
    private TextView DTTB;
    private TextView rate;
    Map<monAn, Integer> listMA = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qldtactivity);

        db = FirebaseFirestore.getInstance();
        handler = new Handler(Looper.getMainLooper());

        initializeViews();
        setupActionBar();
        setupChart();
        setupDatePicker();
        setupDDTB();

        getDataFromFirebase();
        updateChartData();
        loadBCBI();
    }

    private void loadBCBI() {
        Thread thread = new Thread(() -> {
            db.collection("monAn")
                    .whereEqualTo("is_deleted", false)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listMA.put(new monAn(
                                        document.getId(),
                                        document.getString("ten"),
                                        document.getString("loai"),
                                        String.valueOf(document.getDouble("gia").intValue()),
                                        document.getString("img")), 0
                                );
                            }
                            getSLMon();
                        } else {
                            Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        thread.start();
    }

    private void getSLMon() {
        db.collection("CTHD")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            monAn ma = new monAn(
                                    document.getString("monAn.maMA"),
                                    document.getString("monAn.tenMA"),
                                    document.getString("monAn.loaiMA"),
                                    document.getString("monAn.giaTien"),
                                    document.getString("monAn.anhMA"));
                            listMA.put(ma, listMA.getOrDefault(ma, 0) + document.getDouble("soLuong").intValue());
                        }
                        Iterator<Map.Entry<monAn, Integer>> iterator = listMA.entrySet().iterator();
                        int maxValue = Integer.MIN_VALUE;
                        int minValue = Integer.MAX_VALUE;
                        for (Integer value : listMA.values()) {
                            if (value > maxValue) {
                                maxValue = value;
                            }
                            if (value < minValue) {
                                minValue = value;
                            }
                        }
                        Log.e("haha", maxValue + " " + minValue);
                        while (iterator.hasNext()) {
                            Map.Entry<monAn, Integer> entry = iterator.next();
                            monAn key = entry.getKey();
                            Integer value = entry.getValue();
                            if (value*1.0 >= maxValue*0.5)
                                addToView(key, mLinearLayoutBanChay, value);
                            else if (value <= minValue + 1)
                                addToView(key, mLinearLayoutBanIt, value);
                        }

                    } else {
                        Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void initializeViews() {
        tongDoanhThu = findViewById(R.id.doanhthu);
        soDon = findViewById(R.id.sodon);
        barChart = findViewById(R.id.idBarChart);
        mLinearLayoutBanChay = findViewById(R.id.linear_banchay);
        mLinearLayoutBanIt = findViewById(R.id.linear_banit);
        DTTB = findViewById(R.id.dthutb);
        rate = findViewById(R.id.rate);
    }

    private void setupActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);
    }

    private void setupChart() {
        barChart.getDescription().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setSpaceMax(0.8f);
        barChart.getXAxis().setSpaceMin(0.8f);
    }

    private void setupDatePicker() {
        TextView dateTextView = findViewById(R.id.tv1);
        DatePickerDialog.OnDateSetListener datePickerListener = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            if (formatDay.format(myCalendar.getTime()).contains(formatDay.format(Calendar.getInstance().getTime())))
                dateTextView.setText("Hôm nay");
            else
                dateTextView.setText(formatDay.format(myCalendar.getTime()));
            getDataFromFirebase();
        };

        dateTextView.setOnClickListener(view -> new DatePickerDialog(
                QLDTActivity.this,
                datePickerListener,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show());
    }

    private void setupDDTB() {
        Spinner spinner = findViewById(R.id.spn);
        List<String> spinnerItems = Arrays.asList("7 ngày", "30 ngày", "1 quý", "1 năm");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.style_spinner, spinnerItems);
        spinner.setAdapter(spinnerAdapter);
        Thread thread = new Thread(() -> {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                int sn = 7;
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i) {
                        case 0:
                            sn = 7; break;
                        case 1:
                            sn = 30; break;
                        case 2:
                            sn = 90; break;
                        case 3:
                            sn = 365; break;
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());

                    calendar.add(Calendar.DAY_OF_YEAR, -sn + 1);
                    Date startDate = calendar.getTime();
                    Date endDate = new Date();

                    //Log.e("haha", formatDay.format(startDate) + ", " + formatDay.format(endDate));
                    db.collection("HoaDon")
                            .whereEqualTo("tinhTrang", 1)
                            .get()
                            .addOnCompleteListener(task -> {
                                double tongDT = 0;
                                double tongHN = 0;
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String thoiGian = document.getString("thoiGian");
                                        double tongTien_S = document.getDouble("tongTien_S");
                                        if (thoiGian != null) {
                                            try {
                                                Date thoiGianDate = formatDay.parse(thoiGian);
                                                if (thoiGianDate != null && thoiGianDate.after(startDate) && thoiGianDate.before(endDate)) {
                                                    tongDT += tongTien_S;
                                                }
                                                if (thoiGianDate != null && thoiGian.equals(formatDay.format(Calendar.getInstance().getTime()))) {
                                                    tongHN += tongTien_S;
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    double dttb = tongDT / sn / 1000000;
                                    DTTB.setText("Doanh thu TB: " + new DecimalFormat("#.##").format(dttb) + " tr");
                                    if (tongHN < dttb) {
                                        rate.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.round_arrow_drop_down_24), null);
                                        rate.setTextColor(Color.rgb(236, 61, 61));
                                        rate.setText(new DecimalFormat("#.##").format(Math.abs(tongHN - (tongDT / sn))/(tongDT / sn)*100) + "%");
                                    } else {
                                        rate.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.round_arrow_drop_up_24), null);
                                        rate.setTextColor(Color.rgb(20, 181, 93));
                                        rate.setText(new DecimalFormat("#.##").format(Math.abs(tongHN - (tongDT / sn))/(tongDT / sn)*100) + "%");
                                    }
                                } else {
                                    Toast.makeText(QLDTActivity.this, "Can't get data", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        });

        thread.start();
    }

    private void getDataFromFirebase() {
        db.collection("HoaDon")
                .whereEqualTo("thoiGian", formatDay.format(myCalendar.getTime()))
                .whereEqualTo("tinhTrang", 1)
                .get()
                .addOnCompleteListener(task -> {
                    double tongDT = 0;
                    int sl = 0;
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            tongDT += document.getDouble("tongTien_S");
                            sl++;
                        }
                        tongDoanhThu.setText(new DecimalFormat("#.##").format(tongDT / 1000000) + " tr");
                        soDon.setText(String.valueOf(sl));
                    } else {
                        Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateChartData() {
        Thread thread = new Thread(() -> {
            if (mode == MODE_DEFAULT) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_YEAR, -CHART_DATA_COUNT + 1);
                Date startDate = calendar.getTime();
                Date endDate = new Date();

                Map<String, Double> sumByDate = new HashMap<>();
                Calendar tempCalendar = Calendar.getInstance();
                tempCalendar.setTime(startDate);
                for (int i = 0; i < CHART_DATA_COUNT; i++) {
                    String dateKey = formatDay.format(tempCalendar.getTime());
                    sumByDate.put(dateKey, 0.0);
                    tempCalendar.add(Calendar.DAY_OF_YEAR, 1);
                }

                db.collection("HoaDon")
                        .whereEqualTo("tinhTrang", 1)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                if (document.contains("thoiGian") && document.contains("tongTien_S")) {
                                    String thoiGian = document.getString("thoiGian");
                                    double tongTien_S = document.getDouble("tongTien_S");

                                    if (thoiGian != null) {
                                        try {
                                            Date thoiGianDate = formatDay.parse(thoiGian);
                                            if (thoiGianDate != null && thoiGianDate.after(startDate) && thoiGianDate.before(endDate)) {
                                                String formattedDate = formatDay.format(thoiGianDate);
                                                if (sumByDate.containsKey(formattedDate)) {
                                                    double sum = sumByDate.get(formattedDate);
                                                    sum += tongTien_S;
                                                    sumByDate.put(formattedDate, sum);
                                                } else {
                                                    sumByDate.put(formattedDate, tongTien_S);
                                                }
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            ArrayList<String> datesList = new ArrayList<>();
                            ArrayList<Double> sumsList = new ArrayList<>();
                            for (Map.Entry<String, Double> entry : sumByDate.entrySet()) {
                                String date = entry.getKey();
                                double sum = entry.getValue();

                                datesList.add(date.substring(0, 5));
                                sumsList.add(sum);
                            }

                            List<String> sortedDatesList = datesList.stream()
                                    .sorted(dateComparator)
                                    .collect(Collectors.toList());

                            List<Double> sortedSumsList = new ArrayList<>();
                            for (String sortedDate : sortedDatesList) {
                                int index = datesList.indexOf(sortedDate);
                                sortedSumsList.add(sumsList.get(index));
                            }

                            ArrayList<BarEntry> barEntries = new ArrayList<>();
                            for (int i = 0; i < CHART_DATA_COUNT; i++) {
                                barEntries.add(new BarEntry(i, (float)(sortedSumsList.get(i)/1000000)));
                            }

                            BarDataSet barDataSet = new BarDataSet(barEntries, "Đơn vị: triệu vnđ");
                            barDataSet.setColors(Color.rgb(249, 155, 70));
                            barDataSet.setValueTextSize(10f);
                            barData = new BarData(barDataSet);
                            barChart.setData(barData);

                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(sortedDatesList));

                            handler.post(() -> barChart.invalidate());
                        })
                        .addOnFailureListener(e -> {

                        });
            }
        });

        thread.start();
    }

    private void addToView(monAn mon, ViewGroup viewGroup, int soluong) {
        View view = LayoutInflater.from(this).inflate(R.layout.mon_ko_gia, viewGroup, false);
        TextView tv = view.findViewById(R.id.tenmon);
        TextView sl = view.findViewById(R.id.editable);
        ImageView img = view.findViewById(R.id.anhmon);
        Glide.with(this).load(mon.getAnhMA()).into(img);
        tv.setText(mon.getTenMA());
        sl.setText("SL: " + soluong);
        viewGroup.addView(view);
    }

    Comparator<String> dateComparator = new Comparator<String>() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");

        @Override
        public int compare(String date1, String date2) {
            try {
                Date d1 = dateFormat.parse(date1);
                Date d2 = dateFormat.parse(date2);
                return d1.compareTo(d2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
package com.example.damb_qlnh.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damb_qlnh.activity.CTBanActivity;
import com.example.damb_qlnh.adapter.BanAdapter;
import com.example.damb_qlnh.adapter.PhongAdapter;
import com.example.damb_qlnh.models.Phong;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.banAn;
import com.example.damb_qlnh.models.nhanVien;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_QLban_TatCa#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_QLban_TatCa extends Fragment {
    FirebaseFirestore db;
    ArrayList<Phong> listPhong;
    ArrayList<banAn> listBan;
    GridView gridView;
    BanAdapter banAdapter;
    PhongAdapter adapter;
    Spinner spinner;
    int curTang;
    int curPhongIndex;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mView;

    public Fragment_QLban_TatCa() {
        // Required empty public constructor
    }

    public static Fragment_QLban_TatCa newInstance(String param1, String param2) {
        Fragment_QLban_TatCa fragment = new Fragment_QLban_TatCa();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_qlban_tat_ca, container, false);
        initUI();
        return mView;
    }
    private void initUI(){
        curTang = 1;
        db = FirebaseFirestore.getInstance();

        //setup spinner phong
        listPhong = new ArrayList<>();
        spinner = mView.findViewById(R.id.spinner_phong);
        getPhong();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                curPhongIndex = i;
                getListBan();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setSelection(0);

        //setup list ban
        gridView = mView.findViewById(R.id.gv_ban);
        listBan = new ArrayList<>();

        // doi tang
        CardView tang1 = mView.findViewById(R.id.tang1);
        CardView tang2 = mView.findViewById(R.id.tang2);

        tang1.setOnClickListener(view -> {
            if (curTang == 1) return;
            tang1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#fad745")));
            tang2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
            curTang = 1;
            getPhong();
            spinner.setSelection(curPhongIndex);
        });
        tang2.setOnClickListener(view -> {
            if (curTang == 2) return;
            tang2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#fad745")));
            tang1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
            curTang = 2;
            getPhong();
            spinner.setSelection(curPhongIndex);
        });

        //cap nhat tinh trang ban
        db.collection("banAn")
                .addSnapshotListener((snapshots, error) -> {
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.MODIFIED) {
                            getListBan();
                        }
                    }
                });
        db.collection("phong")
                .addSnapshotListener((snapshots, error) -> {
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.MODIFIED) {
                            getPhong();
                        }
                    }
                });
    }
    private void getPhong(){
        ArrayList<Phong> tmp = new ArrayList<>();
        db.collection("phong")
                .whereEqualTo("tang", curTang)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            tmp.add(new Phong(document.getId(), document.getDouble("tinhTrang").intValue()));
                        }
                        Phong p = tmp.get(1);
                        tmp.remove(1);
                        tmp.add(p);
                        listPhong = tmp;
                        adapter = new PhongAdapter(this.getActivity(), R.layout.phong_selected_spinner, listPhong);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(curPhongIndex);
                    } else {
                        Toast.makeText(getActivity(), "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void getListBan(){
        ArrayList<banAn> tmp = new ArrayList<>();
        db.collection("banAn")
                .whereEqualTo("tang", curTang)
                .whereEqualTo("phong", listPhong.get(spinner.getSelectedItemPosition()).getTenPhong())
                .orderBy("maBan")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            tmp.add(new banAn(
                                    document.getString("maBan"),
                                    document.getDouble("tinhTrang").intValue(),
                                    document.getDouble("loaiBan").intValue(),
                                    document.getString("phong"),
                                    document.getDouble("tang").intValue(),
                                    document.getId())
                            );

                            if (document.getDouble("tinhTrang").intValue() != 0) {
                            }
                        }
                        listBan = tmp;
                        banAdapter = new BanAdapter(this.getActivity(), R.layout.ban, listBan);
                        gridView.setAdapter(banAdapter);
                    } else {
                        Toast.makeText(getActivity(), "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
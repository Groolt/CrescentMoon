package com.example.damb_qlnh.Fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.adapter.BanAdapter;
import com.example.damb_qlnh.adapter.PhongAdapter;
import com.example.damb_qlnh.models.Phong;
import com.example.damb_qlnh.models.banAn;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_QLban_ConTrong#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_QLban_ConTrong extends Fragment {
    FirebaseFirestore db;
    ArrayList<Phong> listPhong;
    ArrayList<banAn> listBan;
    GridView gridView;
    BanAdapter banAdapter;
    PhongAdapter adapter;
    Spinner spinner;
    int curTang;
    int curPhongIndex;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mView;

    public Fragment_QLban_ConTrong() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_QLban_DangDung.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_QLban_ConTrong newInstance(String param1, String param2) {
        Fragment_QLban_ConTrong fragment = new Fragment_QLban_ConTrong();
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
        mView =  inflater.inflate(R.layout.fragment_qlban_controng, container, false);
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
            spinner.setSelection(0);
        });
        tang2.setOnClickListener(view -> {
            if (curTang == 2) return;
            tang2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#fad745")));
            tang1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
            curTang = 2;
            getPhong();
            spinner.setSelection(0);
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
                        int t = -1;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            tmp.add(new Phong(document.getId(), document.getDouble("tinhTrang").intValue()));
                            if (document.getId().contains("10")) t = tmp.size()-1;
                        }
                        if (t != -1){
                            Phong p = tmp.get(t);
                            tmp.remove(t);
                            tmp.add(p);
                        }
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
                .whereEqualTo("tinhTrang", 0)
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
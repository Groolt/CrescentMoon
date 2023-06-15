package com.example.damb_qlnh.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.damb_qlnh.activity.CTBanActivity;
import com.example.damb_qlnh.adapter.BanAdapter;
import com.example.damb_qlnh.adapter.PhongAdapter;
import com.example.damb_qlnh.models.Phong;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.banAn;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_QLban_TatCa#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_QLban_TatCa extends Fragment {

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
        //setup spinner phong
        Spinner spinner = mView.findViewById(R.id.spinner_phong);
        PhongAdapter adapter = new PhongAdapter(this.getActivity(), R.layout.phong_selected_spinner, getListPhong());
        spinner.setAdapter(adapter);

        //setup list ban
        GridView gridView = mView.findViewById(R.id.gv_ban);
        BanAdapter banAdapter = new BanAdapter(this.getActivity(), R.layout.ban, getListBan());
        gridView.setAdapter(banAdapter);

        //test start activity chi tiet ban
        TextView txt = mView.findViewById(R.id.tang1);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CTBanActivity.class);
                startActivity(i);
            }
        });
    }

    private ArrayList<banAn> getListBan(){
        ArrayList<banAn> bans = new ArrayList<>();
//        bans.add(new banAn("113.1", 0, 2));
//        bans.add(new banAn("113.2", 1,2));
//        bans.add(new banAn("113.3", 2,4));
//        bans.add(new banAn("113.4", 0,4));
//        bans.add(new banAn("113.5", 1,6));
//        bans.add(new banAn("113.6", 2,6));
//        bans.add(new banAn("113.7", 0,8));
//        bans.add(new banAn("113.8", 1,8));
        return bans;
    }
    private ArrayList<Phong> getListPhong() {
        ArrayList<Phong> phongs = new ArrayList<>();
        phongs.add(new Phong("A1.1", 1));
        phongs.add(new Phong("A1.2", 0));
        phongs.add(new Phong("A1.3", 0));
        phongs.add(new Phong("A1.4", 1));
        phongs.add(new Phong("A1.5", 0));
        return phongs;
    }
}
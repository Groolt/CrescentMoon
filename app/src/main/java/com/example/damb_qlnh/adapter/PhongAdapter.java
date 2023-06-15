package com.example.damb_qlnh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.damb_qlnh.models.Phong;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.Phong;

import java.util.ArrayList;

public class PhongAdapter extends ArrayAdapter<Phong> {

    public PhongAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Phong> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.phong_selected_spinner, parent, false);
        TextView tenphong = convertView.findViewById(R.id.tenphong);

        Phong phong = this.getItem(position);
        if (phong != null) {
            tenphong.setText(phong.getTenPhong());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.phong_spinner, parent, false);
        TextView tenphong = convertView.findViewById(R.id.phong);
        ImageView red = convertView.findViewById(R.id.red);

        Phong phong = this.getItem(position);
        if (phong != null) {
            tenphong.setText(phong.getTenPhong());
            if (phong.getTinhTrang() == 0) {
                red.setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }
}

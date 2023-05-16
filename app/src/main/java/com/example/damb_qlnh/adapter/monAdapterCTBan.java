package com.example.damb_qlnh.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.monAn;

import java.util.ArrayList;
import java.util.List;

public class monAdapterCTBan extends BaseAdapter {
    private Activity context;
    private int layout;
    private List<CTHD> cthds;

    public monAdapterCTBan(@NonNull Context context, int resource, @NonNull ArrayList<CTHD> objects) {
        this.context = (Activity) context;
        this.layout = resource;
        this.cthds = objects;
    }

    @Override
    public int getCount() {
        return cthds.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(layout, null);

        CTHD cthd = cthds.get(position);

        TextView tenMon = convertView.findViewById(R.id.tenmon);
        TextView sl = convertView.findViewById(R.id.editable);
        ImageView anh = convertView.findViewById(R.id.anhmon);
        TextView gia = convertView.findViewById(R.id.gia);
        ImageView close = convertView.findViewById(R.id.huymon);


        if (cthd != null){
            tenMon.setText(cthd.getMonAn().getTenMA());
            tenMon.setPadding(dptopx(16), 0, 0, 0);
            sl.setText("SL: " + Integer.toString(cthd.getSoLuong()));
            anh.setBackgroundResource(R.drawable.mon);
            gia.setText(String.valueOf(cthd.getMonAn().getGiaTien())+"$");
            close.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }
    int dptopx(int dp){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}

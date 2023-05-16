package com.example.damb_qlnh.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.monAn;

import java.util.ArrayList;
import java.util.List;

public class monAdapterQLmon extends BaseAdapter {
    private Activity context;
    private int layout;
    private ArrayList<monAn> dsMon;

    public monAdapterQLmon(@NonNull Context context, int resource, @NonNull ArrayList<monAn> objects) {
        this.context = (Activity) context;
        this.layout = resource;
        this.dsMon = objects;
    }

    public void setFilteredList(ArrayList<monAn> filteredList) {
        this.dsMon = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dsMon.size();
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

        monAn mon = dsMon.get(position);

        TextView tenMon = convertView.findViewById(R.id.tenmon);
        TextView loai = convertView.findViewById(R.id.editable);
        ImageView anh = convertView.findViewById(R.id.anhmon);
        TextView gia = convertView.findViewById(R.id.gia);

        if (mon != null){
            tenMon.setText(mon.getTenMA());
            loai.setText("Loại: " + mon.getLoaiMA());
            anh.setBackgroundResource(R.drawable.mon);
            gia.setText(String.valueOf(mon.getGiaTien())+"$");
        }
        return convertView;
    }
    private int convertDpToPx(int dp, DisplayMetrics displayMetrics) {
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
        return Math.round(pixels);
    }
}

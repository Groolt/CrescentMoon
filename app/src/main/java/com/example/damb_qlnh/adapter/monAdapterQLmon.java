package com.example.damb_qlnh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.bumptech.glide.Glide;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.monAn;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

            Glide.with(context).load(mon.getAnhMA()).into(anh);
            String input = mon.getGiaTien();
            StringBuilder result = new StringBuilder();
            int length = input.length();
            int dotCount = length / 3;

            // Append the remaining digits if the length is not a multiple of three
            int remainingDigits = length % 3;
            if (remainingDigits != 0) {
                result.append(input, 0, remainingDigits);
                if (dotCount > 0) {
                    result.append(".");
                }
            }

            // Append the groups of three digits with dots
            for (int i = 0; i < dotCount; i++) {
                int startIndex = remainingDigits + i * 3;
                result.append(input.substring(startIndex, startIndex + 3));
                if (i < dotCount - 1) {
                    result.append(".");
                }
            }
            gia.setText(result.toString()+ " đ");
        }
        return convertView;
    }

}

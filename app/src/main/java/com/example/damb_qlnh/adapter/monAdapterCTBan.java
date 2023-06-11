package com.example.damb_qlnh.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
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


        if (cthd != null){
            tenMon.setText(cthd.getMonAn().getTenMA());
            sl.setText("SL: " + Integer.toString(cthd.getSoLuong()));
            Glide.with(context).load(cthd.getMonAn().getAnhMA()).into(anh);
            String numString = String.valueOf(Integer.parseInt(cthd.getMonAn().getGiaTien()) * cthd.getSoLuong());
            String str = "";
            for (int i = 0; i < numString.length() ; i++){
                if((numString.length() - i - 1) % 3 == 0 && i < numString.length()-1){
                    str += Character.toString(numString.charAt(i)) + ".";
                }else{
                    str += Character.toString(numString.charAt(i));
                }
            }
            gia.setText(str + " vnđ");
        }
        return convertView;
    }
    int dptopx(int dp){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}

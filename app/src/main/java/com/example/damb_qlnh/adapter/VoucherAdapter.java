package com.example.damb_qlnh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.vouCher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class VoucherAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<vouCher> list;
    private int layout;

    public VoucherAdapter(Context context, int layout, ArrayList<vouCher> list) {
        this.context = context;
        this.list = list;
        this.layout = layout;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private class ViewHolder {
        TextView discount;
        TextView mota;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        vouCher vc = list.get(i);
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.discount = view.findViewById(R.id.discount);
            viewHolder.mota = view.findViewById(R.id.mota);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.discount.setText("Discount " + vc.getGiaTri() + "%");
        String strBD = vc.getNgayBD();
        String strKT = vc.getNgayKT();
        String strMota = "Thời gian: " + strBD + "-" + strKT + "\nSố lượng còn lại: " + vc.getSoLuong();
        viewHolder.mota.setText(strMota);
        return view;
    }
}

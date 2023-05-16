package com.example.damb_qlnh.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.damb_qlnh.activity.CTKHActivity;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.khachHang;

import java.util.List;

public class KHAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<khachHang> list;

    public KHAdapter(Context context, int layout, List<khachHang> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
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
        TextView tenKH;
        CardView vungchon;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.tenKH = view.findViewById(R.id.ten);
            viewHolder.vungchon = view.findViewById(R.id.vungchon);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.vungchon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, CTKHActivity.class));
            }
        });
        viewHolder.tenKH.setText(list.get(i).getTenKH());
        return view;
    }
}

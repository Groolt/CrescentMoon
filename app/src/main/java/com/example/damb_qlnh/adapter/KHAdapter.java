package com.example.damb_qlnh.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
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
        ImageView avatar;
        CardView vungchon;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        khachHang kh = list.get(i);
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.tenKH = view.findViewById(R.id.ten);
            viewHolder.vungchon = view.findViewById(R.id.vungchon);
            viewHolder.avatar = view.findViewById(R.id.avatar);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.vungchon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CTKHActivity.class);
                i.putExtra("idKH", kh.getId());
                context.startActivity(i);
            }
        });
        viewHolder.tenKH.setText(kh.getTenKH());
        Glide.with(context).load(kh.getImg()).into(viewHolder.avatar);
        return view;
    }
}

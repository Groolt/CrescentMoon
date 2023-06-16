package com.example.damb_qlnh.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.damb_qlnh.activity.CTBanActivity;
import com.example.damb_qlnh.models.banAn;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.banAn;

import java.util.ArrayList;
import java.util.List;

public class BanAdapter extends BaseAdapter {

    Context context;
    int layout;
    ArrayList<banAn> dsBan;
    public BanAdapter(Context context, int layout, ArrayList<banAn> dsBan) {
        this.context = context;
        this.layout = layout;
        this.dsBan = dsBan;
    }

    @Override
    public int getCount() {
        return dsBan.size();
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
        TextView tenban;
        ImageView red;
        CardView cv;
        TextView loaiban;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        banAn ban = dsBan.get(position);
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.tenban = convertView.findViewById(R.id.tenban);
            viewHolder.red = convertView.findViewById(R.id.redimg);
            viewHolder.cv = convertView.findViewById(R.id.cv_ban);
            viewHolder.loaiban = convertView.findViewById(R.id.loaiban);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tenban.setText(Html.fromHtml("Bàn " + "<b>" + ban.getTenBan() + "</b> "));
        viewHolder.loaiban.setText("Loại bàn: " + ban.getLoaiBan() + " người");
        viewHolder.red.setVisibility(View.INVISIBLE);
        if (dsBan.get(position).getTinhTrang() != 0) {
            viewHolder.cv.setCardBackgroundColor(Color.rgb(255,169,109));
            //viewHolder.cv.setStrokeColor(Color.rgb(210,145,100));
            if (dsBan.get(position).getTinhTrang() == 2) {
                viewHolder.red.setVisibility(View.VISIBLE);
            }
            viewHolder.cv.setOnClickListener(view -> {
                Intent i = new Intent(context, CTBanActivity.class);
                i.putExtra("maBan", ban.getMaBan());
                i.putExtra("idBan", ban.getId());
                i.putExtra("phong", ban.getPhong());
                context.startActivity(i);
            });
        } else {
            viewHolder.cv.setCardBackgroundColor(Color.rgb(180,199,231));
        }
        return convertView;
    }
}

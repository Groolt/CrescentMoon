package com.example.damb_qlnh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import androidx.cardview.widget.CardView;

import com.example.damb_qlnh.activity.CTHDActivity;
import com.example.damb_qlnh.activity.TraCuuHDActivity;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.hoaDon;
import com.example.damb_qlnh.models.monAn;

import java.text.SimpleDateFormat;
import java.util.List;

public class BillAdapter extends BaseAdapter {

    private Activity context;
    private int layout;
    private List<hoaDon> dsBill;

    public void setFilteredList(List<hoaDon> filteredList){
        this.dsBill = filteredList;
        notifyDataSetChanged();
    }
    public BillAdapter(Activity context, int layout, List<hoaDon> dsBill) {
        this.context = context;
        this.layout = layout;
        this.dsBill = dsBill;
    }

    @Override
    public int getCount() {
        return dsBill.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout, null);

        hoaDon bill = dsBill.get(position);

        TextView ct = convertView.findViewById(R.id.CTHD);
        TextView total = convertView.findViewById(R.id.gtHD);
        CardView detail = convertView.findViewById(R.id.detail);

        if (bill != null){
            String strDate = bill.getThoiGian();
            ct.setText("ID: " + bill.getMaHD() + "\n" + strDate);

            //total.setText(Html.fromHtml("Total:  <b>" +  String.valueOf(bill.getTongTien_S()) + "vnđ</b>"));
            String numString = String.valueOf(bill.getTongTien_S());
            String str = "";
            for (int i = 0; i < numString.length() ; i++){
                if((numString.length() - i - 1) % 3 == 0 && i < numString.length()-1){
                    str += Character.toString(numString.charAt(i)) + ".";
                }else{
                    str += Character.toString(numString.charAt(i));
                }
            }
            total.setText( str+ " vnđ");
            detail.setOnClickListener(view -> {
                Intent i = new Intent(context, CTHDActivity.class);
                i.putExtra("maHD", bill.getMaHD());
                i.putExtra("maKH", bill.getMaKH());
                i.putExtra("ngayTT", bill.getThoiGian());
                context.startActivity(i);
            });
        }

        return  convertView;
    }
}

package com.example.damb_qlnh.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.monAn;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CateAdapter extends RecyclerView.Adapter<CateAdapter.CateViewHolder>{
    private Context context;
    private final String MY_ACTION = "com.groolt.ACTION";
    private final String MY_CTHD = "com.groolt.CTHD";
    private ArrayList<monAn> monAns;
    private ArrayList<CTHD> cthds;

    public CateAdapter(Context context, ArrayList<monAn> monAns) {
        this.context = context;
        this.monAns = monAns;
    }

    @NonNull
    @Override
    public CateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cate_rcv, parent, false);
        return new CateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CateViewHolder holder, int position) {
        monAn monAn1 = monAns.get(position);
        holder.imgFood.setImageResource(monAn1.getAnhMA());
        holder.txtFood.setText(monAn1.getTenMA().toString().trim());
        holder.txtPrice.setText(monAn1.getGiaTien().toString().trim()+"$");
        holder.cardViewPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(holder.txtQuantity.getText().toString().trim());
                holder.txtQuantity.setText(Integer.toString(++n));
            }
        });
        holder.cardViewMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(holder.txtQuantity.getText().toString().trim());
                if(n > 0) {
                    holder.txtQuantity.setText(Integer.toString(--n));
                }
            }
        });
        holder.cardViewAdd.setOnClickListener(new View.OnClickListener() {//tao cthd up len firebase tinh trang la chua dat
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MY_ACTION);
                CTHD cthd = new CTHD(monAn1, Integer.parseInt(holder.txtQuantity.getText().toString().trim()), "HD01");
                Gson gson = new Gson();
                String json = gson.toJson(cthd);
                intent.putExtra(MY_CTHD, json);
                context.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return monAns == null ? 0 : monAns.size();
    }
    public void setFilteredList(ArrayList<monAn> filteredList) {
        this.monAns = filteredList;
        notifyDataSetChanged();
    }
    public class CateViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgFood;
        private TextView txtFood, txtPrice, txtQuantity;
        private CardView cardViewPlus, cardViewMinus, cardViewAdd;
        public CateViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewMinus = itemView.findViewById(R.id.cuscate_btnMinus);
            cardViewPlus = itemView.findViewById(R.id.cuscate_btnPlus);
            cardViewAdd = itemView.findViewById(R.id.cuscate_btnAdd);
            imgFood = itemView.findViewById(R.id.cuscate_imgcate);
            txtFood = itemView.findViewById(R.id.cuscate_txtcate);
            txtPrice = itemView.findViewById(R.id.cuscate_txtprice);
            txtQuantity = itemView.findViewById(R.id.cuscate_txtquan);
        }
    }
}

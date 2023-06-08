package com.example.damb_qlnh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.CTHD;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private ArrayList<CTHD> cthds;

    public CartAdapter(Context context, ArrayList<CTHD> CTHD) {
        this.context = context;
        this.cthds= CTHD;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart_rcv,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CTHD cthd = cthds.get(position);
        Glide.with(context).load(cthd.getMonAn().getAnhMA()).into(holder.imgFood);
        holder.txtFood.setText(cthd.getMonAn().getTenMA().toString().trim());
        holder.txtPrice.setText(formatMoney(cthd.getMonAn().getGiaTien().toString().trim()));
        holder.txtQuantity.setText(String.valueOf(cthd.getSoLuong()));
        holder.cardViewPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(holder.txtQuantity.getText().toString().trim());
                holder.txtQuantity.setText(Integer.toString(++n));
                cthds.get(holder.getAdapterPosition()).setSoLuong(n);
                notifyDataSetChanged();
            }
        });
        holder.cardViewMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = Integer.parseInt(holder.txtQuantity.getText().toString().trim());
                if(n > 0){
                    holder.txtQuantity.setText(Integer.toString(--n));
                    cthds.get(holder.getAdapterPosition()).setSoLuong(n);
                    notifyDataSetChanged();
                }
            }
        });
        holder.imgBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cthds.remove(cthd);
                 notifyDataSetChanged();
            }
        });
    }
    public String formatMoney(String input){
        StringBuilder result = new StringBuilder();
        int length = input.length();
        int dotCount = length / 3;

        int remainingDigits = length % 3;
        if (remainingDigits != 0) {
            result.append(input, 0, remainingDigits);
            if (dotCount > 0) {
                result.append(".");
            }
        }

        for (int i = 0; i < dotCount; i++) {
            int startIndex = remainingDigits + i * 3;
            result.append(input.substring(startIndex, startIndex + 3));
            if (i < dotCount - 1) {
                result.append(".");
            }
        }
        return result.toString();
    }
    @Override
    public int getItemCount() {
        return cthds == null ? 0 : cthds.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgFood;
        private TextView txtFood, txtPrice, txtQuantity;
        private CardView cardViewPlus, cardViewMinus;
        private ImageButton imgBin;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewMinus = itemView.findViewById(R.id.cuscart_btnMinus);
            cardViewPlus = itemView.findViewById(R.id.cuscart_btnPlus);
            imgFood = itemView.findViewById(R.id.cuscart_imgfood);
            txtFood = itemView.findViewById(R.id.cuscart_txtfood);
            txtPrice = itemView.findViewById(R.id.cuscart_txtprice);
            txtQuantity = itemView.findViewById(R.id.cuscart_txtquan);
            imgBin = itemView.findViewById(R.id.cuscart_bin);
        }
    }
}

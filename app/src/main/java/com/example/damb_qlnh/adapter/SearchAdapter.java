package com.example.damb_qlnh.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.activity.UserHome;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.monAn;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private Context context;
    private ArrayList<monAn> monAns;
    private String maHD;

    public SearchAdapter(Context context, ArrayList<monAn> monAns) {
        this.context = context;
        this.monAns = monAns;
        SharedPreferences prefs = context.getSharedPreferences("dba", MODE_PRIVATE);
        maHD = prefs.getString("maHD", "");
    }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cate_rcv, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        monAn monAn1 = monAns.get(position);
        Glide.with(context).load(monAn1.getAnhMA()).into(holder.imgFood);
        holder.txtFood.setText(monAn1.getTenMA().toString().trim());
        holder.txtPrice.setText(formatMoney(monAn1.getGiaTien())+" đ");
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
                if(Integer.parseInt(String.valueOf(holder.txtQuantity.getText())) > 0)
                {
                    CTHD cthd = new CTHD(monAn1, Integer.parseInt(holder.txtQuantity.getText().toString().trim()), maHD);
                    CateAdapter.addCTHDS(cthd);
                }
            }
        });

    }
    public void setFilteredList(ArrayList<monAn> filteredList) {
        this.monAns = filteredList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return monAns == null ? 0 : monAns.size();
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

    public class SearchViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgFood;
        private TextView txtFood, txtPrice, txtQuantity;
        private CardView cardViewPlus, cardViewMinus, cardViewAdd;
        public SearchViewHolder(@NonNull View itemView) {
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

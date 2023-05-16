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
import com.example.damb_qlnh.activity.UserCategories;
import com.example.damb_qlnh.models.monAn;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.PopularViewHolder>{
    private Context context;
    private ArrayList<monAn> monAns;

    public PopularAdapter(Context context, ArrayList<monAn> monAns) {
        this.context = context;
        this.monAns = monAns;
    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_home_popular,parent,false);
        return new PopularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        monAn monAn = monAns.get(position);
        holder.txtCategories.setText(monAn.getTenMA().toString().trim());
        holder.imgCategories.setImageResource(monAn.getAnhMA());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserCategories.class);
                intent.putExtra("loaiMA", monAn.getLoaiMA().toString().trim());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return monAns == null ? 0 : monAns.size();
    }

    public class PopularViewHolder extends RecyclerView.ViewHolder{
        private TextView txtCategories;
        private ImageView imgCategories;
        private CardView cardView;
        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategories = itemView.findViewById(R.id.cushome_txtpopular);
            imgCategories = itemView.findViewById(R.id.cushome_imgpopular);
            cardView = itemView.findViewById(R.id.cushome_cv1);
        }
    }
}

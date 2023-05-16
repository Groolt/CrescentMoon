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
import com.example.damb_qlnh.models.Categories;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CatergoriesViewHolder>{
    private Context context;
    private ArrayList<Categories> categories;

    public CategoriesAdapter(Context context, ArrayList<Categories> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CatergoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_home_rcv1,parent,false);
        return new CatergoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatergoriesViewHolder holder, int position) {
        Categories categories1 = categories.get(position);
        holder.txtCategories.setText(categories1.getName().toString().trim());
        holder.imgCategories.setImageResource(categories1.getImg());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserCategories.class);
                intent.putExtra("loaiMA", categories1.getName().toString().trim());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    public class CatergoriesViewHolder extends RecyclerView.ViewHolder{
        private TextView txtCategories;
        private ImageView imgCategories;
        private CardView cardView;
        public CatergoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategories = itemView.findViewById(R.id.cushome_txtcategories);
            imgCategories = itemView.findViewById(R.id.cushome_imgcategories);
            cardView = itemView.findViewById(R.id.cushome_cv);
        }
    }
}

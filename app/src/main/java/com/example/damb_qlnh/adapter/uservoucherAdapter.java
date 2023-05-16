package com.example.damb_qlnh.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.activity.UserCart;
import com.example.damb_qlnh.models.vouCher;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class uservoucherAdapter extends RecyclerView.Adapter<uservoucherAdapter.uservoucherViewHolder> {
    private ArrayList<vouCher> vouChers;
    private Context context;

    public uservoucherAdapter(ArrayList<vouCher> vouChers, Context context) {
        this.vouChers = vouChers;
        this.context = context;
    }

    @NonNull
    @Override
    public uservoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_voucher_rcv, parent, false);
        return new uservoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull uservoucherViewHolder holder, int position) {
        vouCher vouCher = vouChers.get(position);
        holder.disCount.setText("Discount " + vouCher.getGiaTri() + "%");
        String strBD = vouCher.getNgayBD();
        String strKT = vouCher.getNgayKT();
        String strMota = "Thời gian: " + strBD + "-" + strKT + "\nSố lượng còn lại: " + vouCher.getSoLuong();
        holder.moTa.setText(strMota);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = context.getSharedPreferences("dba", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("GTVC", vouCher.getGiaTri());
                editor.commit();
                context.startActivity(new Intent(context, UserCart.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return vouChers != null ? vouChers.size() : 0;
    }

    public class uservoucherViewHolder extends RecyclerView.ViewHolder{
        private TextView disCount, moTa;
        private ImageButton imageButton;
        public uservoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            disCount = itemView.findViewById(R.id.discount);
            moTa = itemView.findViewById(R.id.mota);
            imageButton = itemView.findViewById(R.id.imageView7);
        }
    }
}

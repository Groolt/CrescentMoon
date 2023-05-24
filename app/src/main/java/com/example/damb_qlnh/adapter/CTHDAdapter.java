package com.example.damb_qlnh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.monAn;

import java.util.ArrayList;

public class CTHDAdapter extends RecyclerView.Adapter<CTHDAdapter.CTHDViewHolder> {
    private Context context;
    private ArrayList<CTHD> cthds;

    public CTHDAdapter(Context context, ArrayList<CTHD> cthds) {
        this.context = context;
        this.cthds = cthds;
    }

    @NonNull
    @Override
    public CTHDViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_detail_his_rcv, parent, false);
        return new CTHDViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CTHDViewHolder holder, int position) {
        CTHD cthd = cthds.get(position);
//        holder.imgMon.setImageResource(cthd.getMonAn().getAnhMA());
        holder.txtGia.setText(Integer.toString(Integer.parseInt(cthd.getMonAn().getGiaTien()) * cthd.getSoLuong()) + "$");
        holder.txttenMon.setText(cthd.getMonAn().getTenMA());
        holder.txtloaiMon.setText("Loại: " + cthd.getMonAn().getLoaiMA());
        holder.txtsoLuong.setText("SL: " + cthd.getSoLuong());
    }

    @Override
    public int getItemCount() {
        return cthds == null ? 0 : cthds.size();
    }

    public class CTHDViewHolder extends RecyclerView.ViewHolder{
        private TextView txttenMon, txtloaiMon, txtsoLuong, txtGia;
        private ImageView imgMon;
        public CTHDViewHolder(@NonNull View itemView) {
            super(itemView);
            txttenMon = itemView.findViewById(R.id.tenmon);
            txtloaiMon = itemView.findViewById(R.id.editable);
            txtGia = itemView.findViewById(R.id.gia);
            imgMon = itemView.findViewById(R.id.anhmon);
            txtsoLuong = itemView.findViewById(R.id.soluong);
        }
    }
}

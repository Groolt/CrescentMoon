package com.example.damb_qlnh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mon_cthd, parent, false);
        return new CTHDViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CTHDViewHolder holder, int position) {
        CTHD cthd = cthds.get(position);
        Glide.with(context).load(cthd.getMonAn().getAnhMA()).into(holder.imgMon);
        String numString = String.valueOf(Integer.parseInt(cthd.getMonAn().getGiaTien()) * cthd.getSoLuong());
        String str = "";
        for (int i = 0; i < numString.length() ; i++){
            if((numString.length() - i - 1) % 3 == 0 && i < numString.length()-1){
                str += Character.toString(numString.charAt(i)) + ".";
            }else{
                str += Character.toString(numString.charAt(i));
            }
        }
        holder.txtGia.setText(str + " vnđ");
        holder.txttenMon.setText(cthd.getMonAn().getTenMA());
        holder.txtsoLuong.setText("SL: " + cthd.getSoLuong());
    }

    @Override
    public int getItemCount() {
        return cthds == null ? 0 : cthds.size();
    }

    public class CTHDViewHolder extends RecyclerView.ViewHolder{
        private TextView txttenMon, txtsoLuong, txtGia;
        private ImageView imgMon;
        public CTHDViewHolder(@NonNull View itemView) {
            super(itemView);
            txttenMon = itemView.findViewById(R.id.tenmon);
            txtGia = itemView.findViewById(R.id.gia);
            imgMon = itemView.findViewById(R.id.anhmon);
            txtsoLuong = itemView.findViewById(R.id.editable);
        }
    }
}

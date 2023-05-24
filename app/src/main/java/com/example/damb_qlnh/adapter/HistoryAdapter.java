package com.example.damb_qlnh.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.hoaDon;
import com.example.damb_qlnh.models.monAn;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    private ArrayList<hoaDon> hoaDons;
    private Context context;

    public HistoryAdapter(ArrayList<hoaDon> hoaDons, Context context) {
        this.hoaDons = hoaDons;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_his_rcv, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        hoaDon hoaDon1 = hoaDons.get(position);
        holder.txtMaHD.setText("ID: " + hoaDon1.getMaHD().toString().trim());
        holder.txtNgay.setText(hoaDon1.getThoiGian().toString().trim());
        holder.txtTotal.setText("Total: " + hoaDon1.getTongTien_S().toString().trim() + "$");
        holder.cardViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(hoaDon1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hoaDons == null ? 0 : hoaDons.size();
    }
    public void showDialog(hoaDon hoaDon1){
        Dialog dialog = new Dialog(context, android.R.style.Theme_Material_Light_Dialog_Presentation);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_detail);
        Button btnOkay;
        TextView txtin4KH = dialog.findViewById(R.id.custom_detail_in4KH);
        String string = hoaDon1.getMaHD() + "\n" + hoaDon1.getMaKH() + "\n" + hoaDon1.getThoiGian();
        txtin4KH.setText(string);
        TextView txtGia = dialog.findViewById(R.id.tongtien);
        txtGia.setText(hoaDon1.getTongTien_S().toString().trim() +"$");
        RecyclerView recyclerView = dialog.findViewById(R.id.rcv);
        ArrayList<CTHD> l = new ArrayList<>();
//        l.add(new CTHD((new monAn("1","Pizza", "Main Course", "1", R.drawable.test)), 2,"1"));
//        l.add(new CTHD((new monAn("2","Pizza", "Main Course", "1", R.drawable.test)), 1,"1"));
//        l.add(new CTHD((new monAn("3","Pizza", "Main Course", "1", R.drawable.test)), 2,"1"));
//        l.add(new CTHD((new monAn("4","Pizza", "Main Course", "1", R.drawable.test)), 3,"1"));
//        l.add(new CTHD((new monAn("5","Pizza", "Main Course", "1", R.drawable.test)), 2,"1"));
//        l.add(new CTHD((new monAn("6","Pizzajj", "Main Course", "1", R.drawable.test)), 2,"1")); // getCTHD from db
        CTHDAdapter cthdAdapter = new CTHDAdapter(context, l);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cthdAdapter);
        btnOkay = dialog.findViewById(R.id.gddetail_btnOkay);
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        private TextView txtMaHD, txtNgay, txtTotal;
        private CardView cardViewDetail;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaHD = itemView.findViewById(R.id.cushis_txtmaHD);
            txtNgay = itemView.findViewById(R.id.cushis_txtday);
            txtTotal = itemView.findViewById(R.id.cushis_txttotal);
            cardViewDetail = itemView.findViewById(R.id.btnDetail);
        }
    }
}

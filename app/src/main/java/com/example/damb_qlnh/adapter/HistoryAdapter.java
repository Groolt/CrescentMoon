package com.example.damb_qlnh.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.hoaDon;
import com.example.damb_qlnh.models.monAn;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    private ArrayList<hoaDon> hoaDons;
    private Context context;
    private ArrayList<CTHD> l;
    private CTHDAdapter cthdAdapter;
    private ProgressDialog progressDialog;

    public HistoryAdapter(ArrayList<hoaDon> hoaDons, Context context) {
        this.hoaDons = hoaDons;
        this.context = context;
        progressDialog = new ProgressDialog(context);
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
        holder.txtTotal.setText("Total: " + formatMoney(hoaDon1.getTongTien_S()) + " đ");
        holder.cardViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(hoaDon1);
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
        txtGia.setText(formatMoney(hoaDon1.getTongTien_S()) + "đ");
        RecyclerView recyclerView = dialog.findViewById(R.id.rcv);
        l = new ArrayList<>();
        cthdAdapter = new CTHDAdapter(context, l);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cthdAdapter);
        getCTHD(hoaDon1);
        btnOkay = dialog.findViewById(R.id.gddetail_btnOkay);
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void getCTHD(hoaDon hoaDon1){
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CTHD").whereEqualTo("maHD", hoaDon1.getMaHD())
                .whereEqualTo("tinhTrang", 0)
                .get() .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                                CTHD cthd = document.toObject(CTHD.class);
                                if(l.contains(cthd)){
                                    int index = l.indexOf(cthd);
                                    cthd.setSoLuong(l.get(index).getSoLuong() + cthd.getSoLuong());
                                    l.remove(index);
                                }
                                l.add(document.toObject(CTHD.class));
                            }
                        cthdAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
        progressDialog.dismiss();
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

package com.example.damb_qlnh.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.CTHD;
import com.example.damb_qlnh.models.monAn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class monAdapterCTBan extends BaseAdapter {
    private Activity context;
    private int layout;
    private List<CTHD> cthds;
    private String maHD;

    public monAdapterCTBan(@NonNull Context context, int resource, @NonNull ArrayList<CTHD> objects) {
        this.context = (Activity) context;
        this.layout = resource;
        this.cthds = objects;
    }

    public void setMaHD (String maHD) {
        this.maHD = maHD;
    }

    @Override
    public int getCount() {
        return cthds.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(layout, null);

        CTHD cthd = cthds.get(position);

        TextView tenMon = convertView.findViewById(R.id.tenmon);
        TextView sl = convertView.findViewById(R.id.editable);
        ImageView anh = convertView.findViewById(R.id.anhmon);
        TextView gia = convertView.findViewById(R.id.gia);
        ImageView check = convertView.findViewById(R.id.check);

        if (cthd != null){
            tenMon.setText(cthd.getMonAn().getTenMA());
            sl.setText("SL: " + Integer.toString(cthd.getSoLuong()));
            Glide.with(context).load(cthd.getMonAn().getAnhMA()).into(anh);
            String numString = String.valueOf(Integer.parseInt(cthd.getMonAn().getGiaTien()) * cthd.getSoLuong());
            String str = "";
            for (int i = 0; i < numString.length() ; i++){
                if((numString.length() - i - 1) % 3 == 0 && i < numString.length()-1){
                    str += Character.toString(numString.charAt(i)) + ".";
                }else{
                    str += Character.toString(numString.charAt(i));
                }
            }
            gia.setText(str + " vnđ");
            if (check != null){
                if (cthd.getTinhTrang() == 1) check.setVisibility(View.GONE);
                check.setOnClickListener(view -> {
                    FirebaseFirestore db;
                    db = FirebaseFirestore.getInstance();
                    db.collection("CTHD")
                            .whereEqualTo("maHD", maHD)
                            .whereEqualTo("monAn.maMA", cthd.getMonAn().getMaMA())
                            .get()
                            .addOnCompleteListener(task -> {
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    db.collection("CTHD").document(document.getId()).update("tinhTrang", 1);
                                }
                            });
                });
            }
        }
        return convertView;
    }
    int dptopx(int dp){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void setList(ArrayList<CTHD> listCTHD) {
        this.cthds = listCTHD;
        notifyDataSetChanged();
    }
}

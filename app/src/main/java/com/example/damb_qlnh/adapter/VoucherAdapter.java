package com.example.damb_qlnh.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.vouCher;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class VoucherAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<vouCher> list;
    private int layout;

    public void setList(ArrayList<vouCher> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public VoucherAdapter(Context context, int layout, ArrayList<vouCher> list) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private class ViewHolder {
        TextView discount;
        TextView mota;
        ImageView xoa;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        vouCher vc = list.get(i);
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.discount = view.findViewById(R.id.discount);
            viewHolder.mota = view.findViewById(R.id.mota);
            viewHolder.xoa = view.findViewById(R.id.delete_btn);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.discount.setText("Discount " + vc.getGiaTri() + "%");
        String strBD = vc.getNgayBD();
        String strKT = vc.getNgayKT();
        String strMota = "Thời gian: " + strBD + " – " + strKT + "\nSố lượng còn lại: " + vc.getSoLuong();
        viewHolder.mota.setText(strMota);
        viewHolder.xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa voucher");
                builder.setMessage("Bạn có đồng ý xóa voucher?");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                        DocumentReference documentRef = firestore.collection("Voucher").document(vc.getMaVoucher());

                        documentRef.update("is_deleted", true)
                                .addOnSuccessListener(aVoid -> {
                                    list.remove(i);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Thất bại", Toast.LENGTH_SHORT).show();
                                });
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return view;
    }
}

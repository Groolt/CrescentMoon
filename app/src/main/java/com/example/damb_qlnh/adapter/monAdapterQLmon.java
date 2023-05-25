package com.example.damb_qlnh.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.activity.MainActivity;
import com.example.damb_qlnh.activity.QLMonActivity;
import com.example.damb_qlnh.activity.UserHome;
import com.example.damb_qlnh.models.monAn;
import com.github.dhaval2404.imagepicker.ImagePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class monAdapterQLmon extends BaseAdapter {
    private Activity context;
    private int layout;
    private ArrayList<monAn> dsMon;
    private boolean isImgChanged;
    Dialog dialog;
    public monAdapterQLmon(@NonNull Context context, int resource, @NonNull ArrayList<monAn> objects) {
        this.context = (Activity) context;
        this.layout = resource;
        this.dsMon = objects;
    }

    public void setFilteredList(ArrayList<monAn> filteredList) {
        this.dsMon = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dsMon.size();
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

        monAn mon = dsMon.get(position);

        TextView tenMon = convertView.findViewById(R.id.tenmon);
        TextView loai = convertView.findViewById(R.id.editable);
        ImageView anh = convertView.findViewById(R.id.anhmon);
        TextView gia = convertView.findViewById(R.id.gia);
        ImageView huy = convertView.findViewById(R.id.huymon);
        LinearLayout ln = convertView.findViewById(R.id.linearLayout3);

        if (mon != null){
            tenMon.setText(mon.getTenMA());
            loai.setText("Loại: " + mon.getLoaiMA());

            Glide.with(context).load(mon.getAnhMA()).into(anh);
            String tien = mon.getGiaTien();

            gia.setText(formatMoney(tien)+ " đ");

            ln.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isImgChanged = false;
                    dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.them_mon_an);
                    Window window = dialog.getWindow();
                    if (window == null) return;
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    CardView cancel = dialog.findViewById(R.id.cancel_button);
                    cancel.setOnClickListener(view1 -> dialog.dismiss());

                    TextView addBtn = dialog.findViewById(R.id.addBtn);
                    Spinner spn = dialog.findViewById(R.id.Loai);
                    ArrayAdapter adapter = new ArrayAdapter<>(
                            context,
                            R.layout.style_spinner_form1,
                            Arrays.asList("Appetizers", "Main Courses", "Side Dishes", "Drinks", "Desserts"));
                    spn.setAdapter(adapter);
                    spn.setSelection(adapter.getPosition(mon.getLoaiMA()));

                    ImageView img = dialog.findViewById(R.id.img);
                    img.setImageDrawable(anh.getDrawable());

                    EditText ten = dialog.findViewById(R.id.tenMon);
                    ten.setText(mon.getTenMA());

                    EditText gia = dialog.findViewById(R.id.Gia);
                    gia.setText(mon.getGiaTien());
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImagePicker.with(context)
                                    .crop()
                                    .compress(1024)
                                    .maxResultSize(1080, 1080)
                                    .start();
                        }
                    });
                    addBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImagePicker.with(context)
                                    .crop()
                                    .compress(1024)
                                    .maxResultSize(1080, 1080)
                                    .start();
                        }
                    });

                    CardView confirmButton = dialog.findViewById(R.id.confirm_button);
                    TextView tvConfirm = dialog.findViewById(R.id.tv);
                    tvConfirm.setText("Cập nhật");
                    confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String tmp = ten.getText().toString();
                            StringBuilder imgFileName = new StringBuilder();

                            for (int i = 0; i < tmp.length(); i++) {
                                char currentChar = tmp.charAt(i);
                                if (currentChar != ' ') {
                                    imgFileName.append(currentChar);
                                }
                            }
                            Map<String, Object> newData = new HashMap<>();
                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();
                            StorageReference imagesRef = storageRef.child("monAn/"+imgFileName.toString()+".jpg");
                            DocumentReference documentRef = firestore.collection("monAn").document(mon.getMaMA());
                            if(isImgChanged){
                                Drawable drawable = img.getDrawable();
                                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] imageData = baos.toByteArray();
                                UploadTask uploadTask = imagesRef.putBytes(imageData);
                                uploadTask.addOnSuccessListener(taskSnapshot ->
                                        imagesRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                                            newData.put("img",downloadUri.toString());
                                            newData.put("ten", ten.getText().toString());
                                            newData.put("gia", Integer.parseInt(gia.getText().toString()));
                                            newData.put("loai", spn.getSelectedItem().toString());
                                            newData.put("is_deleted", false);
                                            documentRef.update(newData)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        mon.setAnhMA(downloadUri.toString());
                                                        mon.setGiaTien(formatMoney(gia.getText().toString()));
                                                        mon.setLoaiMA(spn.getSelectedItem().toString());
                                                        mon.setTenMA(ten.getText().toString());
                                                        dsMon.set(position, mon);
                                                        notifyDataSetChanged();
                                                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }).addOnFailureListener(
                                                e -> Toast.makeText(context, "error: " + e.toString(), Toast.LENGTH_SHORT).show())).addOnFailureListener(e -> {
                                });
                            } else {
                                newData.put("img", mon.getAnhMA());
                                newData.put("ten", ten.getText().toString());
                                newData.put("gia", Integer.parseInt(gia.getText().toString()));
                                newData.put("loai", spn.getSelectedItem().toString());
                                newData.put("is_deleted", false);
                                documentRef.update(newData)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mon.setGiaTien(formatMoney(gia.getText().toString()));
                                                    mon.setLoaiMA(spn.getSelectedItem().toString());
                                                    mon.setTenMA(ten.getText().toString());
                                                    dsMon.set(position, mon);
                                                    notifyDataSetChanged();
                                                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }


                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });

            huy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Xóa món ăn");
                    builder.setMessage("Bạn có đồng ý xóa món "+ mon.getTenMA() +"?");
                    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            DocumentReference documentRef = firestore.collection("monAn").document(mon.getMaMA());

                            documentRef.update("is_deleted", true)
                                    .addOnSuccessListener(aVoid -> {
                                        dsMon.remove(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Thất bại", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
        return convertView;
    }

    public void updateImg(Uri uri) {
        ImageView img = dialog.findViewById(R.id.img);
        img.setImageURI(uri);
        isImgChanged = true;
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
}

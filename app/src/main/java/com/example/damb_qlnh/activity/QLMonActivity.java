package com.example.damb_qlnh.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damb_qlnh.adapter.monAdapterQLmon;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.monAn;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.core.OrderBy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QLMonActivity extends AppCompatActivity {

    ArrayList<monAn> monList;
    monAdapterQLmon adapter;
    ListView lv;
    public static Dialog dialog;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlmon);
        db = FirebaseFirestore.getInstance();

        //setup action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.round_arrow_back_24);

        //list mon an
        monList = new ArrayList<>();
        getListMon();
        lv = findViewById(R.id.lv);

        //search
        SearchView searchView = findViewById(R.id.searchview);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchView.isIconified())
                    searchView.setIconified(true);
                else
                    searchView.setQuery("", false);
                getListMon();
            }
        });

        // Them mon an
        ImageView addbtn = findViewById(R.id.addbtn);
        addbtn.setOnClickListener(view -> {
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.them_mon_an);
            Window window = dialog.getWindow();
            if (window == null) return;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            CardView cancel = dialog.findViewById(R.id.cancel_button);
            cancel.setOnClickListener(view1 -> dialog.dismiss());

            ImageView img = dialog.findViewById(R.id.img);
            TextView addBtn = dialog.findViewById(R.id.addBtn);
            Spinner spn = dialog.findViewById(R.id.Loai);
            spn.setAdapter(new ArrayAdapter<>(
                    QLMonActivity.this,
                    R.layout.style_spinner_form1,
                    Arrays.asList("Appetizers", "Main Courses", "Side Dishes", "Drinks", "Desserts")));
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePicker.with(QLMonActivity.this)
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start();
                }
            });
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImagePicker.with(QLMonActivity.this)
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start();
                }
            });

            CardView confirmButton = dialog.findViewById(R.id.confirm_button);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText ten = dialog.findViewById(R.id.tenMon);
                    EditText gia = dialog.findViewById(R.id.Gia);

                    String tmp = ten.getText().toString();
                    StringBuilder imgFileName = new StringBuilder();

                    for (int i = 0; i < tmp.length(); i++) {
                        char currentChar = tmp.charAt(i);
                        if (currentChar != ' ') {
                            imgFileName.append(currentChar);
                        }
                    }
                    Map<String, Object> data = new HashMap<>();

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference imagesRef = storageRef.child("monAn/"+imgFileName.toString()+".jpg");

                    Drawable drawable = img.getDrawable();
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageData = baos.toByteArray();
                    UploadTask uploadTask = imagesRef.putBytes(imageData);
                    uploadTask.addOnSuccessListener(taskSnapshot ->
                            imagesRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                                data.put("img",downloadUri.toString());
                                data.put("ten", ten.getText().toString());
                                data.put("gia", Integer.parseInt(gia.getText().toString()));
                                data.put("loai", spn.getSelectedItem().toString());
                                data.put("is_deleted", false);
                                db.collection("monAn").add(data);
                                getListMon();
                                Toast.makeText(QLMonActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(
                            e -> Toast.makeText(QLMonActivity.this, "Upload image fail", Toast.LENGTH_SHORT).show())).addOnFailureListener(e -> {
                    });

                    dialog.dismiss();
                }
            });

            dialog.show();
        });

    }

    private void filterList(String newText) {
        ArrayList<monAn> filteredList = new ArrayList<>();
        for (monAn mon : monList) {
            if (mon.getTenMA().toLowerCase().contains(newText.toLowerCase())
                    || mon.getLoaiMA().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(mon);
            }
        }
        adapter.setFilteredList(filteredList);
    }

    private void getListMon() {
        monList.clear();
        db.collection("monAn")
                .whereEqualTo("is_deleted", false)
                .orderBy("loai").orderBy("ten")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            monList.add(new monAn(
                                    document.getId(),
                                    document.getString("ten"),
                                    document.getString("loai"),
                                    String.valueOf(document.getDouble("gia").intValue()),
                                    document.getString("img")));
                        }
                        adapter = new monAdapterQLmon(this, R.layout.mon_qlmon, monList);
                        lv.setAdapter(adapter);
                    } else {
                        Toast.makeText(this, "Can't get data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (dialog!= null && dialog.isShowing()) {
                ImageView imageView = dialog.findViewById(R.id.img);
                imageView.setImageURI(uri);
            } else {
                adapter.updateImg(uri);
            }
        }
    }
}
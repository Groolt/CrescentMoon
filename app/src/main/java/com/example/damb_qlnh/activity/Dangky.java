package com.example.damb_qlnh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.khachHang;
import com.example.damb_qlnh.models.monAn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Dangky extends AppCompatActivity {
    private TextView txtUsername, txtPassword, txtConfirmPass;
    private CardView cardViewSignup;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    final Calendar myCalendar= Calendar.getInstance();
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_dangky);
        Init();
        cardViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, confirmPass;
                email = txtUsername.getText().toString().trim();
                password = txtPassword.getText().toString().trim();
                confirmPass = txtConfirmPass.getText().toString().trim();
                int c = 0;
                if(!confirmPass.equals(password)){
                    txtConfirmPass.setError("Those passwords didn’t match. Try again.");
                    txtConfirmPass.requestFocus();
                    c++;
                }
                if(!email.contains("@gmail.com")){
                    txtUsername.setError("Account is not properly formatted.");
                    txtUsername.requestFocus();
                    c++;
                }
                if(password.length() < 8){
                    txtPassword.setError("Password must be more than 8 characters.");
                    txtPassword.requestFocus();
                }
                if(c != 0){
                    return;
                }
                progressDialog.setTitle("Signing Account");
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(Dangky.this, MainActivity.class);
                                    String tmp = email.substring(0, email.indexOf("@"));
                                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference storageRef = storage.getReference();
                                    StorageReference imagesRef = storageRef.child("khachHang/"+id.toString()+".jpg");

                                    Drawable drawable = getResources().getDrawable(R.drawable.img_5);
                                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] imageData = baos.toByteArray();
                                    UploadTask uploadTask = imagesRef.putBytes(imageData);
                                    uploadTask.addOnSuccessListener(taskSnapshot ->
                                            imagesRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                                                khachHang khachHang = new khachHang();
                                                khachHang.setTenKH(tmp);
                                                khachHang.setMaKH(id);
                                                khachHang.setImg(downloadUri.toString());
                                                khachHang.setGioiTinh("Nam");
                                                khachHang.setSDT("8888-888-888");
                                                khachHang.setXepHang("Bronze");
                                                khachHang.setDob(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
                                                db.collection("khachHang").document(id)
                                                        .set(khachHang);
                                            }).addOnFailureListener(
                                                    e -> Toast.makeText(Dangky.this, "Upload image fail", Toast.LENGTH_SHORT).show())).addOnFailureListener(e -> {
                                    });
                                    startActivity(intent);
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Dangky.this, "An error occurred. Try again.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
            }
        });
    }
    public void Init() {
        txtUsername = findViewById(R.id.gddk_txtusername);
        txtPassword = findViewById(R.id.gddk_txtpass);
        txtConfirmPass = findViewById(R.id.gddk_txtcfpass);
        cardViewSignup = findViewById(R.id.gddk_btnsignup);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(Dangky.this);
    }
}
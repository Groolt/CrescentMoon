package com.example.damb_qlnh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.example.damb_qlnh.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.Utils;
import com.google.zxing.common.StringUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText txtUsername, txtPassword;
    private FirebaseAuth mAuth;
    private CardView cardViewSignin;
    private TextView btnsignup;
    private LinearLayout linearLayout;
    ProgressDialog progressDialog;
    private String maHD = "#HD";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        Init();
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Dangky.class);
                startActivity(intent);
            }
        });
        cardViewSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtUsername.getText().toString().trim().isEmpty()){
                    return;
                }
                String email, password;
                email = txtUsername.getText().toString().trim();
                password = txtPassword.getText().toString().trim();
                progressDialog.setTitle("Logging");
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    if(email.equals("binhnguyen9803@gmail.com")){
                                        startActivity(new Intent(MainActivity.this, MainActivity1.class));
                                    }
                                    else {
                                        startActivity(new Intent(MainActivity.this, UserHome.class));
                                    }
                                }
                                else {
                                    progressDialog.dismiss();
                                    txtUsername.setError("Username/password isn't correct! Try again");
                                    txtUsername.requestFocus();
                                    return;
                                }
                            }
                        });
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(txtUsername.getText().toString().trim().isEmpty()){
                    txtUsername.setError("Please type the username!");
                    txtUsername.requestFocus();
                    return;
                }
                progressDialog.setTitle("Sending Mail");
                progressDialog.show();
                mAuth.sendPasswordResetEmail(txtUsername.getText().toString().trim())
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Account don't exists.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
    public void Init() {
        txtUsername = findViewById(R.id.gddn_txtusername);
        txtPassword = findViewById(R.id.gddn_txtpass);
        cardViewSignin = findViewById(R.id.gddn_btnsignin);
        btnsignup = findViewById(R.id.gddn_btnsignup);
        linearLayout = findViewById(R.id.lnlayforgot);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(MainActivity.this);
        getMaHD();
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
    public void getMaHD(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("HoaDon")
                .whereEqualTo("tinhTrang", 1)
                .count()
                .get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task1) {
                        if (task1.isSuccessful()) {
                            // Count fetched successfully
                            AggregateQuerySnapshot snapshot1 = task1.getResult();
                            if (snapshot1.getCount() < 10) {
                                maHD += "000";
                            } else if (snapshot1.getCount() < 100) {
                                maHD += "00";
                            } else if (snapshot1.getCount() < 1000) {
                                maHD += "0";
                            }
                            maHD += String.valueOf(snapshot1.getCount() + 1);
                            SharedPreferences prefs = getSharedPreferences("dba", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("maHD", maHD);
                            editor.commit();
                        }
                    }
                });

    }
}
package com.example.damb_qlnh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damb_qlnh.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText txtUsername, txtPassword;
    private FirebaseAuth mAuth;
    private CardView cardViewSignin;
    private TextView btnsignup;
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
                String email, password;
                email = txtUsername.getText().toString().trim();
                password = txtPassword.getText().toString().trim();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if(email.equals("binhnguyen9803@gmail.com")){
                                        startActivity(new Intent(MainActivity.this, MainActivity1.class));
                                    }
                                    else {
                                        startActivity(new Intent(MainActivity.this, UserHome.class));
                                    }
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Username/password isn't correct! Try again", Toast.LENGTH_SHORT).show();
                                    return;
                                }
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
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
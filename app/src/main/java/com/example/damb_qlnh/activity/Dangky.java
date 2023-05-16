package com.example.damb_qlnh.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damb_qlnh.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Dangky extends AppCompatActivity {
    private TextView txtUsername, txtPassword, txtConfirmPass;
    private CardView cardViewSignup;
    private FirebaseAuth mAuth;
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
                    Toast.makeText(Dangky.this, "Those passwords didn’t match. Try again.", Toast.LENGTH_SHORT).show();
                    c++;
                }
                if(!email.contains("@gmail.com")){
                    Toast.makeText(Dangky.this, "Account is not properly formatted.", Toast.LENGTH_SHORT).show();
                    c++;
                }
                if(password.length() < 8){
                    Toast.makeText(Dangky.this, "Password must be more than 8 characters.", Toast.LENGTH_SHORT).show();
                    c++;
                }
                if(c != 0){
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(Dangky.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                else {
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
    }
}
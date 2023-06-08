package com.example.damb_qlnh.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.damb_qlnh.R;
import com.example.damb_qlnh.models.khachHang;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {
    private TextView txtName;
    private EditText txtPhone, txtGender, txtRank, txtBirthday;
    private Button btnBack;
    final Calendar myCalendar= Calendar.getInstance();
    private ImageButton imageButton;
    private CircleImageView circleImageView;
    private CardView cardViewEdit, cardViewLogout;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    DocumentReference documentRef = firestore.collection("khachHang").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

    private khachHang khachHang1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        init();
        setData();
        cardViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logout
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserProfile.this, MainActivity.class));
                finish();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cardViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home:
                        startActivity(new Intent(UserProfile.this, UserHome.class));
                        break;
                    case R.id.action_history:
                        startActivity(new Intent(UserProfile.this, UserHistory.class));
                        break;
                    case R.id.action_order:
                        startActivity(new Intent(UserProfile.this, UserOrder.class));
                        break;
                    case R.id.action_QR:
                        Toast.makeText(UserProfile.this, "QR",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(UserProfile.this)
                        .cropSquare()//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            circleImageView.setImageURI(uri);
            StorageReference imagesRef = storageRef.child("khachHang/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg");
            Drawable drawable = circleImageView.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();
            UploadTask uploadTask = imagesRef.putBytes(imageData);
            uploadTask.addOnSuccessListener(taskSnapshot ->
                    imagesRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                        khachHang1.setImg(downloadUri.toString());
                        documentRef.set(khachHang1);
                    }));
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserProfile.this, UserHome.class));
    }
    public void init(){
        txtName = findViewById(R.id.gdpro_txtnameclient);
        txtRank = findViewById(R.id.gdpro_txtrank);
        txtGender = findViewById(R.id.gdpro_txtgender);
        txtPhone = findViewById(R.id.gdpro_txtphone);
        txtBirthday = findViewById(R.id.gdpro_txtdob);
        btnBack = findViewById(R.id.btn_back_profile);
        imageButton = findViewById(R.id.gdpro_imgvoucher);
        cardViewEdit = findViewById(R.id.gdpro_btnedit);
        cardViewLogout = findViewById(R.id.gdpro_btnlog);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.action_profile);
        circleImageView = findViewById(R.id.gdpro_imgprofile);
        mAuth = FirebaseAuth.getInstance();
        khachHang1 = (khachHang) getIntent().getSerializableExtra("khachHang");
    }
    public void changePassword(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.change_password_dialog);
        Window window = dialog.getWindow();
        if (window == null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CardView cancel = dialog.findViewById(R.id.cancel_button);
        CardView okay = dialog.findViewById(R.id.confirm_button);
        EditText newPass, confirmPass;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        newPass = dialog.findViewById(R.id.dialog_new_pass);
        confirmPass = dialog.findViewById(R.id.dialog_confirm_pass);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!confirmPass.getText().toString().trim().equals(newPass.getText().toString().trim())){
                    Toast.makeText(UserProfile.this, "Those passwords didn’t match. Try again.", Toast.LENGTH_SHORT).show();
                }
                else{
                    String newPassword = confirmPass.getText().toString().trim();
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(UserProfile.this, "Successful update", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            });
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void setData(){
        txtName.setText(khachHang1.getTenKH().toString().trim());
        txtRank.setText(khachHang1.getXepHang().toString().trim());
        txtPhone.setText(khachHang1.getSDT().toString().trim());
        txtBirthday.setText(khachHang1.getDob().toString().trim());
        txtGender.setText(khachHang1.getGioiTinh().toString().trim());
        Glide.with(UserProfile.this).load(khachHang1.getImg()).into(circleImageView);
    }
    public void showDialog(){
        Dialog dialog = new Dialog(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_profile);
        Button btnOkay, btnCancle;
        RadioButton rdNam, rdNu;
        TextView txtNameS;
        EditText txtPhoneS, txtBirthdayS;
        btnOkay = dialog.findViewById(R.id.custom_profile_btnAccept);
        btnCancle = dialog.findViewById(R.id.custom_profile_btnCancle);
        txtNameS = dialog.findViewById(R.id.custom_profile_txtname);
        txtPhoneS = dialog.findViewById(R.id.custome_profile_txtPhone);
        txtBirthdayS = dialog.findViewById(R.id.custome_profile_txtbd);
        rdNam = dialog.findViewById(R.id.custome_profile_nam);
        rdNu = dialog.findViewById(R.id.custom_profile_nu);
        DatePickerDialog.OnDateSetListener date = (view1, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);
            txtBirthdayS.setText(new SimpleDateFormat("dd/MM/yyyy").format(myCalendar.getTime()));
        };
        txtBirthdayS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(UserProfile.this, date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rdNam.isChecked())
                    khachHang1.setGioiTinh("Nam");
                else if(rdNu.isChecked())
                    khachHang1.setGioiTinh("Nữ");
                khachHang1.setTenKH(txtNameS.getText().toString().trim());
                khachHang1.setSDT(txtPhoneS.getText().toString().trim());
                khachHang1.setDob(txtBirthdayS.getText().toString().trim());
                setData();
                documentRef.set(khachHang1);
                //Capnhatdata len db
                dialog.dismiss();
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

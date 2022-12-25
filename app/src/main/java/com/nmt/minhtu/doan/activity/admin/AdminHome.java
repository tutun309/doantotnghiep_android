package com.nmt.minhtu.doan.activity.admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.activity.LoginActivity;
import com.nmt.minhtu.doan.data_local.DataLocalManager;
import com.nmt.minhtu.doan.model.User;

import java.io.ByteArrayOutputStream;

public class AdminHome extends AppCompatActivity implements View.OnClickListener {
    ImageView imgAvatar;
    TextView txtName,txtManageUser, txtManageTour, txtManageCity, txtManageHistory, txtProfile;
    Button btnLogout;
    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        init();
        user = DataLocalManager.getUser();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(user.getImg(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        imgAvatar.setImageBitmap(decodedImage);
        txtName.setText("Xin chào "+ user.getName());

        txtManageUser.setOnClickListener(this);
        txtManageTour.setOnClickListener(this);
        txtManageCity.setOnClickListener(this);
        txtManageHistory.setOnClickListener(this);
        txtProfile.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    private void init(){
        imgAvatar = findViewById(R.id.img_avatar_profile);
        txtName = findViewById(R.id.txtName);
        txtManageUser = findViewById(R.id.txt_manage_account);
        txtManageTour = findViewById(R.id.txt_manage_tour);
        txtManageCity = findViewById(R.id.txt_manage_city);
        txtManageHistory = findViewById(R.id.txt_manage_booktour);
        txtProfile = findViewById(R.id.txt_profile);
        btnLogout = findViewById(R.id.btn_admin_logout);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.txt_manage_account:
                Intent intent = new Intent(AdminHome.this, ManageUserActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_manage_tour:
                intent = new Intent(AdminHome.this, ManageTourActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_manage_city:
                intent = new Intent(AdminHome.this, ManageCityActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_manage_booktour:
                intent = new Intent(AdminHome.this, ManageBookingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_admin_logout:
                DataLocalManager.setUser(null);
                intent = new Intent(AdminHome.this, LoginActivity.class);
                startActivity(intent);
                break;

        }
    }
}
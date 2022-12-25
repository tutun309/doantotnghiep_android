package com.nmt.minhtu.doan.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.nmt.minhtu.doan.ImgFromGrallery;
import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.api.ApiService;
import com.nmt.minhtu.doan.api.ResponsePOST;
import com.nmt.minhtu.doan.data_local.DataLocalManager;
import com.nmt.minhtu.doan.model.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 1;
    private Button btnChonAnh, btnDangki;
    private TextView txtTitle;
    private EditText edtEmail, edtPassword, edtName, edtAddress;
    private RadioGroup radioGroupRole;
    private RadioButton radioAdmin, radioUser;
    private ImageView imgAvatar;
    private LinearLayout groupLayoutRole;
    private Bitmap currentBitmap;
    private int vaitroId;
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        Uri uri = data.getData();
                        try {
                            currentBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imgAvatar.setImageBitmap(currentBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initView();
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        btnDangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickPostApi();
            }
        });
        User user = DataLocalManager.getUser();
        if (user != null) {
            vaitroId = user.getVaitroId();
            if (vaitroId == 0) {
                txtTitle.setText("Thêm người dùng");
                btnDangki.setText("Lưu");
            }
        } else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) groupLayoutRole.getLayoutParams();
            params.height = 0;
            params.width = 0;
            groupLayoutRole.setLayoutParams(params);
            groupLayoutRole.setVisibility(View.INVISIBLE);
        }

    }


    private void initView() {

        btnChonAnh = findViewById(R.id.btn_them_anh);
        imgAvatar = findViewById(R.id.img_avatar);
        btnDangki = findViewById(R.id.btn_sign_in);
        edtEmail = findViewById(R.id.txt_email);
        edtPassword = findViewById(R.id.txt_password);
        edtName = findViewById(R.id.edt_name);
        edtAddress = findViewById(R.id.edt_address);
        btnDangki = findViewById(R.id.btn_sign_in);
        radioGroupRole = findViewById(R.id.radio_group_role);
        radioAdmin = findViewById(R.id.radio_admin);
        radioUser = findViewById(R.id.radio_user);
        txtTitle = findViewById(R.id.txt_title);
        groupLayoutRole = findViewById(R.id.group_layout_role);
    }

    private void clickPostApi() {
        User user = new User();
        user.setEmail(edtEmail.getText().toString());
        user.setPassword(edtPassword.getText().toString());
        user.setName(edtName.getText().toString());
        user.setAddress(edtAddress.getText().toString());
        user.setImg(ImgFromGrallery.endCodeImg(currentBitmap));

        //check vai tro la admin thi lay vai tro theo radio
        if(vaitroId == 0){
            int id = radioGroupRole.getCheckedRadioButtonId();
            if(id == R.id.radio_admin){
                user.setVaitroId(0);
            } else if(id == R.id.radio_user){
                user.setVaitroId(1);
            }
        } else {
            user.setVaitroId(1);
        }
        ApiService.apiService.postUser(user).enqueue(new Callback<ResponsePOST>() {
            @Override
            public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                ResponsePOST responsePOST = response.body();
                if (responsePOST.getErrCode() == 0) {
                    Toast.makeText(SignUpActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Toast.makeText(SignUpActivity.this, "" + responsePOST.getMess(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePOST> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "call POST api loi!", Toast.LENGTH_LONG).show();
            }
        });

//
    }

    //---------------request permission---------
    private void onClickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions, MY_REQUEST_CODE);
        }
    }

    //------------------check allow permission---------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }
}
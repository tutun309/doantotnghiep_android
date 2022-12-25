package com.nmt.minhtu.doan.activity.admin;

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
import com.nmt.minhtu.doan.model.Category;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAddCategory extends AppCompatActivity {
    private ImageView img;
    TextView iconBack;
    private EditText edtName;
    private Button btnAddImg, btnSave;
    private static final int MY_REQUEST_CODE = 1;
    private Bitmap currentBitmap;
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
                            img.setImageBitmap(currentBitmap);
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
        setContentView(R.layout.activity_admin_add_category);

        img = findViewById(R.id.img_avatar);
        edtName = findViewById(R.id.txt_name);
        btnAddImg = findViewById(R.id.btn_them_anh);
        btnSave = findViewById(R.id.btn_save);
        iconBack = findViewById(R.id.icon_back);


        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postApi();
            }
        });

        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void postApi() {
        Category category = new Category();
        category.setName(edtName.getText().toString());
        category.setImg(ImgFromGrallery.endCodeImg(currentBitmap));

        //Log.d(">>>>>>>>>Check base64",endCodeImg(currentBitmap)+"");
        ApiService.apiService.postCategory(category).enqueue(new Callback<ResponsePOST>() {
            @Override
            public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                ResponsePOST responsePOST = response.body();
                if(responsePOST.getErrCode() == 0){
                    Toast.makeText(AdminAddCategory.this,"Thêm thành công!",Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Toast.makeText(AdminAddCategory.this,""+responsePOST.getMess(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePOST> call, Throwable t) {
                Toast.makeText(AdminAddCategory.this,"call API loi", Toast.LENGTH_LONG).show();
            }
        });
    }

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
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
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


}
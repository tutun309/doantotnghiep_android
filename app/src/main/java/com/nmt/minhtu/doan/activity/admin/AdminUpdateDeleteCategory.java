package com.nmt.minhtu.doan.activity.admin;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class AdminUpdateDeleteCategory extends AppCompatActivity implements View.OnClickListener {
    TextView iconBack;
    Category currentCategory = new Category();
    ImageView imgAvt;
    EditText editName;
    Button btnAddImg, btnUpdate, btnDelete;
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
                            imgAvt.setImageBitmap(currentBitmap);
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
        setContentView(R.layout.activity_admin_update_delete_category);
        initView();

        setCurrentCategory();
        btnAddImg.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        iconBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.icon_back:
                finish();
                break;
            case R.id.btn_them_anh:
                onClickRequestPermission();
                break;
            case R.id.btn_update:
                currentCategory.setImg(ImgFromGrallery.endCodeImg(currentBitmap));
                currentCategory.setName(editName.getText().toString());
                ApiService.apiService.updateCategory(currentCategory).enqueue(new Callback<ResponsePOST>() {
                    @Override
                    public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                        ResponsePOST responsePOST = response.body();
                        Log.d("TAG", "onResponse: "+responsePOST.getMess());
                        if(responsePOST != null){
                            Toast.makeText(AdminUpdateDeleteCategory.this,""+responsePOST.getMess(),Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(AdminUpdateDeleteCategory.this,"Co loi",Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponsePOST> call, Throwable t) {
                        Toast.makeText(AdminUpdateDeleteCategory.this,"call API loi", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.btn_delete:
                handleDialalogWarning();
                break;
        }
    }

    private void handleDialalogWarning() {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_warning);


            Window window = dialog.getWindow();
            if(window == null){
                return;
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);


            Button btnSure = (Button) dialog.findViewById(R.id.btn_sure);
            Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);

            btnSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ApiService.apiService.deleteCategoryById(currentCategory.getId()).enqueue(new Callback<ResponsePOST>() {
                        @Override
                        public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                            ResponsePOST responsePOST = response.body();
                            Toast.makeText(AdminUpdateDeleteCategory.this,""+responsePOST.getMess(),Toast.LENGTH_LONG).show();
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponsePOST> call, Throwable t) {
                            Toast.makeText(AdminUpdateDeleteCategory.this,"call API loi", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setCurrentCategory() {
        Intent intent = getIntent();
        int idCategory = intent.getIntExtra("currentCategoryId", 1);
        Log.d("TAG", "onCreate: " + idCategory);
        ApiService.apiService.getCategoryById(idCategory).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if(response.body() != null){
                    currentCategory= response.body();
                    currentBitmap = ImgFromGrallery.deCodeToBase64(currentCategory.getImg());
                    imgAvt.setImageBitmap(currentBitmap);
                    editName.setText(currentCategory.getName());
                } else {
                    Toast.makeText(AdminUpdateDeleteCategory.this,"Không tồn tại danh mục này!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Toast.makeText(AdminUpdateDeleteCategory.this,"call API loi", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView() {
        iconBack = findViewById(R.id.icon_back);
        imgAvt = findViewById(R.id.img_avatar);
        editName = findViewById(R.id.txt_name);
        btnAddImg = findViewById(R.id.btn_them_anh);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
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
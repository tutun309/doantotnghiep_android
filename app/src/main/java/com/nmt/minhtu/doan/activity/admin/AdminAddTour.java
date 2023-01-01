package com.nmt.minhtu.doan.activity.admin;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Spinner;
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
import com.nmt.minhtu.doan.adapter.AdminAdapterSpinnerCategory;
import com.nmt.minhtu.doan.api.ApiService;
import com.nmt.minhtu.doan.api.ResponsePOST;
import com.nmt.minhtu.doan.model.Category;
import com.nmt.minhtu.doan.model.Tour;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAddTour extends AppCompatActivity {
    private Spinner spnCategory;
    private ProgressDialog progressDialog;
    private ImageView imgAvt;
    private Button btnAddImg, btnAddTour;
    private EditText edtName, edtPrice, edtDesc;
    private TextView iconBack;
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
//                            Log.d("TAG", "onActivityResult: "+uri);
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
        setContentView(R.layout.activity_admin_add_tour);
        initView();

        showProgress();
        setSpinnerCategory();

        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        /*btnAddTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = edtName.getText().toString();
                    Float price = Float.parseFloat(edtPrice.getText().toString());
                    String desc = edtDesc.getText().toString();
                    Category category = (Category) spnCategory.getSelectedItem();
                    String img = ImgFromGrallery.endCodeImg(currentBitmap);
                    Integer id = category.getId();
                    if(name.equals("") || desc.equals("")){
                        Toast.makeText(AdminAddTour.this,"Xem lại thông tin", Toast.LENGTH_LONG).show();
                    } else {
                        showProgress();
                        Tour tour = new Tour(name,img,price,desc,category);
                        ApiService.apiService.createNewTour(tour).enqueue(new Callback<ResponsePOST>() {
                            @Override
                            public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                                progressDialog.dismiss();
                                ResponsePOST responsePOST = response.body();
                                Toast.makeText(AdminAddTour.this,""+responsePOST.getMess(), Toast.LENGTH_LONG).show();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<ResponsePOST> call, Throwable t) {
                                Toast.makeText(AdminAddTour.this,"Call API loi", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }catch (Exception e){
                }
            }
        });*/
    }

    private void initView(){
        spnCategory = findViewById(R.id.spinner_category);
        imgAvt = findViewById(R.id.img_avatar);
        btnAddImg = findViewById(R.id.btn_them_anh);
        btnAddTour = findViewById(R.id.btn_save);
        edtName = findViewById(R.id.txt_name);
        edtPrice = findViewById(R.id.txt_price);
        edtDesc = findViewById(R.id.txt_desc);
        iconBack = findViewById(R.id.icon_back);
    }

    private void setSpinnerCategory(){
        ApiService.apiService.getListCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categories = response.body();
                AdminAdapterSpinnerCategory adapter = new AdminAdapterSpinnerCategory(categories);
                spnCategory.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(AdminAddTour.this,"call API loi", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showProgress(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Thí chủ hãy đợi chút...!");
        progressDialog.show();
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
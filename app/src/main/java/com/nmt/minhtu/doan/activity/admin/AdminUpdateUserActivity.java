package com.nmt.minhtu.doan.activity.admin;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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

public class AdminUpdateUserActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 1;
    private ProgressDialog progressDialog;
    private Button btnChonAnh, btnUpdate, btnDelete;
    private TextView txtTitle;
    private EditText edtEmail, edtName, edtAddress;
    private RadioGroup radioGroupRole;
    private RadioButton radioAdmin, radioUser;
    private ImageView imgAvatar;
    private LinearLayout groupLayoutRole;
    private Bitmap currentBitmap;
    private User currentUser;
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
        setContentView(R.layout.activity_admin_update_user);

        initView();

        showProgress();
        User user = DataLocalManager.getUser();
        if(user.getVaitroId()==0){
            Intent intent = getIntent();
            int idCurrentUser = intent.getIntExtra("currentUserId", 1);
            setCurrentUser(idCurrentUser);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleDeleteUser(idCurrentUser);
                }
            });

        } else if(user.getVaitroId() == 1){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) groupLayoutRole.getLayoutParams();
            params.height = 0;
            params.width = 0;
            groupLayoutRole.setLayoutParams(params);
            groupLayoutRole.setVisibility(View.INVISIBLE);
            btnDelete.setVisibility(View.INVISIBLE);
            setCurrentUser(user.getId());
        }
        //---------------set onclick cac button---------
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickPostApi();
            }
        });



    }

    private void handleDeleteUser(int idCurrentUser) {
        if(idCurrentUser == DataLocalManager.getUser().getId()) {
            Toast.makeText(AdminUpdateUserActivity.this,"Bạn không thể xóa chính mình!", Toast.LENGTH_LONG).show();
        } else {
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
                        ApiService.apiService.deleteUserById(idCurrentUser).enqueue(new Callback<ResponsePOST>() {
                            @Override
                            public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                                ResponsePOST responsePOST = response.body();
                                Toast.makeText(AdminUpdateUserActivity.this,""+responsePOST.getMess(),Toast.LENGTH_LONG).show();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<ResponsePOST> call, Throwable t) {
                                Toast.makeText(AdminUpdateUserActivity.this,"call API loi", Toast.LENGTH_LONG).show();
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
    }

    private void setCurrentUser(int idCurrentUser) {


        ApiService.apiService.getUserById(idCurrentUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                currentUser = response.body();
                edtEmail.setText(currentUser.getEmail());
                edtName.setText(currentUser.getName());
                edtAddress.setText(currentUser.getAddress());
                currentBitmap = ImgFromGrallery.deCodeToBase64(currentUser.getImg());
                imgAvatar.setImageBitmap(currentBitmap);
                if (currentUser.getVaitroId() == 0) {
                    radioAdmin.setChecked(true);
                } else if (currentUser.getVaitroId() == 1) {
                    radioUser.setChecked(true);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(AdminUpdateUserActivity.this, "call POST api loi!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initView() {

        btnChonAnh = findViewById(R.id.btn_them_anh);
        imgAvatar = findViewById(R.id.img_avatar);
        btnUpdate = findViewById(R.id.btn_update);
        edtEmail = findViewById(R.id.txt_email);
        edtName = findViewById(R.id.edt_name);
        edtAddress = findViewById(R.id.edt_address);
        btnDelete = findViewById(R.id.btn_delete);
        radioGroupRole = findViewById(R.id.radio_group_role);
        radioAdmin = findViewById(R.id.radio_admin);
        radioUser = findViewById(R.id.radio_user);
        txtTitle = findViewById(R.id.txt_title);
        groupLayoutRole = findViewById(R.id.group_layout_role);
    }

    private void clickPostApi() {
        currentUser.setName(edtName.getText().toString());
        currentUser.setAddress(edtAddress.getText().toString());
        currentUser.setImg(ImgFromGrallery.endCodeImg(currentBitmap));

        //check vai tro la admin thi lay vai tro theo radio
        int id = radioGroupRole.getCheckedRadioButtonId();
        if (id == R.id.radio_admin) {
            currentUser.setVaitroId(0);
        } else if (id == R.id.radio_user) {
            currentUser.setVaitroId(1);
        }

        Log.d("TAG", "clickPostApi: "+currentUser.toString());

        ApiService.apiService.updateUser(currentUser).enqueue(new Callback<ResponsePOST>() {
            @Override
            public void onResponse(Call<ResponsePOST> call, Response<ResponsePOST> response) {
                ResponsePOST responsePOST = response.body();
                if (responsePOST.getErrCode() == 0) {
                    Toast.makeText(AdminUpdateUserActivity.this, "Cập nhật thành công!", Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Toast.makeText(AdminUpdateUserActivity.this, "" + responsePOST.getMess(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePOST> call, Throwable t) {
                Toast.makeText(AdminUpdateUserActivity.this, "call POST api loi!", Toast.LENGTH_LONG).show();
            }
        });


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

    private void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Thí chủ hãy đợi chút...!");
        progressDialog.show();
    }

}
package com.nmt.minhtu.doan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.activity.admin.AdminHome;
import com.nmt.minhtu.doan.api.ApiService;
import com.nmt.minhtu.doan.api.ResponseLoginUser;
import com.nmt.minhtu.doan.data_local.DataLocalManager;
import com.nmt.minhtu.doan.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btnSignIn;
    TextView txtDangki;
    EditText edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        User isUser = DataLocalManager.getUser();
        if(isUser != null){
            if(isUser.getVaitroId() == 0){
                Intent intent = new Intent(LoginActivity.this, AdminHome.class);
                startActivity(intent);
            } else if(isUser.getVaitroId()== 1){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User user = new User();
                user.setEmail(edtEmail.getText().toString());
                user.setPassword(edtPassword.getText().toString());

                ApiService.apiService.postLoginUser(user).enqueue(new Callback<ResponseLoginUser>() {
                    @Override
                    public void onResponse(Call<ResponseLoginUser> call, Response<ResponseLoginUser> response) {
                        try {
                            ResponseLoginUser responseLoginUser = response.body();
                            if(responseLoginUser.getErrCode() == 0){
                                DataLocalManager.setUser(responseLoginUser.getUser());
                                if(responseLoginUser.getUser().getVaitroId() == 0){
                                    Intent intent = new Intent(LoginActivity.this, AdminHome.class);
                                    startActivity(intent);
                                } else if(responseLoginUser.getUser().getVaitroId() == 1){
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(LoginActivity.this,responseLoginUser.getMess(),Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            Log.d("TAG", "onResponse: "+e);
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseLoginUser> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,"call POST api loi!",Toast.LENGTH_LONG).show();
                    }
                });


            }
        });

        txtDangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });
    }

    public void init(){
        btnSignIn = findViewById(R.id.btn_sign_in);
        txtDangki = findViewById(R.id.txt_dangki);
        edtEmail = findViewById(R.id.edt_email_login);
        edtPassword = findViewById(R.id.edt_password_login);
    }
}
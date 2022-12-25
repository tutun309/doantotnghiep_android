package com.nmt.minhtu.doan.activity.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.activity.SignUpActivity;
import com.nmt.minhtu.doan.adapter.AdminListUserAdapter;
import com.nmt.minhtu.doan.api.ApiService;
import com.nmt.minhtu.doan.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageUserActivity extends AppCompatActivity {
    FloatingActionButton btnAdd;
    RecyclerView recyclerViewUser;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

        btnAdd = findViewById(R.id.btn_add);

        recyclerViewUser = findViewById(R.id.recycleview_list_user);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Thí chủ hãy đợi chút...!");
        progressDialog.show();

        setListUser();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageUserActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });



    }

    private void setListUser() {
        ApiService.apiService.getListUser().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> userList = response.body();
                AdminListUserAdapter adapter = new AdminListUserAdapter(ManageUserActivity.this, userList);
                recyclerViewUser.setAdapter(adapter);
                recyclerViewUser.setLayoutManager(
                                new LinearLayoutManager(getApplicationContext(),
                                RecyclerView.VERTICAL, false));
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(ManageUserActivity.this,"call API loi", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setListUser();
    }
}
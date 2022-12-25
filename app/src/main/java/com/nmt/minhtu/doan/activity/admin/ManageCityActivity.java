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
import com.nmt.minhtu.doan.adapter.AdminCategoryAdapter;
import com.nmt.minhtu.doan.api.ApiService;
import com.nmt.minhtu.doan.model.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageCityActivity extends AppCompatActivity {
    FloatingActionButton btnAdd;
    RecyclerView recyclerViewCategory;
    List<Category> categoryList =  new ArrayList<>();
    AdminCategoryAdapter adminCategoryAdapter;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_city);

        btnAdd = findViewById(R.id.btn_add);

        recyclerViewCategory = findViewById(R.id.recycleview_list_catogory);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Thí chủ hãy đợi chút...!");
        progressDialog.show();

        setListCategory();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageCityActivity.this, AdminAddCategory.class);
                startActivity(intent);
            }
        });



    }

    private void setListCategory(){
        ApiService.apiService.getListCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                progressDialog.dismiss();
                categoryList = response.body();
                adminCategoryAdapter = new AdminCategoryAdapter(ManageCityActivity.this,categoryList);
                recyclerViewCategory.setAdapter(adminCategoryAdapter);
                recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(ManageCityActivity.this,"call API loi", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListCategory();
    }
}
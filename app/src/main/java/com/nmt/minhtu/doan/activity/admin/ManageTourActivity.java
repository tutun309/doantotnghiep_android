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
import com.nmt.minhtu.doan.adapter.AdminListTourAdapter;
import com.nmt.minhtu.doan.api.ApiService;
import com.nmt.minhtu.doan.model.Tour;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageTourActivity extends AppCompatActivity {
    RecyclerView rcvListTour;
    FloatingActionButton btnAdd;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tour);

        initView();
        showProgress();
        setRcvListTour();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageTourActivity.this, AdminAddTour.class);
                startActivity(intent);
            }
        });
    }

    private void initView(){
        btnAdd = findViewById(R.id.btn_add);
        rcvListTour = findViewById(R.id.recycleview_list_tour);
    }

    private  void setRcvListTour(){
        ApiService.apiService.getAllTour().enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
                List<Tour> tourList = response.body();
                AdminListTourAdapter adapter = new AdminListTourAdapter(tourList,ManageTourActivity.this);
                rcvListTour.setAdapter(adapter);
                rcvListTour.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<List<Tour>> call, Throwable t) {
                Toast.makeText(ManageTourActivity.this,"Call API loi", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showProgress(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Thí chủ hãy đợi chút...!");
        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRcvListTour();
    }
}
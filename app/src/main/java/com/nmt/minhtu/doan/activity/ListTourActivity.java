package com.nmt.minhtu.doan.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.adapter.AdminListTourAdapter;
import com.nmt.minhtu.doan.api.ApiService;
import com.nmt.minhtu.doan.model.Tour;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTourActivity extends AppCompatActivity {
    RecyclerView rcvListTour;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tour);
        rcvListTour = findViewById(R.id.recycleview_list_tour);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Thí chủ hãy đợi chút...!");
        progressDialog.show();

        try {
            Intent intent = getIntent();
            int idCategory = intent.getIntExtra("categoryId", 1);
            ApiService.apiService.getTourByCategoryId(idCategory).enqueue(new Callback<List<Tour>>() {
                @Override
                public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
                    List<Tour> tourList = response.body();
                    Log.d("TAG", "onCreate: "+tourList.size());

                    AdminListTourAdapter adapter = new AdminListTourAdapter(tourList,ListTourActivity.this);
                    rcvListTour.setAdapter(adapter);
                    rcvListTour.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<Tour>> call, Throwable t) {
                    Toast.makeText(ListTourActivity.this,"Call API lỗi", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
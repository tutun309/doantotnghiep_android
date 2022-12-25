package com.nmt.minhtu.doan.activity.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.adapter.AdminListBookingAdapter;
import com.nmt.minhtu.doan.api.ApiService;
import com.nmt.minhtu.doan.model.Booking;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageBookingActivity extends AppCompatActivity {
    RecyclerView rcvListBooking;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_history);

        initView();
        showProgress();
        setListBooking();
    }

    private void setListBooking() {
        ApiService.apiService.getAllBooking().enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                List<Booking> bookingList = response.body();
                AdminListBookingAdapter adapter = new AdminListBookingAdapter(bookingList, getApplicationContext());
                rcvListBooking.setAdapter(adapter);
                rcvListBooking.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Call API loi", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView(){
        rcvListBooking = findViewById(R.id.recycleview_list_booking);
    }

    private void showProgress(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Thí chủ hãy đợi chút...!");
        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListBooking();
    }
}
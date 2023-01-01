package com.nmt.minhtu.doan.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.adapter.AdminListBookingAdapter;
import com.nmt.minhtu.doan.api.ApiService;
import com.nmt.minhtu.doan.data_local.DataLocalManager;
import com.nmt.minhtu.doan.model.Booking;
import com.nmt.minhtu.doan.model.User;
import com.nmt.minhtu.doan.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentHistory extends Fragment {
    RecyclerView rcvListBooking;
    ProgressDialog progressDialog;
    List<Booking> bookedTourList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    public FragmentHistory() {
        setListBooking();
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        rcvListBooking = view.findViewById(R.id.recycleview_booked_tour);
        swipeRefreshLayout = view.findViewById(R.id.swipe_reload);
        showProgress();
        setListBooking();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setListBooking();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void setListBooking(){
        if (Utils.INSTANCE.isLogin()) {
            User user = DataLocalManager.getUser();
            ApiService.apiService.getBookingByUserId(user.getId()).enqueue(new Callback<List<Booking>>() {
                @Override
                public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                    if(response.isSuccessful()){
                        bookedTourList = response.body();
                        AdminListBookingAdapter adapter = new AdminListBookingAdapter(bookedTourList, getContext());
                        rcvListBooking.setAdapter(adapter);
                        rcvListBooking.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<List<Booking>> call, Throwable t) {

                }
            });
        }
    }

    private void showProgress(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Xin hãy đợi giây lát...!");
        progressDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        setListBooking();
    }
}
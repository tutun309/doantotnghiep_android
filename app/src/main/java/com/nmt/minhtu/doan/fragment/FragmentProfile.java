package com.nmt.minhtu.doan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nmt.minhtu.doan.ImgFromGrallery;
import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.activity.LoginActivity;
import com.nmt.minhtu.doan.activity.MainActivity;
import com.nmt.minhtu.doan.activity.admin.AdminUpdateUserActivity;
import com.nmt.minhtu.doan.api.ApiService;
import com.nmt.minhtu.doan.data_local.DataLocalManager;
import com.nmt.minhtu.doan.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentProfile extends Fragment {
    Button btnSignOut;
    ImageView imgAvt;
    TextView userName, txtHistory,txtManageAccount;
    MainActivity mainActivity;
    User user = DataLocalManager.getUser();

    public FragmentProfile() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgAvt = view.findViewById(R.id.img_avatar_profile);
        userName = view.findViewById(R.id.txtName);
        txtHistory = view.findViewById(R.id.txt_manage_tour);
        txtManageAccount = view.findViewById(R.id.txt_manage_account);

        btnSignOut = view.findViewById(R.id.btn_profile_logout);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataLocalManager.setUser(null);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });

        imgAvt.setImageBitmap(ImgFromGrallery.deCodeToBase64(user.getImg()));
        userName.setText("Xin chào "+user.getName());
        txtHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).getViewPager2().setCurrentItem(1);
            }
        });

        txtManageAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AdminUpdateUserActivity.class);
                startActivity(intent);


            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ApiService.apiService.getUserById(user.getId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                DataLocalManager.setUser(user);
                imgAvt.setImageBitmap(ImgFromGrallery.deCodeToBase64(user.getImg()));
                userName.setText("Xin chào "+user.getName());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
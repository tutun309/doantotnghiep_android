package com.nmt.minhtu.doan.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.nmt.minhtu.doan.utils.Utils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentProfile extends Fragment {
    Button btnSignOut;
    ImageView imgAvt;
    TextView userName, txtHistory,txtManageAccount;
    MainActivity mainActivity;
    User user;
    Boolean isLogin;

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

        initData();
        setupView();
        initListener();

    }

    private void initListener() {
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if(Utils.INSTANCE.isLogin()) {
                    imgAvt.setImageResource(R.drawable.profile);
                    userName.setText("Chưa xác định");
                    btnSignOut.setText("Đăng nhập");
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                DataLocalManager.setUser(null);
            }
        });

        txtHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.INSTANCE.isLogin()) {
                    ((MainActivity) requireActivity()).getViewPager2().setCurrentItem(1);
                } else {
                    Toast.makeText(requireContext(), "Bạn cần đăng nhập để thực hiện chức năng này!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtManageAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.INSTANCE.isLogin()) {
                    Intent intent = new Intent(getActivity(), AdminUpdateUserActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(requireContext(), "Bạn cần đăng nhập để thực hiện chức năng này!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setupView() {
        if(user != null) {
            imgAvt.setImageBitmap(ImgFromGrallery.deCodeToBase64(user.getImg()));
            userName.setText("Xin chào "+user.getName());
            btnSignOut.setText("Đăng xuất tài khoản");
        } else {
            userName.setText("Chưa xác định");
            btnSignOut.setText("Đăng nhập");
            imgAvt.setImageResource(R.drawable.profile);
        }
    }

    private void initData() {
        user = DataLocalManager.getUser();
        isLogin = Utils.INSTANCE.isLogin();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if(DataLocalManager.getUser() != null) {
            ApiService.apiService.getUserById(user.getId()).enqueue(new Callback<User>() {
                @SuppressLint("SetTextI18n")
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
        }*/
    }
}
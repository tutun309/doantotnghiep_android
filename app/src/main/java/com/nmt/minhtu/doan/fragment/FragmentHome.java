package com.nmt.minhtu.doan.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.nmt.minhtu.doan.ImgFromGrallery;
import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.adapter.AdminCategoryAdapter;
import com.nmt.minhtu.doan.adapter.AdminListTourAdapter;
import com.nmt.minhtu.doan.adapter.SliderHomeFragmentAdapter;
import com.nmt.minhtu.doan.adapter.UserCategoryAdapter;
import com.nmt.minhtu.doan.api.ApiService;
import com.nmt.minhtu.doan.data_local.DataLocalManager;
import com.nmt.minhtu.doan.model.Category;
import com.nmt.minhtu.doan.model.Tour;
import com.nmt.minhtu.doan.model.User;
import com.nmt.minhtu.doan.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentHome extends Fragment {

    ViewPager2 viewPagerSlider;
    SliderHomeFragmentAdapter sliderHomeFragmentAdapter;
    TextView seeAll, txtName;
    ImageView imgAvatar;
    EditText editTextSearch;
    RecyclerView recyclerViewCategory;
    List<Category> categoryList;
    List<Tour> tourList;
    ProgressDialog progressDialog;
    UserCategoryAdapter userCategoryAdapter;
    RecyclerView rcvListTour;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (viewPagerSlider.getCurrentItem() == categoryList.size() - 1) {
                viewPagerSlider.setCurrentItem(0);
            } else {
                viewPagerSlider.setCurrentItem(viewPagerSlider.getCurrentItem() + 1);
            }
        }
    };

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //-----------ánh xạ view-------------
        recyclerViewCategory = view.findViewById(R.id.recycleview_danhmucdiadiem);
        viewPagerSlider = view.findViewById(R.id.viewpager_diadiemnoibat);
        txtName = view.findViewById(R.id.txtName);
        imgAvatar = view.findViewById(R.id.imageAvatar);
        rcvListTour = view.findViewById(R.id.recycleview_list_tour_home);
        editTextSearch = view.findViewById(R.id.editTextSearch);

        setupView();
        showProgress();
        setListCategory();
        setRcvListTourHome();


        return view;
    }

    private void setupView() {
        if(Utils.INSTANCE.isLogin()) {
            //------------set thong tin ca nhan---------
            User user = DataLocalManager.getUser();
            txtName.setText("Xin chào " +user.getName()+" !");
            imgAvatar.setImageBitmap(ImgFromGrallery.deCodeToBase64(user.getImg()));
        } else {
            txtName.setText("Chúc ngày mới vui vẻ!");
            imgAvatar.setImageResource(R.drawable.profile);
        }
    }

    private void setListCategory() {
        ApiService.apiService.getListCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                categoryList = response.body();

                userCategoryAdapter = new UserCategoryAdapter(getContext(),categoryList);
                recyclerViewCategory.setAdapter(userCategoryAdapter);
                recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

                AdminCategoryAdapter adapter = new AdminCategoryAdapter(getContext(), categoryList);
                viewPagerSlider.setAdapter(adapter);

                viewPagerSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        handler.removeCallbacks(runnable);
                        handler.postDelayed(runnable, 2500);
                    }
                });

            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    private  void setRcvListTourHome(){
        ApiService.apiService.getAllTour().enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
                tourList = response.body();

                AdminListTourAdapter adapter = new AdminListTourAdapter(tourList, getContext());

                rcvListTour.setAdapter(adapter);
                rcvListTour.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Tour>> call, Throwable t) {
                Toast.makeText(getContext(),"Call API loi", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void showProgress(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Xin hãy đợi giây lát...!");
        progressDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupView();
    }
}
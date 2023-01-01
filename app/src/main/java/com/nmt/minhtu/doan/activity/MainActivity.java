package com.nmt.minhtu.doan.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.adapter.MainViewpagerAdapter;
import com.nmt.minhtu.doan.data_local.DataLocalManager;

public class MainActivity extends AppCompatActivity {
    public ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.main_viewpager);
        bottomNavigationView = findViewById(R.id.bottom_nav);


        MainViewpagerAdapter mainViewpagerAdapter = new MainViewpagerAdapter(this);
        viewPager2.setAdapter(mainViewpagerAdapter);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.menu_trangchu:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.menu_lichsu:
                        if(DataLocalManager.getUser() == null) {
                            Toast.makeText(MainActivity.this, "Bạn cần đăng nhập để thực hiện chức năng này!", Toast.LENGTH_SHORT).show();
                        } else {
                            viewPager2.setCurrentItem(1);
                        }
                        break;
                    case R.id.menu_thongbao:
                        if(DataLocalManager.getUser() == null) {
                            Toast.makeText(MainActivity.this, "Bạn cần đăng nhập để thực hiện chức năng này!", Toast.LENGTH_SHORT).show();
                        } else {
                            viewPager2.setCurrentItem(2);
                        }
                        break;
                    case R.id.menu_profile:
                        viewPager2.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_trangchu).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_lichsu).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menu_thongbao).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menu_profile).setChecked(true);
                        break;
                }
            }
        });
        viewPager2.setUserInputEnabled(false);
    }

    public ViewPager2 getViewPager2() {
        return viewPager2;
    }

    public void setViewPager2(ViewPager2 viewPager2) {
        this.viewPager2 = viewPager2;
    }
}
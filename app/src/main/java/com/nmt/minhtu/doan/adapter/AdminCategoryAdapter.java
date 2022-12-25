package com.nmt.minhtu.doan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nmt.minhtu.doan.ImgFromGrallery;
import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.activity.admin.AdminUpdateDeleteCategory;
import com.nmt.minhtu.doan.data_local.DataLocalManager;
import com.nmt.minhtu.doan.model.Category;

import java.util.List;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.CategoryViewHolder> {
    Context context;
    List<Category> categories;

    public AdminCategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int vaitro = DataLocalManager.getUser().getVaitroId();
        View view = null;
        if(vaitro == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_custom_item_category, parent, false);
        } else if(vaitro == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_slider_home, parent, false);
        }
        return new CategoryViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.img.setImageBitmap(ImgFromGrallery.deCodeToBase64(category.getImg()));
        holder.ten.setText(category.getName());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idvaitro = DataLocalManager.getUser().getVaitroId();
                if(idvaitro == 0) {
                    Intent intent = new Intent(context, AdminUpdateDeleteCategory.class);
                    intent.putExtra("currentCategoryId", category.getId());
                    context.startActivity(intent);
                }
//                else if(idvaitro == 1){
//                    Intent intent = new Intent(context, ListTourActivity.class);
//                    intent.putExtra("currentCategoryId", category.getId());
//                    context.startActivity(intent);
//                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView ten;
        //    TextView danhGia;
        CardView item;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_slider);
            ten = itemView.findViewById(R.id.textViewName_slider);
            //danhGia = itemView.findViewById(R.id.txt_danhgia_slider);
            item = itemView.findViewById(R.id.itemView);
        }
    }

}

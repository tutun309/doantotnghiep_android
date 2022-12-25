package com.nmt.minhtu.doan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nmt.minhtu.doan.ImgFromGrallery;
import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.activity.ListTourActivity;
import com.nmt.minhtu.doan.model.Category;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserCategoryAdapter extends RecyclerView.Adapter<UserCategoryAdapter.UserCategoryViewHolder> {
    Context context;
    List<Category> categories;

    public UserCategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public UserCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_list_danhmuc, parent, false);
        return new UserCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.img.setImageBitmap(ImgFromGrallery.deCodeToBase64(category.getImg()));
        holder.ten.setText(category.getName());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(context, ListTourActivity.class);
                    intent.putExtra("categoryId", category.getId());
                    context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class UserCategoryViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView ten;
        //    TextView danhGia;
        RelativeLayout item;
        public UserCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.txt_place_image);
            ten = itemView.findViewById(R.id.txt_place_name);
            item = itemView.findViewById(R.id.item);
        }
    }

}

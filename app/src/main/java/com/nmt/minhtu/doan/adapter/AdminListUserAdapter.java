package com.nmt.minhtu.doan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.nmt.minhtu.doan.ImgFromGrallery;
import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.activity.admin.AdminUpdateUserActivity;
import com.nmt.minhtu.doan.model.User;

import java.util.List;

public class AdminListUserAdapter extends RecyclerView.Adapter<AdminListUserAdapter.UserViewHolder> {
    Context context;
    List<User> userList;

    public AdminListUserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(com.nmt.minhtu.doan.R.layout.admin_custom_list_user, parent, false);

        return new UserViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.img.setImageBitmap(ImgFromGrallery.deCodeToBase64(user.getImg()));
        holder.txtTen.setText(user.getName());
        holder.txtAddress.setText(user.getAddress());
        if (user.getVaitroId() == 0) {
            holder.txtRole.setText("Vai trò: " + "admin");
        } else if (user.getVaitroId() == 1) {
            holder.txtRole.setText("Vai trò: " + "User");
        }
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminUpdateUserActivity.class);
                intent.putExtra("currentUserId", user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView txtTen, txtAddress, txtRole;
        RelativeLayout item;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_avt);
            txtTen = itemView.findViewById(R.id.txt_name);
            txtAddress = itemView.findViewById(R.id.txt_name_address);
            txtRole = itemView.findViewById(R.id.txt_role);
            item = itemView.findViewById(R.id.item_user);
        }
    }
}


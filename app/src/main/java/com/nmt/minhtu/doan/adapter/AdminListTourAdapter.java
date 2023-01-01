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
import com.nmt.minhtu.doan.activity.admin.tour.DetailTourActivity;
import com.nmt.minhtu.doan.activity.admin.AdminUpdateDeleteTour;
import com.nmt.minhtu.doan.data_local.DataLocalManager;
import com.nmt.minhtu.doan.model.Tour;
import com.nmt.minhtu.doan.utils.Utils;

import java.util.List;

public class AdminListTourAdapter  extends RecyclerView.Adapter<AdminListTourAdapter.TourViewHolder> {
    private List<Tour> tourList;
    private Context context;

    public AdminListTourAdapter(List<Tour> tourList, Context context) {
        this.tourList = tourList;
        this.context = context;
        notifyDataSetChanged();
    }

    public void setTourList(List<Tour> tourList){
        this.tourList = tourList;
    }


    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_custom_item_list_tour, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        Tour tour = tourList.get(position);
        holder.imgAvt.setImageBitmap(ImgFromGrallery.deCodeToBase64(tour.getImg()));
        holder.txtName.setText(tour.getName());
        holder.txtCategory.setText("Từ: "+tour.getCategory().getName());
        holder.txtPrice.setText("Giá: " + Utils.INSTANCE.formatMoney(tour.getPrice()) + " VND");
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer userId = DataLocalManager.getUser() == null ? 1: DataLocalManager.getUser().getVaitroId();
                if(userId == 0){
                    Intent intent = new Intent(context, AdminUpdateDeleteTour.class);
                    intent.putExtra("currentTourId", tour.getId());
                    context.startActivity(intent);
                } else if (userId == 1){
                    Intent intent = new Intent(context, DetailTourActivity.class);
                    intent.putExtra("currentTourId", tour.getId());
                    context.startActivity(intent);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }

    class TourViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvt;
        TextView txtName, txtCategory, txtPrice;
        RelativeLayout item;

        public TourViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvt = itemView.findViewById(R.id.img_avt);
            txtName = itemView.findViewById(R.id.txt_name);
            txtCategory = itemView.findViewById(R.id.txt_name_category);
            txtPrice = itemView.findViewById(R.id.txt_price_tour);
            item = itemView.findViewById(R.id.item_tour);
        }
    }
}

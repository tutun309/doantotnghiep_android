package com.nmt.minhtu.doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.model.SliderHomeFragment;

import java.util.List;

public class SliderHomeFragmentAdapter extends RecyclerView.Adapter<SliderHomeFragmentAdapter.SliderViewHolder> {
    //    Context context;
    List<SliderHomeFragment> sliderHomeFragments;

    public SliderHomeFragmentAdapter(List<SliderHomeFragment> sliderHomeFragments) {
        this.sliderHomeFragments = sliderHomeFragments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_slider_home, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        SliderHomeFragment slider = sliderHomeFragments.get(position);
        holder.img.setImageResource(slider.getImg());
        holder.ten.setText(slider.getTen());
        holder.danhGia.setText("Đánh giá: " + slider.getDanhGia().toString() + " ");
    }

    @Override
    public int getItemCount() {
        return sliderHomeFragments.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView ten, danhGia;
        CardView item;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_slider);
            ten = itemView.findViewById(R.id.textViewName_slider);
            danhGia = itemView.findViewById(R.id.txt_danhgia_slider);
            item = itemView.findViewById(R.id.itemView);
        }
    }

}

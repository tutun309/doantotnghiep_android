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
import com.nmt.minhtu.doan.activity.booking.DetailBookingActivity;
import com.nmt.minhtu.doan.activity.booking.DetailBookingActivityV2;
import com.nmt.minhtu.doan.model.Booking;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdminListBookingAdapter extends RecyclerView.Adapter<AdminListBookingAdapter.ListBookingViewHolder> {
    private List<Booking> bookingList;
    private Context context;

    public AdminListBookingAdapter(List<Booking> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }


    @NonNull
    @Override
    public ListBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_custom_item_list_booking, parent, false);
        return new ListBookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListBookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.imgAvt.setImageBitmap(ImgFromGrallery.deCodeToBase64(booking.getTour().getImg()));
        holder.txtNameTour.setText(booking.getTour().getName());
        holder.txUserName.setText("Khách: "+booking.getUser().getName());
        holder.txtStatus.setText(booking.getBookingStatus());
        holder.txtDateBooking.setText(booking.getTimeBooking() == null ? "" : booking.getTimeBooking());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(context.getApplicationContext(), DetailBookingActivityV2.class);
                    intent.putExtra("currentBookingId", booking.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }


//                intent.putExtra("booking", tour.getId());
//                Integer userId = DataLocalManager.getUser().getVaitroId();
//                if(userId == 0){
//                    Intent intent = new Intent(context, AdminUpdateDeleteTour.class);
//                    intent.putExtra("currentTourId", tour.getId());
//                    context.startActivity(intent);
//                } else if (userId == 1){
//                    Intent intent = new Intent(context, DetailTourActivity.class);
//                    intent.putExtra("currentTourId", tour.getId());
//                    context.startActivity(intent);
//                }
//
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    class ListBookingViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvt;
        TextView txtNameTour, txUserName, txtDateBooking,txtStatus;
        RelativeLayout item;

        public ListBookingViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvt = itemView.findViewById(R.id.img_avt);
            txtNameTour = itemView.findViewById(R.id.txt_name);
            txUserName = itemView.findViewById(R.id.txt_use_name);
            txtDateBooking = itemView.findViewById(R.id.txt_date);
            txtStatus = itemView.findViewById(R.id.txt_status);

            item = itemView.findViewById(R.id.item_tour);
        }
    }
}

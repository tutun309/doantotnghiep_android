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
import com.nmt.minhtu.doan.model.BookedTour;

import java.util.List;

public class BookedTourAdaper extends RecyclerView.Adapter<BookedTourAdaper.BookedTourViewHolder> {
    //    Context context;
    List<BookedTour> bookedTourList;

    public BookedTourAdaper(List<BookedTour> bookedTourList) {
        this.bookedTourList = bookedTourList;
    }

    @NonNull
    @Override
    public BookedTourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_booked_tour, parent, false);
        return new BookedTourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookedTourViewHolder holder, int position) {
        BookedTour bookedTour = bookedTourList.get(position);
        holder.img.setImageResource(bookedTour.getImg());
        holder.name.setText(bookedTour.getName());
        holder.price.setText("Giá: "+ String.valueOf(bookedTour.getPrice())+"$");
        holder.status.setText( bookedTour.getStatus() );
//        holder.status.setText("Trạng thái: " + bookedTour.getStatus().toString() );
    }

    @Override
    public int getItemCount() {
        return bookedTourList.size();
    }

    class BookedTourViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name,price, status;
        CardView item;

        public BookedTourViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_booked_tour);
            name = itemView.findViewById(R.id.txt_nameBookedTour);
            price = itemView.findViewById(R.id.txt_priceBookedTour);
            status = itemView.findViewById(R.id.txt_statusBookedTour);
            item = itemView.findViewById(R.id.itemView);
        }
    }

}

package com.nmt.minhtu.doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nmt.minhtu.doan.R;
import com.nmt.minhtu.doan.model.Category;

import java.util.List;

public class AdminAdapterSpinnerCategory extends BaseAdapter {
    List<Category> categories;

    public AdminAdapterSpinnerCategory(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categories.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            itemView = inflater.inflate(R.layout.admin_custom_item_spinner_category, parent,false);
        } else itemView = convertView;
        Category category  = categories.get(position);
        TextView name = itemView.findViewById(R.id.txt_name);
        name.setText(category.getName());

        return itemView;
    }
}


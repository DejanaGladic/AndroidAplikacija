package com.example.user.mit1pokusaj.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.mit1pokusaj.R;
import com.example.user.mit1pokusaj.models.Category;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<Category> {

    public CategoryAdapter(Context context, ArrayList<Category> categoryList) {
        super(context, 0, categoryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.category_spinner_row, parent, false
            );
        }

        ImageView imageView = convertView.findViewById(R.id.image_product);
        TextView textViewName = convertView.findViewById(R.id.text_view_name);

        Category currentItem = getItem(position);

        if (currentItem != null) {
            imageView.setImageResource(currentItem.getCatImage());
            textViewName.setText(currentItem.getName());
        }

        return convertView;
    }
}

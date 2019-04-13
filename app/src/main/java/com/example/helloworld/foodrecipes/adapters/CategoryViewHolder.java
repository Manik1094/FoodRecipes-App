package com.example.helloworld.foodrecipes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.helloworld.foodrecipes.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    OnRecipeListener listener;
    CircleImageView categoryImage;
    TextView categoryTitle;

    public CategoryViewHolder(@NonNull View itemView , OnRecipeListener listener) {
        super(itemView);

        this.listener = listener;
        categoryImage = itemView.findViewById(R.id.category_image);
        categoryTitle = itemView.findViewById(R.id.category_title);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      listener.onCategoryClick(categoryTitle.getText().toString());
    }
}

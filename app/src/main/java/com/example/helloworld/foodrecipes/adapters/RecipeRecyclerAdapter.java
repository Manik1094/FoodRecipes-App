package com.example.helloworld.foodrecipes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.helloworld.foodrecipes.R;
import com.example.helloworld.foodrecipes.models.Recipe;

import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Recipe> mRecipes;
    OnRecipeListener mOnRecipeListener;
    public static final int RECIPE_TYPE = 1;
    public static final int LOADING_TYPE = 2;

    public RecipeRecyclerAdapter(OnRecipeListener mOnRecipeListener) {

        this.mOnRecipeListener = mOnRecipeListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = null;
        switch (position){
            case RECIPE_TYPE : {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recipe_list_item , viewGroup, false);
                return new RecipeViewHolder(view , mOnRecipeListener);
            }

            case LOADING_TYPE : {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_list_item , viewGroup, false);
                return new LoadingViewHolder(view);
            }
            
            default: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recipe_list_item , viewGroup, false);
                return new RecipeViewHolder(view , mOnRecipeListener);
            }
        }



    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(viewHolder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(mRecipes.get(position).getImage_url())
                .into(((RecipeViewHolder) viewHolder).image);


        ((RecipeViewHolder) viewHolder).title.setText(mRecipes.get(position).getTitle());
        ((RecipeViewHolder) viewHolder).publisher.setText(mRecipes.get(position).getPublisher());
        ((RecipeViewHolder) viewHolder).socialScore.setText(String.valueOf(Math.round(mRecipes.get(position).getSocial_rank())));

    }

    @Override
    public int getItemCount() {
        if(mRecipes !=null){
            return mRecipes.size();
        }
        return 0;
    }

    public void setRecipes(List<Recipe> recipes){
        mRecipes = recipes;
        notifyDataSetChanged();
    }

}

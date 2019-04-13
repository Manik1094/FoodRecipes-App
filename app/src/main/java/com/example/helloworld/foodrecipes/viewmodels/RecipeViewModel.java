package com.example.helloworld.foodrecipes.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.helloworld.foodrecipes.adapters.RecipeViewHolder;
import com.example.helloworld.foodrecipes.models.Recipe;
import com.example.helloworld.foodrecipes.repositories.RecipeRepository;
import com.example.helloworld.foodrecipes.requests.responses.RecipeResponse;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private String mRecipeId;
    private boolean mDidRetreiveRecipe;

    public RecipeViewModel(){
        mDidRetreiveRecipe = false;
        mRecipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<Recipe> getRecipe(){
        return mRecipeRepository.getRecipe();
    }

    public LiveData<Boolean> isRecipeRequestTimedOut() {
        return  mRecipeRepository.isRecipeRequestTimedOut();
    }


    public String getRecipeId(){
        return mRecipeId;
    }

    public void searchRecipeById(String recipeId){
        mRecipeId = recipeId;
        mRecipeRepository.searchRecipeById(recipeId);
    }

    public void setRetreivedRecipe(boolean retreivedRecipe){
        mDidRetreiveRecipe = retreivedRecipe;
    }

    public boolean didRetreiveRecipe(){
        return mDidRetreiveRecipe;
    }


}

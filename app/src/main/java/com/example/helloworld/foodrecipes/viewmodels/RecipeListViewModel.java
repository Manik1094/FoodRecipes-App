package com.example.helloworld.foodrecipes.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.helloworld.foodrecipes.models.Recipe;
import com.example.helloworld.foodrecipes.repositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {
    private static final String TAG = "RecipeListViewModel";

    private RecipeRepository recipeRepository;

    public RecipeListViewModel(){

        recipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return recipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query , int pageNumber){
        Log.e(TAG, "searchRecipesApi: Inside searchRecipesApi method");
        recipeRepository.searchRecipesApi(query , pageNumber);
    }

}

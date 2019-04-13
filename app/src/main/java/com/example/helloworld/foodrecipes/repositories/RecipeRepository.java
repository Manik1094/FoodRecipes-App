package com.example.helloworld.foodrecipes.repositories;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.helloworld.foodrecipes.models.Recipe;
import com.example.helloworld.foodrecipes.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeApiClient recipeApiClient;
    private static final String TAG = "RecipeRepository";


    public static RecipeRepository getInstance(){
        if(instance==null){
            instance = new RecipeRepository();
        }
        return instance;
    }

    public RecipeRepository(){
        recipeApiClient = RecipeApiClient.getInstance();

    }

    public LiveData<List<Recipe>> getRecipes(){
        return recipeApiClient.getRecipes();
    }

    public void searchRecipesApi(String query , int pageNumber){
        if(pageNumber == 0){
            pageNumber = 1;
        }
        Log.e(TAG, "searchRecipesApi: Inside searchRecipesApi method");
        recipeApiClient.searchRecipesApi(query , pageNumber);
    }

}

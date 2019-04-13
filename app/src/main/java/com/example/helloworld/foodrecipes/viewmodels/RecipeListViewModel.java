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
    private boolean mIsViewingRecipes;
    private boolean mIsPerformingQuery;

    public RecipeListViewModel(){
         mIsViewingRecipes = false;
         mIsPerformingQuery = false;
        recipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return recipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query , int pageNumber){
        mIsViewingRecipes = true;
        mIsPerformingQuery = true;
        Log.e(TAG, "searchRecipesApi: Inside searchRecipesApi method");
        recipeRepository.searchRecipesApi(query , pageNumber);
    }

    public void searchNextpage(){
        if(!isPerformingQuery() && isViewingRecipes()){
            recipeRepository.searchNextPage();
        }
    }

    public boolean isViewingRecipes(){
        return mIsViewingRecipes;
    }

    public void setIsViewingRecipes(boolean isViewingRecipes){
        mIsViewingRecipes = isViewingRecipes;
    }

    public void setIsPerformingQuery(boolean isPerformingQuery){
        this.mIsPerformingQuery = isPerformingQuery;
    }

    public boolean isPerformingQuery(){
        return mIsPerformingQuery;
    }

    public boolean onBackPressed(){

        if(mIsPerformingQuery){
            //cancel the request
            recipeRepository.cancelRequest();
            mIsPerformingQuery = false;
        }
         if(mIsViewingRecipes){
            mIsViewingRecipes = false;
            return false;
        }
        return true;
    }

}

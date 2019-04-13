package com.example.helloworld.foodrecipes.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.helloworld.foodrecipes.models.Recipe;
import com.example.helloworld.foodrecipes.requests.RecipeApiClient;

import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance;
    private RecipeApiClient recipeApiClient;
    private static final String TAG = "RecipeRepository";
    private String mQuery;
    private int mPageNumber;
    private MutableLiveData<Boolean> mIsQueryExhausted = new MutableLiveData<>();
    private MediatorLiveData<List<Recipe>> mRecipes = new MediatorLiveData<>();


    public static RecipeRepository getInstance(){
        if(instance==null){
            instance = new RecipeRepository();
        }
        return instance;
    }

    public RecipeRepository(){
        recipeApiClient = RecipeApiClient.getInstance();
        initMediators();
    }

    private void initMediators(){
        final LiveData<List<Recipe>> recipeListApiSource = recipeApiClient.getRecipes();
        mRecipes.addSource(recipeListApiSource, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if(recipes!=null){
                    mRecipes.setValue(recipes);
                    doneQuery(recipes);
                }else{
                    //search database cache
                    doneQuery(null);
                }
            }
        });
    }

    private void doneQuery(List<Recipe> list){
        if(list!=null){
            if(list.size()% 30 !=0){
                mIsQueryExhausted.setValue(true);
            }
        }else{
            mIsQueryExhausted.setValue(true);
        }
    }

    public LiveData<Boolean> isQueryExhausted(){
        return mIsQueryExhausted;
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }

    public LiveData<Recipe> getRecipe(){
        return recipeApiClient.getRecipe();
    }

    public LiveData<Boolean> isRecipeRequestTimedOut() {
       return  recipeApiClient.isRecipeRequestTimedOut();
    }

    public void searchRecipeById(String recipeId){
        recipeApiClient.searchRecipeById(recipeId);
    }

    public void searchRecipesApi(String query , int pageNumber){
        if(pageNumber == 0){
            pageNumber = 1;
        }
        mQuery = query;
        mPageNumber = pageNumber;
        mIsQueryExhausted.setValue(false);
        Log.e(TAG, "searchRecipesApi: Inside searchRecipesApi method");
        recipeApiClient.searchRecipesApi(query , pageNumber);
    }

    public void searchNextPage(){
        searchRecipesApi(mQuery , mPageNumber + 1);
    }

    public void cancelRequest(){
      recipeApiClient.cancelRequest();
    }


}

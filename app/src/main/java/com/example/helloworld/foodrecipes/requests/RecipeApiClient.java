package com.example.helloworld.foodrecipes.requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import com.example.helloworld.foodrecipes.AppExecutors;
import com.example.helloworld.foodrecipes.models.Recipe;
import com.example.helloworld.foodrecipes.requests.responses.RecipeSearchResponse;
import com.example.helloworld.foodrecipes.util.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import retrofit2.Call;
import retrofit2.Response;

import static com.example.helloworld.foodrecipes.util.Constants.NETWORK_TIMEOUT;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";

    public static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    private RetreiveRecipesRunnable mRetrieveRecipesRunnable;


    public static RecipeApiClient getInstance(){
        if(instance == null){
            instance = new RecipeApiClient();
        }
        return instance;
    }

    public RecipeApiClient(){
        mRecipes = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }

    public void searchRecipesApi(String query , int pageNumber){
        Log.e(TAG, "searchRecipesApi: Inside searchRecipesApi"+query + pageNumber );
        if(mRetrieveRecipesRunnable!=null){
            mRetrieveRecipesRunnable = null;
        }
        mRetrieveRecipesRunnable = new RetreiveRecipesRunnable(query , pageNumber);
      final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveRecipesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {

               // this will run after 3 seconds
               // Let the user know its timed out
               // Basically after 3 seconds the request we are making above will be cancelled
                handler.cancel(true);
                Log.e(TAG, "run: Request timeout" );
            }
        } , NETWORK_TIMEOUT , TimeUnit.MILLISECONDS);
    }

    private class RetreiveRecipesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetreiveRecipesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            Log.e(TAG, "run: Inside run method of RetreiveRecipesRunnable" );

            try {
                Response response = getRecipes(query , pageNumber).execute();
                Log.e(TAG, "run: Response code is"+response.code() );
                if(cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    Log.e(TAG, "run: Response"+response.body() );
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse)response.body()).getRecipes());
                    Log.e(TAG, "run: SIze of list received"+list.size());
                    if(pageNumber == 1){
                        mRecipes.postValue(list);
                    }else{
                        List<Recipe> currentRecipes = mRecipes.getValue();
                        currentRecipes.addAll(list);
                        mRecipes.postValue(currentRecipes);
                    }
                }else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: "+error );
                    mRecipes.postValue(null);
                }
            } catch (Exception e) {
                Log.e(TAG, "run: Error"+e.toString() );
                e.printStackTrace();
                mRecipes.postValue(null);
            }

        }

        private Call<RecipeSearchResponse> getRecipes(String query , int pageNumber){
            return ServiceGenerator.getRecipeApi().searchRecipe(
                    Constants.API_KEY,
                    query,
                    String.valueOf(pageNumber)
            );
        }

        private void cancelRequest(){

            Log.d(TAG, "cancelRequest: Canceling the search request");
            cancelRequest = true;
        }
    }


}

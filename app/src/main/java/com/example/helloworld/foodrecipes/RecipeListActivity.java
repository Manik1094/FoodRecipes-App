package com.example.helloworld.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.helloworld.foodrecipes.adapters.OnRecipeListener;
import com.example.helloworld.foodrecipes.adapters.RecipeRecyclerAdapter;
import com.example.helloworld.foodrecipes.models.Recipe;
import com.example.helloworld.foodrecipes.util.VerticalSpacingItemDecorator;
import com.example.helloworld.foodrecipes.viewmodels.RecipeListViewModel;

import java.util.List;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    private RecipeListViewModel recipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;
    public static final String TAG = "RecipeListActivity";
    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Log.e(TAG, "onCreate: Inside RecipeListActivity" );

        mRecyclerView = findViewById(R.id.recipe_list);
        mSearchView = findViewById(R.id.search_view);

        recipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        initRecyclerView();


        subscribeObservers();
        initSearchView();

        if(!recipeListViewModel.isViewingRecipes()){
           //display search categories
            displaySearchCategories();
        }

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    public void subscribeObservers(){
        recipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if(recipes!=null){
                    if(recipeListViewModel.isViewingRecipes()){
                        Log.e(TAG, "onChanged: List size"+recipes.size());
                        recipeListViewModel.setIsPerformingQuery(false);
                        mAdapter.setRecipes(recipes);
                    }

                }

            }
        });

        recipeListViewModel.isQueryExhausted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean){
                    //query is exhausted
                    mAdapter.setQueryExhausted();
                }
            }
        });
    }

    private void initRecyclerView(){
      mAdapter = new RecipeRecyclerAdapter( this);
      VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
      mRecyclerView.addItemDecoration(itemDecorator);
      mRecyclerView.setAdapter(mAdapter);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

      mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
          @Override
          public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
              if(!mRecyclerView.canScrollVertically(1)){
                  //search next page
                  recipeListViewModel.searchNextpage();
              }
          }
      });


    }

    private void initSearchView(){

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mAdapter.displayLoading();
                recipeListViewModel.searchRecipesApi(s, 1);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }


    @Override
    public void onRecipeClick(int position) {

        Intent intent = new Intent(this , RecipeActivity.class);
        intent.putExtra("recipe",mAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        mAdapter.displayLoading();
        recipeListViewModel.searchRecipesApi(category, 1);
        mSearchView.clearFocus();
    }

    private void displaySearchCategories(){
        recipeListViewModel.setIsViewingRecipes(false);
        mAdapter.displaySearchCategories();
    }

    @Override
    public void onBackPressed() {

        if(recipeListViewModel.onBackPressed()){
            super.onBackPressed();
        }else{
            displaySearchCategories();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_categories){
            displaySearchCategories();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }
}

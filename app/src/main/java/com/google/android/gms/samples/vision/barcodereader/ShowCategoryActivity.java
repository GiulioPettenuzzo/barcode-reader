package com.google.android.gms.samples.vision.barcodereader;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.samples.vision.barcodereader.adapters.ShowCategoryAdapter;
import com.google.android.gms.samples.vision.barcodereader.adapters.ShowProductByCattegoryAdapter;
import com.google.android.gms.samples.vision.barcodereader.entities.Category;
import com.google.android.gms.samples.vision.barcodereader.entities.RealCattegory;

import java.util.ArrayList;

public class ShowCategoryActivity extends AppCompatActivity {
    private RecyclerView rVShowProduct;
    private RecyclerView.LayoutManager rVLayoutManager;
    private ShowCategoryAdapter showCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_category);

        setContentView(R.layout.activity_show_category);
        rVShowProduct = (RecyclerView)findViewById(R.id.cat_recycler_view);
        rVShowProduct.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVShowProduct.setLayoutManager(rVLayoutManager);
        showCategoryAdapter = new ShowCategoryAdapter(this,giveMeSomeCategory());
        rVShowProduct.setAdapter(showCategoryAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "onFabFoo");
            }
        });
    }

    public ArrayList<Category> giveMeSomeCategory(){
        ArrayList<Category> allCategory = new ArrayList<>();
        for(int i = 0;i<20;i++){
            Category category = new RealCattegory("Category " + i);
            allCategory.add(category);
        }
        return allCategory;
    }
}

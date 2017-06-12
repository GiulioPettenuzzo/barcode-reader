package com.google.android.gms.samples.vision.barcodereader;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;

import com.google.android.gms.samples.vision.barcodereader.adapters.ShowProductByCattegoryAdapter;
import com.google.android.gms.samples.vision.barcodereader.adapters.helpers.ItemTouchHelperCallback;
import com.google.android.gms.samples.vision.barcodereader.entities.Attribute;
import com.google.android.gms.samples.vision.barcodereader.entities.AttributesInPlus;
import com.google.android.gms.samples.vision.barcodereader.entities.Category;
import com.google.android.gms.samples.vision.barcodereader.entities.Position;
import com.google.android.gms.samples.vision.barcodereader.entities.Product;
import com.google.android.gms.samples.vision.barcodereader.entities.RealCattegory;
import com.google.android.gms.samples.vision.barcodereader.entities.RealPosition;
import com.google.android.gms.samples.vision.barcodereader.entities.RealProduct;

import java.util.ArrayList;

import static android.support.design.widget.NavigationView.*;

public class ShowProductByCattegoryActivity extends AppCompatActivity {

    private RecyclerView rVShowProduct;
    private RecyclerView.LayoutManager rVLayoutManager;
    private ShowProductByCattegoryAdapter showProductByCattegoryAdapter;
    private ItemTouchHelper itemTouchHelper;
    private Category category;
    private android.support.v7.app.ActionBar actionBar;
    ArrayList<Product> allProduct = new ArrayList<>();

    private final String SAVE_CATEGORY = "save_category_instant_state";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_by_cattegory);


        rVShowProduct = (RecyclerView)findViewById(R.id.rv_show_product);
        rVShowProduct.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVShowProduct.setLayoutManager(rVLayoutManager);
        actionBar = getSupportActionBar();


        if(savedInstanceState!=null&&savedInstanceState.containsKey(SAVE_CATEGORY)){
            category = (Category) savedInstanceState.getParcelable(SAVE_CATEGORY);
            actionBar.setTitle(category.getName());
           // showProductByCattegoryAdapter.setSelectedCategory(category);
            //the part of code below is for debug
            String cattname = category.getName();
            String catDesc = category.getDescription();
            Position catPlace = category.getPosition();
            for (Product currProduct:category.getAllProduct()) {
                String name = currProduct.getName();
                String barcode = currProduct.getBarcode();
                String desc = currProduct.getDescription();
                float price = currProduct.getPrice();
                String imageUrl = currProduct.getImageURL();
                ArrayList<Position> pos = currProduct.getPosition();
                ArrayList<Attribute> attr = currProduct.getNewAttributes();
            }
        }
        else{
            Intent intent = getIntent();
            Bundle oldBundle = intent.getBundleExtra("name");
            category = oldBundle.getParcelable("category_selected");
            actionBar.setTitle(category.getName());
        }

        showProductByCattegoryAdapter = new ShowProductByCattegoryAdapter(this,category.getAllProduct());
        showProductByCattegoryAdapter.setSelectedCategory(category);
        // showProductByCattegoryAdapter.setSelectedCategory(category);
        rVShowProduct.setAdapter(showProductByCattegoryAdapter);
        ItemTouchHelperCallback myItemTouchHelper = new ItemTouchHelperCallback(showProductByCattegoryAdapter);
        itemTouchHelper = new ItemTouchHelper(myItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rVShowProduct);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVE_CATEGORY,category);
        super.onSaveInstanceState(outState);
    }

    private class giveMeSomeProducts{

        private ArrayList<Product> allProduct;
        private Position position = new RealPosition("garage");
        private Position productPosition = new RealPosition("s1");

        private ArrayList<Product> giveMeTwentyProducts(){
            allProduct = new ArrayList<>();
            String name;
            for(int i = 0; i<20;i++){
                name = "name = " + i;
                Product product = new RealProduct("barcode",name);
                allProduct.add(product);
            }
            return allProduct;
        }

        private Category giveMeACattegory(){
            Category cattegory = new RealCattegory("cattegoria1",position);
            String name;
            for(int i = 0;i<20;i++){
                name = "prod " + i;
                Product product = new RealProduct("barcode",name,cattegory);
                product.setPosition(productPosition);
                product.setDescription("questa è la migliore birra italiana");
                product.setPrice(50);
                Attribute attribute = new AttributesInPlus("nome ","valore");
                product.setNewAttribute(attribute);
                product.setImageURL("http://www.donnamoderna.com/var/ezflow_site/storage/images/media/images/adv/moretti/birra-moretti/66481223-1-ita-IT/Birra-Moretti.jpg");
                cattegory.addNewProduct(product);
            }
            return cattegory;
        }
    }
}

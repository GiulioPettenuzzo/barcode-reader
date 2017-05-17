package com.google.android.gms.samples.vision.barcodereader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.samples.vision.barcodereader.adapters.ShowProductByCattegoryAdapter;
import com.google.android.gms.samples.vision.barcodereader.entities.Category;
import com.google.android.gms.samples.vision.barcodereader.entities.Position;
import com.google.android.gms.samples.vision.barcodereader.entities.Product;
import com.google.android.gms.samples.vision.barcodereader.entities.RealCattegory;
import com.google.android.gms.samples.vision.barcodereader.entities.RealPosition;
import com.google.android.gms.samples.vision.barcodereader.entities.RealProduct;

import java.util.ArrayList;

public class ShowProductByCattegoryActivity extends AppCompatActivity {

    private RecyclerView rVShowProduct;
    private RecyclerView.LayoutManager rVLayoutManager;
    private ShowProductByCattegoryAdapter showProductByCattegoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_by_cattegory);
        rVShowProduct = (RecyclerView)findViewById(R.id.rv_show_product);
        rVShowProduct.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVShowProduct.setLayoutManager(rVLayoutManager);

        giveMeSomeProducts products = new giveMeSomeProducts();
        RealCattegory cattegory = (RealCattegory) products.giveMeACattegory();
        ArrayList<Product> listOfProduct = cattegory.getAllProduct();
        showProductByCattegoryAdapter = new ShowProductByCattegoryAdapter(this,listOfProduct);
        rVShowProduct.setAdapter(showProductByCattegoryAdapter);

    }


    private class giveMeSomeProducts{

        private ArrayList<Product> allProduct;
        private Position position = new RealPosition("garage");

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
                name = "Prodotto " + i;
                Product product = new RealProduct("barcode",name);
                cattegory.addNewProduct(product);
            }
            return cattegory;
        }
    }
}
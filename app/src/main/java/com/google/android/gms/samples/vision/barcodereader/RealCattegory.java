package com.google.android.gms.samples.vision.barcodereader;

import java.util.ArrayList;

/**
 * Created by giuliopettenuzzo on 16/05/17.
 */

public class RealCattegory implements Cattegory {

    private String name;
    private String description;
    private Position position;
    private ArrayList<Product> allProduct;
    private int numberOfElements;

    public RealCattegory(String name,Position position){
        this.name = name;
        this.position = position;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public int getNumberOfElements() {
        return allProduct.size();
    }

    @Override
    public ArrayList<Product> getAllProduct() {
        return allProduct;
    }

    public void addNewProduct(Product product) {
        allProduct.add(product);
    }

    public void removeProduct(Product product) {
        allProduct.remove(product);
    }
}

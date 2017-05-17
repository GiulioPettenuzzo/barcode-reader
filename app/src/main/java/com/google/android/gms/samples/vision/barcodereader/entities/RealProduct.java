package com.google.android.gms.samples.vision.barcodereader.entities;

import java.util.ArrayList;

/**
 * Created by giuliopettenuzzo on 16/05/17.
 */

public class RealProduct implements Product {

    private String name;
    private float price;
    private String barcode;
    private String description;
    private String imageURL;
    private ArrayList<Attribute> attributesInPlus;
    private ArrayList<Position> allPosition;
    private Category cattegory;

    public RealProduct(String barcode,String name,Category cattegory){
        this.barcode = barcode;
        this.name = name;
        cattegory.addNewProduct(this);
    }
    //for the beginning, a constructor without cattegory
    public RealProduct(String barcode,String name){
        this.barcode = barcode;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getBarcode() {
        return barcode;
    }

    @Override
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public ArrayList<Position> getPosition() {
        return allPosition;
    }

    @Override
    public void setPosition(Position position) {
        allPosition.add(position);
    }

    @Override
    public String getImageURL() {
        return imageURL;
    }

    @Override
    public void setImageURL(String URL) {
        imageURL = URL;
    }

    @Override
    public ArrayList<Attribute> getNewAttributes() {
        return attributesInPlus;
    }

    @Override
    public void setNewAttribute(Attribute attribute) {
        attributesInPlus.add(attribute);
    }

    @Override
    public Category getCattegory() {
        return cattegory;
    }

    @Override
    public void setCattegory(Category cattegory) {
        this.cattegory = cattegory;
        cattegory.addNewProduct(this);
    }


}

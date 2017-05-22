package com.google.android.gms.samples.vision.barcodereader.entities;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by giuliopettenuzzo on 16/05/17.
 */

public class RealCattegory implements Category {

    private String name;
    private String description;
    private Position position;
    private ArrayList<Product> allProduct = new ArrayList<>();
    private int numberOfEqualsProduct;

    public RealCattegory(String name,Position position){
        this.name = name;
        this.position = position;
    }

    public RealCattegory(String name){
        this.name = name;
        this.position = new RealPosition("");
    }

    public static final Creator<RealCattegory> CREATOR = new Creator<RealCattegory>() {

        @Override
        public RealCattegory createFromParcel(Parcel source) {
            return new RealCattegory(source);
        }

        @Override
        public RealCattegory[] newArray(int size) {
            return new RealCattegory[size];
        }
    };

    public RealCattegory(Parcel source) {
        this(source.readString());
        for(int cycle = 0;cycle < source.dataSize();cycle++){
            RealProduct realProduct = source.readParcelable(RealProduct.class.getClassLoader());
            allProduct.add(realProduct);
        }
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

    @Override
    public int getNumberOfEqualsProduct(String barcode) {
        int productCounter = 0;
        for (Product product:allProduct) {
            if(product.getBarcode().compareTo(barcode)==0){
                productCounter++;
            }
        }
        return productCounter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(position.getName());
        for (Product currentProduct:allProduct) {
            dest.writeParcelable(currentProduct,flags);
        }
    }
}

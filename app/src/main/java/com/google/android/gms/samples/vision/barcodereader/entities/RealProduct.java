package com.google.android.gms.samples.vision.barcodereader.entities;

import android.os.Parcel;

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
    private ArrayList<Attribute> attributesInPlus = new ArrayList<>();
    private ArrayList<Position> allPosition = new ArrayList<>();
    private Category cattegory;

    public RealProduct(String barcode,String name,Category cattegory){
        this.barcode = barcode;
        this.name = name;
        this.cattegory = cattegory;
        allPosition.add(cattegory.getPosition());
        //cattegory.addNewProduct(this);
    }
    //for the beginning, a constructor without cattegory
    public RealProduct(String barcode,String name){
        this.barcode = barcode;
        this.name = name;
    }

    public static final Creator<RealProduct> CREATOR = new Creator<RealProduct>(){

        @Override
        public RealProduct createFromParcel(Parcel source) {
            return new RealProduct(source);
        }

        @Override
        public RealProduct[] newArray(int size) {
            return new RealProduct[size];
        }
    };

    public RealProduct(Parcel source) {
        this(source.readString(),source.readString(), (Category) source.readParcelable(RealCattegory.class.getClassLoader()));
        for(int cycle = 0;cycle<source.dataSize();cycle++){
            AttributesInPlus attribute = source.readParcelable(AttributesInPlus.class.getClassLoader());
            attributesInPlus.add(attribute);
        }

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
    public void setAllAttribute(ArrayList<Attribute> attributes) {
        attributesInPlus = attributes;
    }

    @Override
    public Category getCattegory() {
        return cattegory;
    }

    @Override
    public void setCattegory(Category cattegory) {
        this.cattegory = cattegory;
        allPosition.add(cattegory.getPosition());

        //cattegory.addNewProduct(this);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(barcode);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(imageURL);
        dest.writeFloat(price);
        dest.writeParcelable(cattegory,flags);
        //the position will be passed as a string not as an arraylist
        String position = "";
        for (Position currPos:allPosition) {
            if(position.isEmpty()){
                position = currPos.getName();
            }
            else {
                position = position + " - " + currPos.getName();
            }
        }
        dest.writeString(position);
        for (Attribute currentAttribute:attributesInPlus) {
            dest.writeParcelable(currentAttribute,flags);
        }
    }
}

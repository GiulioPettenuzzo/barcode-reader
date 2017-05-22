package com.google.android.gms.samples.vision.barcodereader.entities;

import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by giuliopettenuzzo on 16/05/17.
 */
//TODO: ALL THE INTERFACES MUST EXTENDS PARCELABLE OBJECTS

/**
 * this class represent a physical object, the same object the user detected scanning the barcode
 */
public interface Product extends Parcelable{
    //name
    String getName();
    void setName(String name);
    //barcode
    String getBarcode();
    void setBarcode(String barcode);
    //description
    String getDescription();
    void setDescription(String description);
    //price
    float getPrice();
    void setPrice(float price);
    //position
    ArrayList<Position> getPosition();
    void setPosition(Position position);
    //image
    String getImageURL();
    void setImageURL(String URL);
    //new attributes
    ArrayList<Attribute> getNewAttributes();
    void setNewAttribute(Attribute attribute);
    //cattegory
    Category getCattegory();
    void setCattegory(Category cattegory);
}

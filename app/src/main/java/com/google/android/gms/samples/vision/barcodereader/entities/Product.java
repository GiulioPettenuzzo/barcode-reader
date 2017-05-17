package com.google.android.gms.samples.vision.barcodereader.entities;

import java.util.ArrayList;

/**
 * Created by giuliopettenuzzo on 16/05/17.
 */
//TODO: ALL THE INTERFACES MUST EXTENDS PARCELABLE OBJECTS

/**
 * this class represent a physical object, the same object the user detected scanning the barcode
 */
public interface Product{
    //name
    public String getName();
    public void setName(String name);
    //barcode
    public String getBarcode();
    public void setBarcode(String barcode);
    //description
    public String getDescription();
    public void setDescription(String description);
    //price
    public float getPrice();
    public void setPrice(float price);
    //position
    public ArrayList<Position> getPosition();
    public void setPosition(Position position);
    //image
    public String getImageURL();
    public void setImageURL(String URL);
    //new attributes
    public ArrayList<Attribute> getNewAttributes();
    public void setNewAttribute(Attribute attribute);
    //cattegory
    public Category getCattegory();
    public void setCattegory(Category cattegory);
}
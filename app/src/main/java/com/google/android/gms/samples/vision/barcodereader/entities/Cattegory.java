package com.google.android.gms.samples.vision.barcodereader.entities;

import com.google.android.gms.samples.vision.barcodereader.entities.Position;
import com.google.android.gms.samples.vision.barcodereader.entities.Product;

import java.util.ArrayList;

/**
 * Created by giuliopettenuzzo on 16/05/17.
 */

public interface Cattegory {
    public void setName(String name);
    public String getName();
    public void setDescription(String description);
    public String getDescription();
    public void setPosition(Position position);
    public Position getPosition();
    public int getNumberOfElements();
    public ArrayList<Product> getAllProduct();
    public void addNewProduct(Product product);
    public void removeProduct(Product product);
}

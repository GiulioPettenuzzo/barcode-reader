package com.google.android.gms.samples.vision.barcodereader.entities;

import android.os.Parcelable;

import com.google.android.gms.samples.vision.barcodereader.entities.Position;
import com.google.android.gms.samples.vision.barcodereader.entities.Product;

import java.util.ArrayList;

/**
 * Created by giuliopettenuzzo on 16/05/17.
 */

/**
 * this object is created to group all the products that have somethink in common
 * the method in witch they will groupped will be choose from the user
 */
public interface Category extends Parcelable{
    /**
     * the user can set the name
     * @param name
     */
    void setName(String name);

    /**
     * @return the name of the cattegory
     */
    String getName();

    /**
     * set the description of the cattegory
     * @param description
     */
    void setDescription(String description);

    /**
     * @return the description
     */
    String getDescription();

    /**
     * a position is also an object that rappresent the physical position of the products , this method is use to set the position
     * @param position
     */
    void setPosition(Position position);

    /**
     * @return the position of the object
     */
    Position getPosition();

    /**
     * return the number of the products in the category
     * @return
     */
    int getNumberOfElements();

    /**
     * @return all the product in the category grouped in a list
     */
    ArrayList<Product> getAllProduct();

    /**
     * @param product add a product to this list
     */
    void addNewProduct(Product product);

    /**
     * @param product remove the product from this list
     */
    void removeProduct(Product product);

    /**
     * given a barcode, this method returns the number of product that have the same barcode
     * @param barcode
     * @return
     */
    int getNumberOfEqualsProduct(String barcode);
}

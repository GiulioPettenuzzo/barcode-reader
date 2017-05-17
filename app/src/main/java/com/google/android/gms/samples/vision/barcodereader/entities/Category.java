package com.google.android.gms.samples.vision.barcodereader.entities;

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
public interface Category {
    /**
     * the user can set the name
     * @param name
     */
    public void setName(String name);

    /**
     * @return the name of the cattegory
     */
    public String getName();

    /**
     * set the description of the cattegory
     * @param description
     */
    public void setDescription(String description);

    /**
     * @return the description
     */
    public String getDescription();

    /**
     * a position is also an object that rappresent the physical position of the products , this method is use to set the position
     * @param position
     */
    public void setPosition(Position position);

    /**
     * @return the position of the object
     */
    public Position getPosition();

    /**
     * return the number of the products in the category
     * @return
     */
    public int getNumberOfElements();

    /**
     * @return all the product in the category grouped in a list
     */
    public ArrayList<Product> getAllProduct();

    /**
     * @param product add a product to this list
     */
    public void addNewProduct(Product product);

    /**
     * @param product remove the product from this list
     */
    public void removeProduct(Product product);
}

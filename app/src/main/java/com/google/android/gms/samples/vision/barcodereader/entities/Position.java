package com.google.android.gms.samples.vision.barcodereader.entities;

/**
 * Created by giuliopettenuzzo on 16/05/17.
 */

/**
 * this class is created in order to represent a product's physical position
 * the only think the user cares is the name of the position
 */
public interface Position {
    /**
     * set the name of the position
     * @param name
     */
    public void setName(String name);

    /**
     * @return the name of the position
     */
    public String getName();
}

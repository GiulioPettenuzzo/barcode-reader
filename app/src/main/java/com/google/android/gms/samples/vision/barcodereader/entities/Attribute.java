package com.google.android.gms.samples.vision.barcodereader.entities;

/**
 * Created by giuliopettenuzzo on 16/05/17.
 */

/**
 * this object's scope is manipulate the new attributes that the user will create witch have a name and a value
 */
public interface Attribute {
    /**
     * @return the name  of the new attributes
     */
    public String getName();

    /**
     * to set the name of new Attributes
     * @param name
     */
    public void setName(String name);

    /**
     * @return the values of new Attributes
     */
    public String getValue();

    /**
     * the value of new Attributes
     * @param value
     */
    public void setValue(String value);
}

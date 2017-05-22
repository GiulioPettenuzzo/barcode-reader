package com.google.android.gms.samples.vision.barcodereader.entities;

/**
 * Created by giuliopettenuzzo on 16/05/17.
 */

import android.os.Parcelable;

/**
 * this object's scope is manipulate the new attributes that the user will create witch have a name and a value
 */
public interface Attribute extends Parcelable{
    /**
     * @return the name  of the new attributes
     */
    String getName();

    /**
     * to set the name of new Attributes
     * @param name
     */
    void setName(String name);

    /**
     * @return the values of new Attributes
     */
    String getValue();

    /**
     * the value of new Attributes
     * @param value
     */
    void setValue(String value);
}

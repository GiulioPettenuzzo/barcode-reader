package com.google.android.gms.samples.vision.barcodereader.entities;

import com.google.android.gms.samples.vision.barcodereader.entities.Attribute;

/**
 * Created by giuliopettenuzzo on 16/05/17.
 */

public class AttributesInPlus implements Attribute {
    private String name;
    private String value;

    public AttributesInPlus(String name,String value){
        this.name = name;
        this.value = value;
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
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}

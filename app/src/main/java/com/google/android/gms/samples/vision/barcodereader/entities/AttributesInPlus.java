package com.google.android.gms.samples.vision.barcodereader.entities;

import android.os.Parcel;

import com.google.android.gms.samples.vision.barcodereader.entities.Attribute;

/**
 * Created by giuliopettenuzzo on 16/05/17.
 * This class is created in order to help the insert new attributes action's
 */

public class AttributesInPlus implements Attribute {
    private String name;
    private String value;

    public AttributesInPlus(String name,String value){
        this.name = name;
        this.value = value;
    }

    public static final Creator<AttributesInPlus> CREATOR = new Creator<AttributesInPlus>() {
        @Override
        public AttributesInPlus createFromParcel(Parcel source) {
            return new AttributesInPlus(source);
        }

        @Override
        public AttributesInPlus[] newArray(int size) {
            return new AttributesInPlus[size];
        }
    };

    public AttributesInPlus(Parcel source) {
        this(source.readString(),source.readString());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(value);
    }
}

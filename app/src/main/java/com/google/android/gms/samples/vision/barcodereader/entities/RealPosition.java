package com.google.android.gms.samples.vision.barcodereader.entities;

import com.google.android.gms.samples.vision.barcodereader.entities.Position;

/**
 * Created by giuliopettenuzzo on 16/05/17.
 */

public class RealPosition implements Position {

    private String name;

    public RealPosition(String name){
        this.name = name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}

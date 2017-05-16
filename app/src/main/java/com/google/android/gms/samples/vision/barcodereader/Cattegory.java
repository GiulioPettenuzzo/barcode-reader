package com.google.android.gms.samples.vision.barcodereader;

import java.util.ArrayList;

/**
 * Created by giuliopettenuzzo on 16/05/17.
 */

public interface Cattegory {
    public void setName(String name);
    public String getName();
    public void setDescription();
    public String getDescription();
    public void setPosition(Position position);
    public Position getPosition();
    public int getNumberOfElements();
    public ArrayList<String> getAllProductsName();
}

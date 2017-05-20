package com.google.android.gms.samples.vision.barcodereader.adapters.helpers;

/**
 * Created by giuliopettenuzzo on 20/05/17.
 */

public interface ItemTouchHelperAdapter {

    /**
     * Called when user swiped an item ofthe list
     * @param position the position of swiped element in the list
     */
    void onItemSwiped(int position);
}

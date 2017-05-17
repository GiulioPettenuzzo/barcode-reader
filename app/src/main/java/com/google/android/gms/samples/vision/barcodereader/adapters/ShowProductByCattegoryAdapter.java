package com.google.android.gms.samples.vision.barcodereader.adapters;

import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.entities.Product;

import java.util.ArrayList;

/**
 * Created by giuliopettenuzzo on 17/05/17.
 */

public class ShowProductByCattegoryAdapter extends RecyclerView.Adapter<ShowProductByCattegoryAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Product> listOfProduct;


    public ShowProductByCattegoryAdapter(Context context, ArrayList<Product> listOfProduct){
        this.context = context;
        this.listOfProduct = listOfProduct;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewOfRecycle = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_product_by_cattegory_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(viewOfRecycle,(TextView)viewOfRecycle.findViewById(R.id.item_show_product_by_cattegory));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = listOfProduct.get(position);
        holder.productName.setText(product.getName());
    }

    @Override
    public int getItemCount() {
        return listOfProduct.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        View itemView;

        public ViewHolder(View itemView,TextView productName) {
            super(itemView);
            this.itemView = itemView;
            this.productName = productName;
        }
    }
}

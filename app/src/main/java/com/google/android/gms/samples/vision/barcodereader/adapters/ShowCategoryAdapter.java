package com.google.android.gms.samples.vision.barcodereader.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.entities.Category;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by giuliopettenuzzo on 06/06/17.
 */

public class ShowCategoryAdapter extends RecyclerView.Adapter<ShowCategoryAdapter.ViewHolder> {

    private ArrayList<Category> allCategory;
    Context context;

    public ShowCategoryAdapter(Context context,ArrayList<Category> allCategory){
        this.allCategory = allCategory;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewOfRecycle = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_category_item,parent,false);
        ShowCategoryAdapter.ViewHolder viewHolder = new ShowCategoryAdapter.ViewHolder(viewOfRecycle,(TextView)viewOfRecycle.findViewById(R.id.category_name),
                (TextView)viewOfRecycle.findViewById(R.id.number_of_product));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category = allCategory.get(position);
        holder.categoryNameTextView.setText(category.getName());
        holder.numberOfProductTextView.setText(String.valueOf(category.getNumberOfElements()));
    }

    @Override
    public int getItemCount() {
        return allCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView categoryNameTextView;
        TextView numberOfProductTextView;

        public ViewHolder(View itemView,TextView categoryNameTextView,TextView numberOfProductTextView) {
            super(itemView);
            this.categoryNameTextView = categoryNameTextView;
            this.numberOfProductTextView = numberOfProductTextView;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
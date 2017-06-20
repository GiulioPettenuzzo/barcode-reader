package com.google.android.gms.samples.vision.barcodereader.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.ShowCategoryActivity;
import com.google.android.gms.samples.vision.barcodereader.entities.Category;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by giuliopettenuzzo on 06/06/17.
 */

public class ShowCategoryAdapter extends RecyclerView.Adapter<ShowCategoryAdapter.ViewHolder> {

    private ArrayList<Category> allCategory;
    private OnCategoryItemListener onCategoryItemListener;
    Context context;
    public Category itemSelected;
    public Category categoryToGo;
    public View viewSelected;
    public boolean isLongPressed = false;
    public int color;


    public ShowCategoryAdapter(Context context,ArrayList<Category> allCategory,OnCategoryItemListener onCategoryItemListener){
        this.allCategory = allCategory;
        this.context = context;
        this.onCategoryItemListener = onCategoryItemListener;
    }

    public ArrayList<Category> getAllCategory(){
        return allCategory;
    }

    public void setItemSelected(Category category){
        itemSelected = category;
        notifyDataSetChanged();
    }
    /**
     *
     * @param category
     * @return the index of the element removed
     */
    public void removeCategory(Category category){
        allCategory.remove(category);
        notifyDataSetChanged();
    }
    public void addCategory(Category category){
        allCategory.add(category);
        notifyDataSetChanged();
    }

    /**
     * this method is useful when an item is modify.
     * the modify process is working like this: the category selected is delete and a new category will create
     * and take the place of the odl one.
     * without this method when you modify an item, it will finish at the end of the recycler view.
     * @param category1 the old category
     * @param category2 the new category
     */
    public void exchangeCategory(Category category1,Category category2){
        int index = allCategory.indexOf(category1);
        category2.setAllProduct(category1.getAllProduct());
        allCategory.remove(category1);
        allCategory.add(index,category2);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewOfRecycle = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_category_item,parent,false);
        ColorDrawable viewColor = (ColorDrawable) viewOfRecycle.getBackground();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            color = viewColor.getColor();
        }
        ViewHolder viewHolder = new ViewHolder(viewOfRecycle,(TextView)viewOfRecycle.findViewById(R.id.category_name),
                (TextView)viewOfRecycle.findViewById(R.id.number_of_product));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Category category = allCategory.get(position);
        holder.categoryNameTextView.setText(category.getName());
        holder.numberOfProductTextView.setText(String.valueOf(category.getNumberOfElements()));

        if(itemSelected != null && itemSelected != category) {
            holder.itemView.setOnLongClickListener(null);
        }
        //still select when the view scroll
        else if(itemSelected == null || itemSelected == category) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemSelected = category;
                    if(isLongPressed = false)
                        isLongPressed = true;
                    else
                        isLongPressed = false;
                    viewSelected = v;
                    notifyDataSetChanged();
                    return onCategoryItemListener.onCategoryLongClick();
                }
            });
        }
        //to remember the instance of long pressed
        if(itemSelected==category){
            holder.itemView.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
        }
        else{
            holder.itemView.setBackgroundColor(color);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryToGo = category;
                onCategoryItemListener.onCategoryClick();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return allCategory.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView categoryNameTextView;
        TextView numberOfProductTextView;

        public ViewHolder(View itemView,TextView categoryNameTextView,TextView numberOfProductTextView) {
            super(itemView);
            this.itemView = itemView;
            this.categoryNameTextView = categoryNameTextView;
            this.numberOfProductTextView = numberOfProductTextView;
        }


    }
    public interface OnCategoryItemListener {
        boolean onCategoryLongClick();
        void onCategoryClick();
    }
}
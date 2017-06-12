package com.google.android.gms.samples.vision.barcodereader.adapters;

import android.app.Dialog;
import android.content.Context;
import android.database.DataSetObservable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.VolleyActivity;
import com.google.android.gms.samples.vision.barcodereader.entities.Category;
import com.google.android.gms.samples.vision.barcodereader.entities.Position;
import com.google.android.gms.samples.vision.barcodereader.entities.RealCattegory;
import com.google.android.gms.samples.vision.barcodereader.entities.RealPosition;

import java.util.ArrayList;

/**
 * Created by giuliopettenuzzo on 01/06/17.
 */

public class SpinnerListAdapter extends ArrayAdapter<Category> implements AdapterView.OnItemSelectedListener{
    ArrayList<Category> allCategory;
    Context context;
    private final DataSetObservable mDataSetObservable = new DataSetObservable();
    TextView textViewForCategoryPosition;
    Category selectedCategory;


    public SpinnerListAdapter(Context context,int textViewResourceId,ArrayList<Category> allCategory){
        super(context,textViewResourceId,allCategory);
        this.context=context;
        this.allCategory=allCategory;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(context);
        textView.setText(allCategory.get(position).getName());
        textView.setTextSize(22);
        if(textView.getText().toString().compareTo("crea nuova cattegoria")==0){
            textView.setTextColor(context.getResources().getColor(android.R.color.holo_green_light));
        }
        return textView;
    }

    @Override
    public int getCount() {
        return allCategory.size();
    }

    @Override
    public Category getItem(int position) {
        return allCategory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(context);
        Category cat = allCategory.get(position);
        String name = allCategory.get(position).getName();
        textView.setText(allCategory.get(position).getName());
        textView.setTextSize(22);
        return textView;
    }

    @Override
    public boolean isEmpty() {
        if(allCategory.size()==0){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final Category forRecognize = allCategory.get(position);
        final String[] catName = new String[1];
        final String[] catPos = new String[1];
        final String[] catDesc = new String[1];
        if(forRecognize.getName().compareTo("crea nuova cattegoria")==0){
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.create_category_dialog);

            final EditText categoryName = (EditText) dialog.findViewById(R.id.insert_category_name);
            categoryName.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            categoryName.setText(R.string.insert_category_name);
            categoryName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    categoryName.setTextColor(context.getResources().getColor(android.R.color.white));

                    if(categoryName.getText().toString().compareTo(context.getResources().getString(R.string.insert_category_name))==0) {
                        categoryName.getText().clear();//clear the text when the user press it for the first time
                    }
                    else if(categoryName.getText().length()==0){
                        categoryName.setText(R.string.insert_category_name);
                        categoryName.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                    }
                }
            });

            final EditText categoryPosition = (EditText) dialog.findViewById(R.id.insert_category_position);
            categoryPosition.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            categoryPosition.setText(R.string.insert_category_position);
            categoryPosition.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    categoryPosition.setTextColor(context.getResources().getColor(android.R.color.white));

                    if(categoryPosition.getText().toString().compareTo(context.getResources().getString(R.string.insert_category_position))==0) {
                        categoryPosition.getText().clear();//clear the text when the user press it for the first time
                    }
                    else if(categoryPosition.getText().length()==0){
                        categoryPosition.setText(R.string.insert_category_position);
                        categoryPosition.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                    }
                }
            });

            final EditText categoryDescription = (EditText) dialog.findViewById(R.id.insert_category_description);
            categoryDescription.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            categoryDescription.setText(R.string.insert_category_description);
            categoryDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    categoryDescription.setTextColor(context.getResources().getColor(android.R.color.white));

                    if(categoryDescription.getText().toString().compareTo(context.getResources().getString(R.string.insert_category_description))==0) {
                        categoryDescription.getText().clear();//clear the text when the user press it for the first time
                    }
                    else if(categoryDescription.getText().length()==0){
                        categoryDescription.setText(R.string.insert_category_description);
                        categoryDescription.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                    }
                }
            });


            Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            Button saveButton = (Button) dialog.findViewById(R.id.save_button);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(categoryPosition.getText().toString().compareTo(context.getResources().getString(R.string.insert_category_position))!=0){
                        catPos[0] = categoryPosition.getText().toString();
                    }
                    else{
                        catPos[0] = null;
                    }
                    if(categoryDescription.getText().toString().compareTo(context.getResources().getString(R.string.insert_category_description))!=0
                            ||categoryDescription.getText().length()>0){
                        catDesc[0] = categoryDescription.getText().toString();
                    }
                    else{
                        catDesc[0] = null;
                    }
                    if(categoryName.getText().toString().compareTo(context.getResources().getString(R.string.insert_category_name))==0
                            ||categoryName.getText().length()==0){
                        Toast toast = Toast.makeText(context,"you must insert a category name",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else{
                        catName[0] = categoryName.getText().toString();

                        selectedCategory = new RealCattegory(catName[0]);
                        if(catPos[0]!=null){
                            selectedCategory.setPosition(new RealPosition(catPos[0]));

                        }
                        if(catDesc[0]!=null){
                            selectedCategory.setDescription(catDesc[0]);
                        }
                       // setSelectedCategory(selectedCategory);
                        allCategory.remove(forRecognize);
                        allCategory.add(selectedCategory);
                        allCategory.add(forRecognize);
                        if(selectedCategory.getPosition()!=null) {
                            textViewForCategoryPosition.setText(selectedCategory.getPosition().getName());
                        }
                        else{
                            textViewForCategoryPosition.setText("");
                            selectedCategory.setPosition(null);
                        }
                        setSelectedCategory(selectedCategory);
                        notifyDataSetChanged();
                        Toast toast = Toast.makeText(context,"category insert succesfull",Toast.LENGTH_SHORT);
                        toast.show();
                        dialog.cancel();
                    }


                }
            });
            dialog.show();

        }
        else{
                if(allCategory.get(position).getPosition()!=null) {
                    setSelectedCategory(allCategory.get(position));
                    textViewForCategoryPosition.setText(selectedCategory.getPosition().getName());
                }
                else {
                    textViewForCategoryPosition.setText("");
                }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * @return the position of the category selected
     */
    public Category getSelectedCategory(){
        return selectedCategory;
    }
    public void setSelectedCategory(Category category){
        selectedCategory = category;
    }
    public void setTextView(TextView textView){
        this.textViewForCategoryPosition=textView;
    }
    public TextView getTextView(){
        return textViewForCategoryPosition;
    }

}

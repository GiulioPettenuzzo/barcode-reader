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
import com.google.android.gms.samples.vision.barcodereader.ShowCategoryActivity;
import com.google.android.gms.samples.vision.barcodereader.VolleyActivity;
import com.google.android.gms.samples.vision.barcodereader.dialog.NewCategoryDialog;
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
    Dialog dialog;
    Dialog dialogCreate;
    private NewCategoryDialog dialogNewCategory;
    public boolean isCreated;



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

    public ArrayList<Category> getAllCategory(){
        return allCategory;
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
        //if the user select the last item the dialog to create a product appear
        if(forRecognize.getName().compareTo(context.getResources().getString(R.string.create_new_category))==0){
            dialogCreate = new Dialog(context);
            dialogCreate.setContentView(R.layout.create_category_dialog);
            dialogNewCategory = new NewCategoryDialog(context, dialogCreate, new NewCategoryDialog.EditDialogListener() {
                @Override
            public void onFinishEditDialog(Category category) {
                if(dialogNewCategory!=null) {
                    if (dialogNewCategory.getSelectedCategory() != null) {
                        allCategory.remove(forRecognize);
                        allCategory.add(dialogNewCategory.getSelectedCategory());
                        allCategory.add(forRecognize);
                        if(dialogNewCategory.getSelectedCategory().getPosition()!=null) {
                            textViewForCategoryPosition.setText(dialogNewCategory.getSelectedCategory().getPosition().getName());
                        }
                        else{
                            textViewForCategoryPosition.setText("");
                            dialogNewCategory.getSelectedCategory().setPosition(null);
                        }
                        setSelectedCategory(dialogNewCategory.getSelectedCategory());
                        isCreated = true;
                        notifyDataSetChanged();
                    }
                }
            }
            });
            dialogNewCategory.setDialog();
            dialogCreate.show();
                    }

        else{
            isCreated = false;
                if(allCategory.get(position).getPosition()!=null) {
                    setSelectedCategory(allCategory.get(position));
                    textViewForCategoryPosition.setText(selectedCategory.getPosition().getName());
                }
                else {
                    textViewForCategoryPosition.setText("");
                    setSelectedCategory(allCategory.get(position));
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
    public void setCreated(Boolean bool){
        this.isCreated = bool;
    }
}

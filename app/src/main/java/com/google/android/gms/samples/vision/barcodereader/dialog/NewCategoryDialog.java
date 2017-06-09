package com.google.android.gms.samples.vision.barcodereader.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.entities.Category;
import com.google.android.gms.samples.vision.barcodereader.entities.RealCattegory;
import com.google.android.gms.samples.vision.barcodereader.entities.RealPosition;

/**
 * Created by giuliopettenuzzo on 08/06/17.
 * in order to use this dialog more than one time
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("ValidFragment")

public class NewCategoryDialog extends DialogFragment {
    Context context;
    Dialog dialog;
    public Category selectedCategory;
    final String[] catName = new String[1];
    final String[] catPos = new String[1];
    final String[] catDesc = new String[1];
    private EditDialogListener listener;


    @SuppressLint("ValidFragment")
    public NewCategoryDialog(Context context, Dialog dialog,EditDialogListener editDialogListener) {
        this.dialog = dialog;
        this.context = context;
        listener = editDialogListener;
    }


    public void setDialog() {

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
                if(categoryPosition.getText().toString().compareTo(context.getResources().getString(R.string.insert_category_position))!=0
                        ||categoryPosition.getText().length()>0){
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


                    Toast toast = Toast.makeText(context,"category insert succesfull",Toast.LENGTH_SHORT);
                    toast.show();
                    listener.onFinishEditDialog(selectedCategory);
                    dialog.dismiss();
                }


            }
        });

    }
    public Category getSelectedCategory(){
        return  selectedCategory;
    }
    public interface EditDialogListener {
        void onFinishEditDialog(Category category);
    }
}

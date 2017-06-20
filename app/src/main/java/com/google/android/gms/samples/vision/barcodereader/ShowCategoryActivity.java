package com.google.android.gms.samples.vision.barcodereader;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.samples.vision.barcodereader.adapters.ShowCategoryAdapter;
import com.google.android.gms.samples.vision.barcodereader.dialog.NewCategoryDialog;
import com.google.android.gms.samples.vision.barcodereader.entities.Attribute;
import com.google.android.gms.samples.vision.barcodereader.entities.AttributesInPlus;
import com.google.android.gms.samples.vision.barcodereader.entities.Category;
import com.google.android.gms.samples.vision.barcodereader.entities.Position;
import com.google.android.gms.samples.vision.barcodereader.entities.Product;
import com.google.android.gms.samples.vision.barcodereader.entities.RealCattegory;
import com.google.android.gms.samples.vision.barcodereader.entities.RealPosition;
import com.google.android.gms.samples.vision.barcodereader.entities.RealProduct;

import java.util.ArrayList;

/**
 * this activity is created to show all the category in the application.
 * The main point of this activity is the long press of the items that allows to delate or modify the elment
 * from the buttons that appears in the menu.
 */
public class ShowCategoryActivity extends AppCompatActivity implements ShowCategoryAdapter.OnCategoryItemListener {
    private RecyclerView rVShowCategory;
    private RecyclerView.LayoutManager rVLayoutManager;
    private MenuItem deleteCategoryMenuItem;
    private MenuItem editCategoryMenuItem;
    private ShowCategoryAdapter showCategoryAdapter;
    private android.support.v7.app.ActionBar actionBar;
    private NewCategoryDialog dialogNewCategory;
    private ArrayList<Category> allCategory = new ArrayList<>();
    private Category itemSelected;
    private Menu menu;
    FloatingActionButton fab;
    Dialog dialog;
    Dialog dialogCreate;


    private static final String SAVE_CATEGORIES = "categories_to_save";
    private static final String CATEGORY_SELECTED = "categories_selected";
    private static final String BUNDLE_NAME = "categories_selected";




    // the navigation bar of the application
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.all_category:
                    return true;
                case R.id.new_product:
                    Intent intent = new Intent(getApplicationContext(),BarcodeCaptureActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_category);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.all_category);


        rVShowCategory = (RecyclerView)findViewById(R.id.cat_recycler_view);
        rVShowCategory.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVShowCategory.setLayoutManager(rVLayoutManager);
        allCategory = giveMeSomeCategory();
        if(savedInstanceState!=null&&savedInstanceState.containsKey(SAVE_CATEGORIES)){
            allCategory = savedInstanceState.getParcelableArrayList(SAVE_CATEGORIES);
            if(savedInstanceState.containsKey(CATEGORY_SELECTED)) {
                itemSelected = savedInstanceState.getParcelable(CATEGORY_SELECTED);
            }
        }

        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.show_cat_activity_title);
        showCategoryAdapter = new ShowCategoryAdapter(this,allCategory,this);




        dialog = new Dialog(getApplicationContext());

        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCreate = new Dialog(ShowCategoryActivity.this);
                dialogCreate.setContentView(R.layout.create_category_dialog);
                dialogNewCategory = new NewCategoryDialog(ShowCategoryActivity.this, dialogCreate, new NewCategoryDialog.EditDialogListener() {
                    @Override
                    public void onFinishEditDialog(Category category) {
                        if(dialogNewCategory!=null) {
                            if (dialogNewCategory.getSelectedCategory() != null) {
                                showCategoryAdapter.addCategory(dialogNewCategory.getSelectedCategory());
                            }
                        }
                    }
                });
                dialogNewCategory.setDialog();
                dialogCreate.show();
            }
        });
        if(itemSelected!=null) {
            showCategoryAdapter.setItemSelected(itemSelected);
            showCategoryAdapter.isLongPressed = true;
            actionBar.setTitle("");
            fab.setVisibility(View.GONE);
        }

        rVShowCategory.setAdapter(showCategoryAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu,menu);
        this.menu = menu;
        deleteCategoryMenuItem = menu.findItem(R.id.delete_item);
        editCategoryMenuItem = menu.findItem(R.id.edit_category_menu_item);
        if(showCategoryAdapter.itemSelected==null) {
            deleteCategoryMenuItem.setVisible(false);
            editCategoryMenuItem.setVisible(false);
        }
        else{
            deleteCategoryMenuItem.setVisible(true);
            editCategoryMenuItem.setVisible(true);
        }
        deleteCategoryMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dialog = new Dialog(ShowCategoryActivity.this);
                dialog.setContentView(R.layout.delete_product_dialog);
                TextView textView = (TextView) dialog.findViewById(R.id.textViewx);
                textView.setText(R.string.delete_category_dialog);
                Button cancel = (Button) dialog.findViewById(R.id.button_ok);
                Button dontCancel = (Button) dialog.findViewById(R.id.button_no);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCategoryAdapter.removeCategory(showCategoryAdapter.itemSelected);
                        showCategoryAdapter.itemSelected = null;
                        showCategoryAdapter.isLongPressed = false;
                        actionBar.setTitle(R.string.show_cat_activity_title);
                        fab.setVisibility(View.VISIBLE);
                        deleteCategoryMenuItem.setVisible(false);
                        editCategoryMenuItem.setVisible(false);
                        dialog.cancel();
                    }
                });
                dontCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                return true;
            }
        });
        editCategoryMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dialogCreate = new Dialog(ShowCategoryActivity.this);
                dialogCreate.setContentView(R.layout.create_category_dialog);
                dialogNewCategory = new NewCategoryDialog(ShowCategoryActivity.this, dialogCreate, new NewCategoryDialog.EditDialogListener() {
                    @Override
                    public void onFinishEditDialog(Category category) {
                        if(dialogNewCategory!=null) {
                            if (dialogNewCategory.getSelectedCategory() != null) {
                                showCategoryAdapter.exchangeCategory(showCategoryAdapter.itemSelected,dialogNewCategory.getSelectedCategory());
                                showCategoryAdapter.itemSelected = null;
                                showCategoryAdapter.isLongPressed = false;
                                actionBar.setTitle(R.string.show_cat_activity_title);
                                fab.setVisibility(View.VISIBLE);
                                deleteCategoryMenuItem.setVisible(false);
                                editCategoryMenuItem.setVisible(false);
                            }
                        }
                    }
                });
                dialogNewCategory.setDialog();
                TextView title = (TextView) dialogCreate.findViewById(R.id.create_category_title);
                title.setText(R.string.edit_category);
                dialogCreate.show();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * this class is used for debug. I want to show some product in the list
     * a next version of the application will use a database.
     * @return
     */
    public ArrayList<Category> giveMeSomeCategory(){
        ArrayList<Category> allCategory = new ArrayList<>();
        for(int i = 0;i<20;i++){
            Category category = new RealCattegory("Category " + i);
            Position catPos = new RealPosition("scaffale");

            category.setPosition(catPos);

            for(int j = 0;j<i;j++){
                String name = "prod " + j;
                Product product = new RealProduct("barcode",name,category);
                Position pos = new RealPosition("garage");
                product.setPosition(pos);
                product.setDescription("questa è la migliore birra italiana");
                product.setPrice(50);
                Attribute attribute = new AttributesInPlus("nome ","valore");
                product.setNewAttribute(attribute);
                product.setImageURL("http://www.donnamoderna.com/var/ezflow_site/storage/images/media/images/adv/moretti/birra-moretti/66481223-1-ita-IT/Birra-Moretti.jpg");
                category.addNewProduct(product);
            }

            allCategory.add(category);
        }
        return allCategory;
    }


    /**
     * this is the layout's way to delete an item
     * when the item is long pressed the activity name and the floating action button will dissolve
     * @return
     */
    @Override
    public boolean onCategoryLongClick() {
        if(showCategoryAdapter.isLongPressed==false) {
            showCategoryAdapter.isLongPressed = true;
            deleteCategoryMenuItem.setVisible(true);
            editCategoryMenuItem.setVisible(true);

            actionBar.setTitle("");
            fab.setVisibility(View.GONE);
            showCategoryAdapter.viewSelected.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
        return true;
    }

    /**
     * this send an intent to ShowProductByCategoryActivity
     */
    @Override
    public void onCategoryClick() {
        if(showCategoryAdapter.itemSelected == null) {
            Intent intent = new Intent(this,ShowProductByCattegoryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(CATEGORY_SELECTED,showCategoryAdapter.categoryToGo);
            intent.putExtra(BUNDLE_NAME,bundle);
            startActivity(intent);

        }
        else{
            showCategoryAdapter.itemSelected = null;
            showCategoryAdapter.isLongPressed = false;
            actionBar.setTitle(R.string.show_cat_activity_title);
            fab.setVisibility(View.VISIBLE);
            deleteCategoryMenuItem.setVisible(false);
            editCategoryMenuItem.setVisible(false);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SAVE_CATEGORIES, showCategoryAdapter.getAllCategory());
        if(showCategoryAdapter.itemSelected!=null)
            outState.putParcelable(CATEGORY_SELECTED,showCategoryAdapter.itemSelected);
        super.onSaveInstanceState(outState);
    }




}

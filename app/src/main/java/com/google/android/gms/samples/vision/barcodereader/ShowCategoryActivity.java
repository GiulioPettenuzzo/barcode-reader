package com.google.android.gms.samples.vision.barcodereader;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
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
    private static final String VIEW_SELECTED = "view_selected";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_category);

        setContentView(R.layout.activity_show_category);
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
        showCategoryAdapter = new ShowCategoryAdapter(this,allCategory,this);




        dialog = new Dialog(getApplicationContext());
        //actionBar.setTitle("ok");

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
                Button cancel = (Button) dialog.findViewById(R.id.button_ok);
                Button dontCancel = (Button) dialog.findViewById(R.id.button_no);
                final int[] i = {0};

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCategoryAdapter.removeCategory(showCategoryAdapter.itemSelected);
                        showCategoryAdapter.itemSelected = null;
                        showCategoryAdapter.isLongPressed = false;
                        actionBar.setTitle(R.string.title_activity_main);
                        fab.setVisibility(View.VISIBLE);
                        deleteCategoryMenuItem.setVisible(false);
                        editCategoryMenuItem.setVisible(false);
                        dialog.cancel();
                        i[0] =1;
                    }
                });
                dontCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        i[0] = 1;
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
                                actionBar.setTitle(R.string.title_activity_main);
                                fab.setVisibility(View.VISIBLE);
                                deleteCategoryMenuItem.setVisible(false);
                                editCategoryMenuItem.setVisible(false);
                            }
                        }
                    }
                });
                dialogNewCategory.setDialog();
                TextView title = (TextView) dialogCreate.findViewById(R.id.create_category_title);
                title.setText("Edit this Category");
                dialogCreate.show();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public ArrayList<Category> giveMeSomeCategory(){
        ArrayList<Category> allCategory = new ArrayList<>();
        for(int i = 0;i<20;i++){
            Category category = new RealCattegory("Category " + i);
            Position catPos = new RealPosition("scaffale");

            category.setPosition(catPos);
            //Product product = new RealProduct("ciao","barcode");
            //product.setImageURL("http://www.donnamoderna.com/var/ezflow_site/storage/images/media/images/adv/moretti/birra-moretti/66481223-1-ita-IT/Birra-Moretti.jpg");

           // category.addNewProduct(product);

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
     * this is the layout's way to delate an item
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

    @Override
    public void onCategoryClick() {
        if(showCategoryAdapter.itemSelected == null) {
            Toast toast = Toast.makeText(this, "entra in questa cattegoria", Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(this,ShowProductByCattegoryActivity.class);
            //intent.putExtra("category_selected",showCategoryAdapter.categoryToGo);
            //startActivity(intent);
            Bundle bundle = new Bundle();
            bundle.putParcelable("category_selected",showCategoryAdapter.categoryToGo);
            intent.putExtra("name",bundle);
            startActivity(intent);

           // intent.putParcelableArrayListExtra("all_product",showCategoryAdapter.categoryToGo.getAllProduct());
           // startActivity(intent);

        }
        else{
            showCategoryAdapter.itemSelected = null;
            showCategoryAdapter.isLongPressed = false;
            actionBar.setTitle(R.string.title_activity_main);
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

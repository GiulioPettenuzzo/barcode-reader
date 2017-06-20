package com.google.android.gms.samples.vision.barcodereader;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.samples.vision.barcodereader.adapters.SpinnerListAdapter;
import com.google.android.gms.samples.vision.barcodereader.entities.Attribute;
import com.google.android.gms.samples.vision.barcodereader.entities.AttributesInPlus;
import com.google.android.gms.samples.vision.barcodereader.entities.Category;
import com.google.android.gms.samples.vision.barcodereader.entities.Position;
import com.google.android.gms.samples.vision.barcodereader.entities.Product;
import com.google.android.gms.samples.vision.barcodereader.entities.RealCattegory;
import com.google.android.gms.samples.vision.barcodereader.entities.RealPosition;
import com.google.android.gms.samples.vision.barcodereader.entities.RealProduct;
import com.google.android.gms.samples.vision.barcodereader.urlManagement.ImageUrlUnpacker;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * This activity is used to crete a new product.
 * the user can insert all the information he want about the product
 */
public class VolleyActivity extends AppCompatActivity {

    TextView barcodeView;
    ImageView mImageView;
    ImageButton buttonPrev;
    ImageButton buttonNext;
    ImageButton photoButton;
    ImageButton imageSelected;
    EditText nameTextView;
    EditText priceTextView;
    EditText descriptionTextView;
    EditText positionTextView;
    Button addPosButton;
    public TextView showAllPositionsTextView;
    Spinner showCategoryView;
    Button addAttributeButton;
    Button saveButton;

    private int numPhotoSelected;
    private int maxNumImage; //given by unpacker in order to know how many image are there in yahoo image searching this barcode
    boolean isClicked = false;
    public ArrayList<Position> tempPosition = new ArrayList<>();
    public ArrayList<Attribute> attributeInPlas = new ArrayList<>();
    private int numberOfAttributeInPlus = 100;
    private SpinnerListAdapter spinnerListAdapter;
    private Category checkCategory;
    private String imageUrl;
    private String stringBarcode;
    Drawable drawable_saved;
    Drawable drawable_not_saved;
    private android.support.v7.app.ActionBar actionBar;

    ImageUrlUnpacker imageUrlUnpacker = new ImageUrlUnpacker();

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_CODE = 73883;




    // constants used to pass extra data in the intent
    public static final String initialYahooURL = "https://it.images.search.yahoo.com/search/images;_ylt=A9mSs3TQLxBZDrgATj0bDQx.;_ylu=X3oDMTB0ZTgxN3Q0BGNvbG8DaXIyBHBvcwMxBHZ0aWQDBHNlYwNwaXZz?p=";
    public static final String finalYahooURL = "&fr=yfp-t-909&fr2=piv-web";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        barcodeView = (TextView) findViewById(R.id.barcode_view);
        mImageView = (ImageView) findViewById(R.id.imageView);
        //this is used to show all the position and sub-position that the user are adding to the creating product
        showAllPositionsTextView = (TextView) findViewById(R.id.show_positions_view);
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.volley_activity_title);

        /**setting the spinner who show all the category in the database
         * the spinner is created in order to choose or create a category,
         * for creating a category you need to click the last item of the list and the same dialog will appears
         * the first item is <none> if the user doesn't need a category
         * NB: when an user change a category, all the sub-position inserted in the editText below will loose.
         */
        showCategoryView = (Spinner) findViewById(R.id.show_category_view);
        spinnerListAdapter = new SpinnerListAdapter(this,R.layout.spinner_list_item,giveMeACategory());
        showCategoryView.setAdapter(spinnerListAdapter);
        showCategoryView.setOnItemSelectedListener(spinnerListAdapter);
        spinnerListAdapter.setTextView(showAllPositionsTextView);
        if(spinnerListAdapter.getSelectedCategory()!=null) {
            if (spinnerListAdapter.getSelectedCategory().getName().compareTo("<none>") != 0) {
                tempPosition.add(spinnerListAdapter.getSelectedCategory().getPosition());
                showAllPositionsTextView.setText(getAllPositionInString());
            }
        }

        //the TextView who show the position that the user insert
        //see the comment in setAddPosButton for description
        showAllPositionsTextView.setText("");
        showAllPositionsTextView.setTextColor(getResources().getColor(android.R.color.holo_green_light));

        //for insert the name
        setNameTextView();
        //for insert the price
        setPriceTextView();
        //for insert description
        setDescriptionTextView();
        //for type sub-position
        setPositionTextView();
        //for insert sub-position
        setAddPosButton();
        /**
         * this Button Allowed to insert programmatically two EditText witch the user will insert
         * the name and the values of the new attribute
         * */
        setAddAttributeButton();

        // THE PART OF THE CODE BELOW IS USED FOR SET THE ACTIVITY WITH THE BARCODE READER
        //recive the intent from BarcodeCaptureActivity who sent the barcode number
        Intent intent = getIntent();
        Barcode barcode = intent.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

        final String url = initialYahooURL + barcode.displayValue + finalYahooURL;

        stringBarcode = barcode.displayValue;
        barcodeView.setText(stringBarcode);

        //using volley to take the picture from yahoo image
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //saving response for pircing
                        imageUrlUnpacker.setResponce(response);
                        String image_url = imageUrlUnpacker.getMyString();
                        if(image_url.compareTo(getResources().getString(R.string.error))!=0) {
                            new DownloadImageTask((ImageView) mImageView)
                                    .execute(image_url);
                            maxNumImage = imageUrlUnpacker.countImageOnUrl();
                        }
                        else{
                            Toast toast = Toast.makeText(getApplicationContext(),R.string.image_not_found,Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageUrlUnpacker.setResponce(getResources().getString(R.string.no_internet_connection));
                Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.no_internet_connection),Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        //for change the image
        setButtonPrev();
        setButtonNext();
        //for save the image
        setImageSelectedButton();
        //for tacke a picture
        setPhotoButton();
        //for save the whole product created
        setSaveButton();

        loadAllImage();
    }

    /**
     * insert the name of the product
     */
    private void setNameTextView(){
        nameTextView = (EditText) findViewById(R.id.name_view);
        /**
         * set the initial value of the text ho explain the user how the text view is used for.
         * this two line are present everywhere we need to explayn the user what an edit text is used without
         * adding more text view
         */
        nameTextView.setText(R.string.insert_name);
        nameTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        nameTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                nameTextView.setTextColor(getResources().getColor(android.R.color.white));

                if(nameTextView.getText().toString().compareTo(getResources().getString(R.string.insert_name))==0) {
                    nameTextView.getText().clear();//clear the text when the user press it for the first time
                }
                else if(nameTextView.getText().length()==0){
                    nameTextView.setText(R.string.insert_name);
                    nameTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
        });
    }

    /**
     * insert the price of the product, only number are allowed
     */
    private void setPriceTextView(){
        priceTextView = (EditText) findViewById(R.id.price_view);
        priceTextView.setText(R.string.insert_price);
        priceTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        priceTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                priceTextView.setTextColor(getResources().getColor(android.R.color.white));
                priceTextView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                if(priceTextView.getText().toString().compareTo(getResources().getString(R.string.insert_price))==0){
                    priceTextView.getText().clear();
                }
                else if(priceTextView.getText().length()==0){
                    priceTextView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    priceTextView.setText(R.string.insert_price);
                    priceTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
        });
    }

    /**
     * insert the description of the product
     */
    private void setDescriptionTextView(){
        descriptionTextView = (EditText) findViewById(R.id.description_view);
        descriptionTextView.setText(R.string.insert_description);
        descriptionTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        descriptionTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                descriptionTextView.setTextColor(getResources().getColor(android.R.color.white));
                if(descriptionTextView.getText().toString().compareTo(getResources().getString(R.string.insert_description))==0){
                    descriptionTextView.getText().clear();
                }
                else if(descriptionTextView.getText().length()==0){
                    descriptionTextView.setText(R.string.insert_description);
                    descriptionTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
        });
    }

    /**
     * button to switch image
     */
    private void setButtonPrev(){
        buttonPrev = (ImageButton) findViewById(R.id.button_prev);
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxNumImage = imageUrlUnpacker.getMax();
                numPhotoSelected = imageUrlUnpacker.getImageNum();
                if(numPhotoSelected == 1){
                    numPhotoSelected=maxNumImage;
                }
                else {
                    numPhotoSelected--;
                }
                imageUrlUnpacker.setImageNum(numPhotoSelected);
                String image_url = imageUrlUnpacker.getMyString();
                new DownloadImageTask((ImageView) mImageView)
                        .execute(image_url);
            }
        });

    }

    /**
     * button to switch image
     */
    private void setButtonNext(){
        buttonNext = (ImageButton) findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxNumImage = imageUrlUnpacker.getMax();
                numPhotoSelected = imageUrlUnpacker.getImageNum();
                if(numPhotoSelected==maxNumImage){
                    numPhotoSelected=1;
                }
                else {
                    numPhotoSelected++;
                }
                imageUrlUnpacker.setImageNum(numPhotoSelected);
                String image_url = imageUrlUnpacker.getMyString();
                new DownloadImageTask((ImageView) mImageView)
                        .execute(image_url);
            }
        });
    }

    /**
     * to save the image. when it is pressed, it change the style and the buttonPrev, ButtonNext and photoButton will be invisible
     */
    private void setImageSelectedButton() {
        imageSelected = (ImageButton) findViewById(R.id.saveImageButton);
        drawable_saved = getDrawable(R.drawable.ic_check_green_24dp);
        drawable_not_saved = imageSelected.getDrawable();
        imageSelected.setOnClickListener(new View.OnClickListener() {
            //change the drawable when the button is clicked and hide the other button
            @Override
            public void onClick(View v) {
                if(isClicked==false){
                    imageSelected.setImageDrawable(drawable_saved);
                    buttonPrev.setVisibility(View.GONE);
                    buttonNext.setVisibility(View.GONE);
                    photoButton.setVisibility(View.GONE);
                    isClicked = true;
                    imageUrlUnpacker.setImageNum(numPhotoSelected);
                    imageUrl = imageUrlUnpacker.getMyString();
                }
                else{
                    imageSelected.setImageDrawable(drawable_not_saved);
                    buttonPrev.setVisibility(View.VISIBLE);
                    buttonNext.setVisibility(View.VISIBLE);
                    photoButton.setVisibility(View.VISIBLE);
                    isClicked = false;
                }
            }
        });
    }

    /**
     * to take a picture if the software is not able to find any photo in the web
     * NB: the image will not be save anywhere. this button can be usefull in a future implementation
     */
    private void setPhotoButton(){
        photoButton = (ImageButton) findViewById(R.id.photoButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();
            }
        });

    }

    /**
     *  this is for adding sub-position
     * to delete a sub_position the user needs to change category in the spinner.
     */
    private void setPositionTextView(){
        positionTextView = (EditText) findViewById(R.id.position_view);
        positionTextView.setText(R.string.insert_new_place);
        positionTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        positionTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                positionTextView.setTextColor(getResources().getColor(android.R.color.white));
                if(positionTextView.getText().toString().compareTo(getResources().getString(R.string.insert_new_place))==0){
                    positionTextView.getText().clear();
                }
                else if(positionTextView.getText().length()==0){
                    positionTextView.setText(R.string.insert_new_place);
                    positionTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
        });

    }

    /**
     * the categories could have a default position or not.
     * when the user select a category from the spinner, the position of the category appear in the text view showAllPositionsTextView
     * and the user can ever add some sub-position with the EditText setPositionTextView.
     * with this button the user can add a sub-position for this product.
     * the position start ever with the category that the product belongs
     */
    private void setAddPosButton(){
        addPosButton = (Button) findViewById(R.id.add_pos_button);
        addPosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(positionTextView.getText().toString().compareTo(getResources().getString(R.string.insert_new_place))!=0 && !positionTextView.getText().toString().isEmpty()) {
                    Position position = new RealPosition(positionTextView.getText().toString());
                    if(spinnerListAdapter.getSelectedCategory()!=null) {
                        if (spinnerListAdapter.getSelectedCategory().getPosition() == null) {
                            tempPosition.clear();
                            Position tempPosition = new RealPosition("");
                            spinnerListAdapter.getSelectedCategory().setPosition(tempPosition);
                            spinnerListAdapter.setCreated(false);
                        }

                    }


                    if(spinnerListAdapter.isCreated==true){
                        tempPosition.clear();
                        tempPosition.add(position);
                        spinnerListAdapter.setCreated(false);
                    }
                    else
                        tempPosition.add(position);
                    positionTextView.setText(R.string.insert_new_place);
                    positionTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    showAllPositionsTextView.setText(getAllPositionInString());
                    positionTextView.getText().clear();
                    if(positionTextView.hasFocus()==true){
                        positionTextView.setTextColor(getResources().getColor(android.R.color.white));
                    }
                    else{
                        positionTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));

                    }

                }
                else if(positionTextView.getText().toString().compareTo(getResources().getString(R.string.insert_new_place))==0||
                        positionTextView.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),R.string.position_allert,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    /**
     * this button add attributes programmatically.
     * when the button is pressed, two edit text will appear so the user can add some attributes in plus if he need.
     * the id of the created view is ever a multiple of 100
     */
    private void setAddAttributeButton(){
        addAttributeButton = (Button) findViewById(R.id.button_add_attribute);
        addAttributeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.constraint);
                ConstraintSet set = new ConstraintSet();
                set.clone(layout);
                final EditText nameTextEdit = new EditText(getApplicationContext());
                final EditText valueTextEdit = new EditText(getApplicationContext());

                /**
                 * if the text edit setting programmatically is the first item setted it will connect between
                 * description view and addAttributeButton
                 * otherwise it will set between the last edit text created programmatically and the addAttributeButton
                 */
                if(numberOfAttributeInPlus==100) {
                    //name Text Edit:
                    nameTextEdit.setText(R.string.new_attribute_name);
                    nameTextEdit.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    nameTextEdit.setId(numberOfAttributeInPlus);
                    layout.addView(nameTextEdit);
                    set.connect(nameTextEdit.getId(), ConstraintSet.TOP, R.id.description_view, ConstraintSet.BOTTOM, 150);
                    set.connect(nameTextEdit.getId(), ConstraintSet.RIGHT, R.id.description_view, ConstraintSet.RIGHT, 0);
                    set.connect(nameTextEdit.getId(), ConstraintSet.LEFT, R.id.description_view, ConstraintSet.LEFT, 0);
                    set.constrainHeight(nameTextEdit.getId(), ConstraintSet.WRAP_CONTENT);

                    set.applyTo(layout);


                    //Value TextEdit:
                    numberOfAttributeInPlus = numberOfAttributeInPlus + 100;
                    valueTextEdit.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    valueTextEdit.setText(R.string.new_attribute_value);
                    valueTextEdit.setId(numberOfAttributeInPlus);
                    layout.addView(valueTextEdit);
                    set.connect(valueTextEdit.getId(), ConstraintSet.TOP, nameTextEdit.getId(), ConstraintSet.BOTTOM, 75);
                    set.connect(valueTextEdit.getId(), ConstraintSet.RIGHT, nameTextEdit.getId(), ConstraintSet.RIGHT, 0);
                    set.connect(valueTextEdit.getId(), ConstraintSet.LEFT, nameTextEdit.getId(), ConstraintSet.LEFT, 0);
                    set.constrainHeight(valueTextEdit.getId(), ConstraintSet.WRAP_CONTENT);

                    set.applyTo(layout);


                    //ri-set the constraint of button
                    set.connect(addAttributeButton.getId(), ConstraintSet.TOP, valueTextEdit.getId(), ConstraintSet.BOTTOM, 75);
                    set.connect(addAttributeButton.getId(), ConstraintSet.LEFT, valueTextEdit.getId(), ConstraintSet.LEFT, 0);
                    set.connect(addAttributeButton.getId(), ConstraintSet.RIGHT, valueTextEdit.getId(), ConstraintSet.RIGHT, 0);
                    set.applyTo(layout);
                    numberOfAttributeInPlus = numberOfAttributeInPlus + 100;
                }
                else{
                    nameTextEdit.setText(R.string.new_attribute_name);
                    nameTextEdit.setTextColor(getResources().getColor(android.R.color.darker_gray));

                    nameTextEdit.setId(numberOfAttributeInPlus);
                    layout.addView(nameTextEdit);
                    set.connect(nameTextEdit.getId(), ConstraintSet.TOP, numberOfAttributeInPlus-100, ConstraintSet.BOTTOM, 150);
                    set.connect(nameTextEdit.getId(), ConstraintSet.RIGHT, numberOfAttributeInPlus-100, ConstraintSet.RIGHT, 0);
                    set.connect(nameTextEdit.getId(), ConstraintSet.LEFT, numberOfAttributeInPlus-100, ConstraintSet.LEFT, 0);
                    set.constrainHeight(nameTextEdit.getId(), ConstraintSet.WRAP_CONTENT);

                    set.applyTo(layout);


                    //Value TextEdit:
                    numberOfAttributeInPlus = numberOfAttributeInPlus + 100;
                    valueTextEdit.setText(R.string.new_attribute_value);
                    valueTextEdit.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    valueTextEdit.setId(numberOfAttributeInPlus);
                    layout.addView(valueTextEdit);
                    set.connect(valueTextEdit.getId(), ConstraintSet.TOP, numberOfAttributeInPlus-100, ConstraintSet.BOTTOM, 75);
                    set.connect(valueTextEdit.getId(), ConstraintSet.RIGHT, numberOfAttributeInPlus-100, ConstraintSet.RIGHT, 0);
                    set.connect(valueTextEdit.getId(), ConstraintSet.LEFT, numberOfAttributeInPlus-100, ConstraintSet.LEFT, 0);
                    set.constrainHeight(valueTextEdit.getId(), ConstraintSet.WRAP_CONTENT);

                    set.applyTo(layout);


                    //ri-set the constraint of button
                    set.connect(addAttributeButton.getId(), ConstraintSet.TOP, numberOfAttributeInPlus, ConstraintSet.BOTTOM, 75);
                    set.connect(addAttributeButton.getId(), ConstraintSet.LEFT, numberOfAttributeInPlus, ConstraintSet.LEFT, 0);
                    set.connect(addAttributeButton.getId(), ConstraintSet.RIGHT, numberOfAttributeInPlus, ConstraintSet.RIGHT, 0);
                    set.applyTo(layout);
                    numberOfAttributeInPlus = numberOfAttributeInPlus + 100;
                }
                nameTextEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        nameTextEdit.setTextColor(getResources().getColor(android.R.color.white));
                        if(nameTextEdit.getText().toString().compareTo(getResources().getString(R.string.new_attribute_name))==0){
                            nameTextEdit.getText().clear();
                        }
                        else if(nameTextEdit.getText().length()==0){
                            nameTextEdit.setText(R.string.new_attribute_name);
                            nameTextEdit.setTextColor(getResources().getColor(android.R.color.darker_gray));
                        }
                    }
                });
                valueTextEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        valueTextEdit.setTextColor(getResources().getColor(android.R.color.white));
                        if(valueTextEdit.getText().toString().compareTo(getResources().getString(R.string.new_attribute_value))==0){
                            valueTextEdit.getText().clear();
                        }
                        else if(valueTextEdit.getText().length()==0){
                            valueTextEdit.setText(R.string.new_attribute_value);
                            valueTextEdit.setTextColor(getResources().getColor(android.R.color.darker_gray));
                        }
                    }
                });

            }
        });
    }

    /**
     * save all the stuff inserted in the activity and send an intent to SowCategoryActivity
     * NB: The database is missing so the product will not be save in real.
     */
    private void setSaveButton(){
        saveButton = (Button) findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            String name = null;
            String description = null;
            String price = null;
            String imageURL = null;
            Position position = null;
            Category category = null;
            Product finalProduct = null;
            @Override
            public void onClick(View v) {
                if(descriptionTextView.getText().toString().compareTo(getResources().getString(R.string.insert_description))!=0
                        &&descriptionTextView.getText().toString().length()!=0){
                    description = descriptionTextView.getText().toString();
                }
                if(priceTextView.getText().toString().compareTo(getResources().getString(R.string.insert_price))!=0
                        &&priceTextView.getText().toString().length()!=0){
                    price = priceTextView.getText().toString();
                }

                if(getAllPositionInString().compareTo("")!=0){
                    position = new RealPosition(getAllPositionInString());
                }
                if(imageUrl!=null){
                    imageURL = imageUrl;
                }
                if(numberOfAttributeInPlus!=100) {
                    for (int i = 100; i < numberOfAttributeInPlus; i = i + 200) {
                        EditText name = (EditText) findViewById(i);
                        EditText value = (EditText) findViewById(i + 100);
                        Attribute attribute = new AttributesInPlus(name.getText().toString(), value.getText().toString());
                        attributeInPlas.add(attribute);
                    }
                }
                if(spinnerListAdapter.getSelectedCategory()!=null){
                    category = spinnerListAdapter.getSelectedCategory();
                }

                if(nameTextView.getText().toString().compareTo(getResources().getString(R.string.insert_name))==0
                        ||nameTextView.getText().toString().length()==0){
                    Toast toast = Toast.makeText(getApplicationContext(),R.string.must_insert_product_name,Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    name = nameTextView.getText().toString();
                    if(spinnerListAdapter.getSelectedCategory()==null){
                        finalProduct = new RealProduct(stringBarcode,name);
                    }
                    else {
                        finalProduct = new RealProduct(stringBarcode, name);
                        if(description!=null){
                            finalProduct.setDescription(description);
                        }
                        if(price!=null){
                            finalProduct.setPrice(Float.parseFloat(price));
                        }
                        if(imageURL!=null){
                            finalProduct.setImageURL(imageURL);
                        }
                        if(position!=null){
                            finalProduct.setPosition(position);
                        }
                        if(attributeInPlas.isEmpty()==false) {
                            finalProduct.setAllAttribute(attributeInPlas);
                        }
                        if(category!=null){
                            finalProduct.setCattegory(category);
                        }
                    }
                    /**
                     * this is used to check if all the stuff are inserted well.
                     * this is the method to recive the informations from the product created
                     Log.i("product inserted",finalProduct.getName() + String.valueOf(finalProduct.getPrice()) + finalProduct.getCattegory().getName() +finalProduct.getBarcode()
                     + finalProduct.getDescription() + finalProduct.getImageURL());
                     for (Attribute currAtt:finalProduct.getNewAttributes()) {
                     Log.i("attribute",currAtt.getName() + currAtt.getValue());
                     }
                     for (Position currPos:finalProduct.getPosition()) {
                     Log.i("attribute",currPos.getName());
                     }
                     */
                    //TODO connettersi al database per salvare il prodotto creato
                    Intent intent = new Intent(getApplicationContext(),ShowCategoryActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    /**
     * this method is used to elaborate the position of the product that the user in typing
     * @return
     */
    private String getAllPositionInString(){
        String totPosition = "";
        if(checkCategory==null) {
            if (spinnerListAdapter.getSelectedCategory() != null) {
                Category categoryForPosition = spinnerListAdapter.getSelectedCategory();
                checkCategory = categoryForPosition;
                if (categoryForPosition.getPosition() != null) {
                    String catName = categoryForPosition.getPosition().getName();
                    totPosition = categoryForPosition.getPosition().getName();
                }
            }
        }
        else{
            if(checkCategory.getName().compareTo(spinnerListAdapter.getSelectedCategory().getName())==0){
                Category categoryForPosition = spinnerListAdapter.getSelectedCategory();
                if (categoryForPosition.getPosition() != null) {
                    String catName = categoryForPosition.getPosition().getName();
                    totPosition = categoryForPosition.getPosition().getName();
                }
            }
            else{
                Category categoryForPosition = spinnerListAdapter.getSelectedCategory();
                checkCategory = categoryForPosition;
                if (categoryForPosition.getPosition() != null) {
                    String catName = categoryForPosition.getPosition().getName();
                    totPosition = categoryForPosition.getPosition().getName();
                }
                Position lastPos = tempPosition.get(tempPosition.size()-1);
                tempPosition.clear();
                tempPosition.add(lastPos);
            }
        }
            for (Position currentPosition : tempPosition) {
                totPosition = totPosition + " - " + currentPosition.getName();
            }
        return totPosition;
    }


    /**
     * inner class that get the image from url string
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        public DownloadImageTask(){}

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    //maybe it became faster...
    private void loadAllImage(){
        for(int current = 1;current<=maxNumImage;current++){
            imageUrlUnpacker.setImageNum(current);
            String image_url = imageUrlUnpacker.getMyString();
            new DownloadImageTask()
                    .execute(image_url);
        }
    }
    //methods that send an intent to camera application and recive the extra as the image captured
    private void dispatchTakePictureIntent() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    CAMERA_CODE);
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
            else {
                //no permission no enjoy
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            imageSelected.setImageDrawable(getDrawable(R.drawable.ic_check_green_24dp));
            buttonPrev.setVisibility(View.GONE);
            buttonNext.setVisibility(View.GONE);
            photoButton.setVisibility(View.GONE);
        }
    }

    //for debug
    // in a future implementation there will be the database
    public ArrayList<Category> giveMeACategory(){
        ArrayList<Category> allCategory = new ArrayList<>();
        Category initaial = new RealCattegory("<none>");
        initaial.setPosition(null);
        allCategory.add(0,initaial);
        for(int i = 1;i<=10;i++){
            Category category = new RealCattegory("cattegoria " + i);
            category.setPosition(new RealPosition("pos" + i));
            allCategory.add(i,category);
        }
        Category catForCreate = new RealCattegory("crea nuova cattegoria");
        allCategory.add(11,catForCreate);
        return allCategory;
    }

}
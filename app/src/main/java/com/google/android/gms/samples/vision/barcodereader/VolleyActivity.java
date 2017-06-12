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
    private int maxNumImage;
    boolean isClicked = false;
    public ArrayList<Position> tempPosition = new ArrayList<>();
    public ArrayList<Attribute> attributeInPlas = new ArrayList<>();
    private int numberOfAttributeInPlus = 100;
    private SpinnerListAdapter spinnerListAdapter;
    private Category checkCategory;
    private String imageUrl;
    private String stringBarcode;

    ImageUrlUnpacker imageUrlUnpacker = new ImageUrlUnpacker();

    static final int REQUEST_IMAGE_CAPTURE = 1;



    // constants used to pass extra data in the intent
    public static final String initialYahooURL = "https://it.images.search.yahoo.com/search/images;_ylt=A9mSs3TQLxBZDrgATj0bDQx.;_ylu=X3oDMTB0ZTgxN3Q0BGNvbG8DaXIyBHBvcwMxBHZ0aWQDBHNlYwNwaXZz?p=";
    public static final String finalYahooURL = "&fr=yfp-t-909&fr2=piv-web";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_volley);

        barcodeView = (TextView) findViewById(R.id.barcode_view);
        mImageView = (ImageView) findViewById(R.id.imageView);
        buttonPrev = (ImageButton) findViewById(R.id.button_prev);
        buttonNext = (ImageButton) findViewById(R.id.button_next);
        photoButton = (ImageButton) findViewById(R.id.photoButton);
        imageSelected = (ImageButton) findViewById(R.id.saveImageButton);
        nameTextView = (EditText) findViewById(R.id.name_view);
        priceTextView = (EditText) findViewById(R.id.price_view);
        descriptionTextView = (EditText) findViewById(R.id.description_view);
        positionTextView = (EditText) findViewById(R.id.position_view);
        addPosButton = (Button) findViewById(R.id.add_pos_button);
        showAllPositionsTextView = (TextView) findViewById(R.id.show_positions_view);
        addAttributeButton = (Button) findViewById(R.id.button_add_attribute);
        saveButton = (Button) findViewById(R.id.button_save);

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
        /**
         * set the initial value of the text ho explain the user how the text view is used for
         */
        nameTextView.setText(R.string.insert_name);
        nameTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        descriptionTextView.setText(R.string.insert_description);
        descriptionTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        priceTextView.setText(R.string.insert_price);
        priceTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        positionTextView.setText(R.string.insert_new_place);
        positionTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
        showAllPositionsTextView.setText("");
        showAllPositionsTextView.setTextColor(getResources().getColor(android.R.color.holo_green_light));



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

        addPosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(positionTextView.getText().toString().compareTo(getResources().getString(R.string.insert_new_place))!=0) {
                    Position position = new RealPosition(positionTextView.getText().toString());
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
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),R.string.position_allert,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

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

        /**
         * this Button Allowd to insert programmatically two EditText witch the user will insert
         * the name and the values of the new attribute
         */
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




 // THE PART OF THE CODE BELOW IS USED FOR SET THE ACTIVITY WITH THE BARCODE READER
         Intent intent = getIntent();
         Barcode barcode = intent.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

        //recive the intent from BarcodeCaptureActivity who sent the barcode number
       // barcodeView.setText(barcode.displayValue);

        final String url = initialYahooURL + barcode.displayValue + finalYahooURL;

        //String url ="https://it.images.search.yahoo.com/search/images;_ylt=A9mSs3TQLxBZDrgATj0bDQx.;_ylu=X3oDMTB0ZTgxN3Q0BGNvbG8DaXIyBHBvcwMxBHZ0aWQDBHNlYwNwaXZz?p=8001435500013&fr=yfp-t-909&fr2=piv-web";
        //stringBarcode = "8001435500013";
        stringBarcode = barcode.displayValue;
        barcodeView.setText(stringBarcode);


        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //saving response for pircing
                        imageUrlUnpacker.setResponce(response);
                        String image_url = imageUrlUnpacker.getMyString();
                        if(image_url.compareTo("error")!=0) {
                            new DownloadImageTask((ImageView) mImageView)
                                    .execute(image_url);
                            maxNumImage = imageUrlUnpacker.countImageOnUrl();
                        }
                        else{
                            //TODO: open a dialog in order to ask at user if he want to use a camera to take a picture about the element
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                barcodeView.setText("Internet connection is missing!");
                imageUrlUnpacker.setResponce("That didn't work!");
            }
        });//
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

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

        final Drawable drawable_saved = getDrawable(R.drawable.ic_check_green_24dp);
        final Drawable drawable_not_saved = imageSelected.getDrawable();
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

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();
            }
        });
        loadAllImage();

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
                    Toast toast = Toast.makeText(getApplicationContext(),"you must insert a category name",Toast.LENGTH_SHORT);
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
                     * this is the method to retcive the informations from the product created
                    Log.i("product inserted",finalProduct.getName() + String.valueOf(finalProduct.getPrice()) + finalProduct.getCattegory().getName() +finalProduct.getBarcode()
                    + finalProduct.getDescription() + finalProduct.getImageURL());
                    for (Attribute currAtt:finalProduct.getNewAttributes()) {
                        Log.i("attribute",currAtt.getName() + currAtt.getValue());
                    }
                    for (Position currPos:finalProduct.getPosition()) {
                        Log.i("attribute",currPos.getName());
                    }
                     */
                    //TODO connettersi al database per salvare il prodotto creatoz
                    Intent intent = new Intent(getApplicationContext(),ShowCategoryActivity.class);
                    startActivity(intent);
                }

            }
        });


    }

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
        for (Position currentPosition:tempPosition) {
            totPosition = totPosition + " - "  + currentPosition.getName();
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
                    73883);
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 73883) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
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
    public ArrayList<Category> giveMeACategory(){
        ArrayList<Category> allCategory = new ArrayList<>();
        Category initaial = new RealCattegory("<none>");
        allCategory.add(initaial);
        for(int i = 1;i<=10;i++){
            Category category = new RealCattegory("cattegoria " + i);
            category.setPosition(new RealPosition("pos" + i));
            allCategory.add(category);
        }
        Category catForCreate = new RealCattegory("crea nuova cattegoria");
        allCategory.add(catForCreate);
        return allCategory;
    }

}
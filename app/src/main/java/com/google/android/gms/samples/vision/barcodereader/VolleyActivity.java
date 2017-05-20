package com.google.android.gms.samples.vision.barcodereader;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.InputStream;


public class VolleyActivity extends AppCompatActivity {

    TextView mTextView;
    ImageView mImageView;
    ImageButton buttonPrev;
    ImageButton buttonNext;
    ImageButton photoButton;
    ImageButton imageSelected;

    private int numPhotoSelected;
    private int maxNumImage;
    boolean isClicked = false;

    ImageUrlUnpacker imageUrlUnpacker = new ImageUrlUnpacker();

    static final int REQUEST_IMAGE_CAPTURE = 1;



    // constants used to pass extra data in the intent
    public static final String initialYahooURL = "https://it.images.search.yahoo.com/search/images;_ylt=A9mSs3TQLxBZDrgATj0bDQx.;_ylu=X3oDMTB0ZTgxN3Q0BGNvbG8DaXIyBHBvcwMxBHZ0aWQDBHNlYwNwaXZz?p=";
    public static final String finalYahooURL = "&fr=yfp-t-909&fr2=piv-web";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        mTextView = (TextView) findViewById(R.id.textNumber);
        mImageView = (ImageView) findViewById(R.id.imageView);
        buttonPrev = (ImageButton) findViewById(R.id.button_prev);
        buttonNext = (ImageButton) findViewById(R.id.button_next);
        photoButton = (ImageButton) findViewById(R.id.photoButton);
        imageSelected = (ImageButton) findViewById(R.id.saveImageButton);//

/**
        // THE PART OF THE CODE BELOW IS USED FOR SET THE ACTIVITY WITH THE BARCODE READER
         Intent intent = getIntent();
         Barcode barcode = intent.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

        //recive the intent from BarcodeCaptureActivity who sent the barcode number
        mTextView.setText(barcode.displayValue);

        final String url = initialYahooURL + barcode.displayValue + finalYahooURL;
*/
        String url ="https://it.images.search.yahoo.com/search/images;_ylt=A9mSs3TQLxBZDrgATj0bDQx.;_ylu=X3oDMTB0ZTgxN3Q0BGNvbG8DaXIyBHBvcwMxBHZ0aWQDBHNlYwNwaXZz?p=8001435500013&fr=yfp-t-909&fr2=piv-web";
        mTextView.setText("8001435500013");


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
                mTextView.setText("That didn't work!");
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

    //class that send an intent to camera application and recive the extra as the image captured
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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

}
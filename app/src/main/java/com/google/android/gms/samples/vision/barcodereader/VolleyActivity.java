package com.google.android.gms.samples.vision.barcodereader;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;


public class VolleyActivity extends AppCompatActivity {

    TextView mTextView;
    ImageView mImageView;
    ImageButton buttonPrev;
    ImageButton buttonNext;

    private int numPhotoSelected;
    private int maxNumImage;

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
        Intent intent = getIntent();
        Barcode barcode = intent.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

        //recive the intent from BarcodeCaptureActivity who sent the barcode number
        mTextView.setText(barcode.displayValue);

        //String url ="https://it.images.search.yahoo.com/search/images;_ylt=A9mSs3TQLxBZDrgATj0bDQx.;_ylu=X3oDMTB0ZTgxN3Q0BGNvbG8DaXIyBHBvcwMxBHZ0aWQDBHNlYwNwaXZz?p=8001435500013&fr=yfp-t-909&fr2=piv-web";
        final String url = initialYahooURL + barcode.displayValue + finalYahooURL;
        final ImageUrlUnpacker imageUrlUnpacker = new ImageUrlUnpacker();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //saving response for pircing
                        imageUrlUnpacker.setResponce(response);
                        String image_url = imageUrlUnpacker.getMyString();
                        new DownloadImageTask((ImageView) mImageView)
                                .execute(image_url);
                        maxNumImage = imageUrlUnpacker.countImageOnUrl();
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
    }

    /**
     * inner class that get the image from url string
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

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

}
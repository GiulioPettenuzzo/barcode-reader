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

    private int numPhotoSelected = 1;
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

        makeVolleyRequestForImageLoader(url,numPhotoSelected);


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numPhotoSelected++;
                if(numPhotoSelected==maxNumImage){
                    numPhotoSelected=1;
                }
                else {
                    numPhotoSelected++;
                }
                makeVolleyRequestForImageLoader(url,numPhotoSelected);
            }
        });
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numPhotoSelected--;
                if(numPhotoSelected==0){
                    numPhotoSelected = maxNumImage-2;
                }
                else {
                    numPhotoSelected--;
                }
                makeVolleyRequestForImageLoader(url,numPhotoSelected);
            }//
        });


    }

    /**
     * this class make a Volley Request to the url given in param,
     * look at all url in the responce and select the "num" one give in param using getImageURL method
     * and load the corrisponding image using DownloadImageTask class
     * @param url
     * @param num
     */
    private void makeVolleyRequestForImageLoader(String url, final int num){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String image_url = getImageURL(response,num);

                        new DownloadImageTask((ImageView) mImageView)
                                .execute(image_url);
                        maxNumImage = countImageOnUrl(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });//
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    /**
     * this class is able to make a parcing of the response to find out the first url witch contains the
     * first image in yahoo image
     * the url I'm searching for is contained into a string like this:
     * ...<\>img src='...IMAGE URL...' ...
     * my http is: https://tse2.mm.bing.net/th?id=OIP.qnErHRm9_ZRCmpR1gMwq-gB2Es&pid=15.1&H=160 &W=62&P=0' alt='' style='width:60px;height:153.6px;
     *
     * @return the url of the first image on yahoo, it return "error" if no url was found
     */

    public String getImageURL(String response,int num) {
        StringTokenizer token = new StringTokenizer(response);
        String first_word = "";//the previus of the correct string
        String second_word = "";    //the correct string
        String imgURL = "https://";
        boolean foundURL = false;
        int current = 0; //this counter is used to switch the image
        while (token.hasMoreTokens() && foundURL == false) {
            first_word = token.nextToken();
            if (first_word.endsWith("img")) {  //...<\>img
                second_word = token.nextToken();
                if (second_word.startsWith("src")) {   //src=
                    //by here starting to extract http url
                    current++;
                    if (current == num) {


                        char[] charArray = second_word.toCharArray();
                        charArray = giveURLFromTheBeginning(charArray);
                        imgURL = imgURL + String.valueOf(charArray);
                        String restPartOfURL = token.nextToken();
                        //somewhere in the url there are some space, them are there just for html sintax,
                        // to obtain the real url them must be replaced with "%20"
                        while (!restPartOfURL.startsWith("/>")) {
                            imgURL = imgURL + "%20" + restPartOfURL;
                            restPartOfURL = token.nextToken();
                        }
                        char[] finalCharArray = imgURL.toCharArray();
                        finalCharArray = remuveLastCharacterOfURL(finalCharArray);
                        imgURL = String.valueOf(finalCharArray);

                        foundURL = true;

                    }
                }
            }
        }
        if (foundURL == false) {
            return "error";
        }
        return imgURL;
    }

    /**
     * this class recognize the first part of url without https://
     * @param charArray
     * @return
     */
    private char[] giveURLFromTheBeginning(char[] charArray){
        char[] urlFromTheBeginning = new char[charArray.length];
        for(int currentCaracter = 0;currentCaracter<charArray.length;currentCaracter++){
            //jump after https://
            if(charArray[currentCaracter]=='h'&&charArray[currentCaracter+1]=='t'&&charArray[currentCaracter+2]=='t'
                    &&charArray[currentCaracter+3]=='p' &&charArray[currentCaracter+4]=='s'&&charArray[currentCaracter+5]==':'
                    &&charArray[currentCaracter+6]=='/'&&charArray[currentCaracter+7]=='/'){
                currentCaracter = currentCaracter+8;
                int i = 0;
                urlFromTheBeginning = new char[charArray.length-currentCaracter];
                while(currentCaracter<charArray.length){
                    urlFromTheBeginning[i] = charArray[currentCaracter];
                    currentCaracter++;
                    i++;
                }

            }
        }
        return urlFromTheBeginning;
    }

    /**
     * in the html page, the url is contains inside '...', you need to remuve the last '
     * @param charArray
     * @return
     */
    private char[] remuveLastCharacterOfURL(char[] charArray){
        char[] urlWithoutLastCharacter = new char[charArray.length-1];
        for(int current = 0; current<charArray.length-1;current++){
            urlWithoutLastCharacter[current] = charArray[current];
        }
        return urlWithoutLastCharacter;
    }

    /**
     * class uses to know how many images are there in the responce of yahoo image
     * @param responce
     * @return
     */
    private int countImageOnUrl(String responce){
        StringTokenizer token = new StringTokenizer(responce);
        String first_word = "";//the previus of the correct string
        String second_word = "";    //the correct string
        int count = 0;
        while (token.hasMoreTokens()) {
            first_word = token.nextToken();
            if (first_word.endsWith("img")) {  //...<\>img
                second_word = token.nextToken();
                if (second_word.startsWith("src")) {   //src=
                    count++;
                }
            }
        }
        return count;
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
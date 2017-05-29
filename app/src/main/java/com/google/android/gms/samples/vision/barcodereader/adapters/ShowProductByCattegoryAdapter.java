package com.google.android.gms.samples.vision.barcodereader.adapters;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.ShowProductByCattegoryActivity;
import com.google.android.gms.samples.vision.barcodereader.adapters.helpers.ItemTouchHelperAdapter;
import com.google.android.gms.samples.vision.barcodereader.entities.Attribute;
import com.google.android.gms.samples.vision.barcodereader.entities.Position;
import com.google.android.gms.samples.vision.barcodereader.entities.Product;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by giuliopettenuzzo on 17/05/17.
 */

public class ShowProductByCattegoryAdapter extends RecyclerView.Adapter<ShowProductByCattegoryAdapter.ViewHolder> implements ItemTouchHelperAdapter {


    private static Context context;
    protected ArrayList<Product> listOfProduct;
    public ArrayList<Product> selectedProduct = new ArrayList<>();
    private static int defaultHeight = 162;


    public ShowProductByCattegoryAdapter(Context context, ArrayList<Product> listOfProduct){
        this.context = context;
        this.listOfProduct = listOfProduct;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewOfRecycle = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_product_by_cattegory_item,parent,false);
        //viewOfRecycle.setBackgroundColor(808080);
        ViewHolder viewHolder = new ViewHolder(viewOfRecycle,(TextView)viewOfRecycle.findViewById(R.id.product_name),
                (TextView)viewOfRecycle.findViewById(R.id.product_number),(ImageView)viewOfRecycle.findViewById(R.id.product_image));
        // viewHolder.expandedView = (TextView)viewOfRecycle.findViewById(R.id.price_text_view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = listOfProduct.get(position);
        holder.productName.setText(product.getName());
       // Category category = product.getCattegory();
       // int num = category.getNumberOfEqualsProduct(product.getBarcode());
        //String numberOfProduct = String.valueOf(num);
       // holder.productNumber.setText(numberOfProduct);
        ArrayList<Position> pos = product.getPosition();
        String positionInString = "";
        for (Position currentpos:pos) {
            String stringpos = currentpos.getName();
            if(positionInString.isEmpty()){
                positionInString = stringpos;
            }
            else {
                positionInString = positionInString + "-" + stringpos;
            }
        }
        holder.productNumber.setText(positionInString);
        holder.barcodeView.setText(product.getBarcode());
        holder.descriptionView.setText(product.getDescription());
        holder.priceView.setText(String.valueOf(product.getPrice())+" euro");
        String totAttributeInString = "";
        for (Attribute currentAttribute:product.getNewAttributes()) {
            String oneAttribute = currentAttribute.getName() + "   " + currentAttribute.getValue() + "\n";
            totAttributeInString = totAttributeInString + oneAttribute;
        }
        holder.otherAttributes.setText(totAttributeInString);
        String url = product.getImageURL();
        new DownloadImageTask((ImageView) holder.productImage)
                .execute(url);
        if (selectedProduct.lastIndexOf(product)<0) {
             //Set Views to View.GONE and .setEnabled(false)
            holder.priceView.setVisibility(View.GONE);
            holder.priceView.setEnabled(false);
            holder.barcodeView.setVisibility(View.GONE);
            holder.barcodeView.setEnabled(false);
            holder.descriptionView.setVisibility(View.GONE);
            holder.descriptionView.setEnabled(false);
            holder.otherAttributes.setVisibility(View.GONE);
            holder.otherAttributes.setEnabled(false);
            holder.isViewExpanded=false;
            holder.itemView.setElevation(14);
            if(holder.itemView.getResources().getConfiguration().orientation==2){
                holder.itemView.getLayoutParams().height = defaultHeight-30;
            }
            else {
                holder.itemView.getLayoutParams().height = defaultHeight;
            }
            holder.itemView.requestLayout();

        }
        else{
            holder.priceView.setVisibility(View.VISIBLE);
            holder.priceView.setEnabled(true);
            holder.barcodeView.setVisibility(View.VISIBLE);
            holder.barcodeView.setEnabled(true);
            holder.descriptionView.setVisibility(View.VISIBLE);
            holder.descriptionView.setEnabled(true);
            holder.otherAttributes.setVisibility(View.VISIBLE);
            holder.otherAttributes.setEnabled(true);
            holder.itemView.setElevation(50);
            holder.isViewExpanded=true;
            //holder.itemView.getLayoutParams().height = defaultHeight*3;
            /**
             * if the orienttion is lanscape you must regolate the layout param height
             */
            if(holder.itemView.getResources().getConfiguration().orientation==2&&product.getNewAttributes().isEmpty()==false){
                int height = defaultHeight*3;
                holder.itemView.getLayoutParams().height = height;
            }
            else if(holder.itemView.getResources().getConfiguration().orientation==1&&product.getNewAttributes().size()>1){
                int attributeSpace = defaultHeight/4;//each attributes in plus needs this space
                int height = defaultHeight*3 + attributeSpace*product.getNewAttributes().size();
                holder.itemView.getLayoutParams().height = height;
            }
            holder.itemView.requestLayout();

        }
        holder.itemView.setOnClickListener(holder);

    }

    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);

        //when you scroll up the list, the listener of the new item is disactivate
        //so they will set in the right settings
        //holder.itemView.setOnClickListener(null);

    }

    public void addProduct(Product product){
        listOfProduct.add(product);
    }
    public void removeProduct(Product product){
        listOfProduct.remove(product);
    }

    @Override
    public int getItemCount() {
        return listOfProduct.size();
    }

    protected ArrayList<Product> getListOfProduct(){
        return listOfProduct;
    }

    @Override
    public void onItemSwiped(final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.delete_product_dialog);
        Button cancel = (Button) dialog.findViewById(R.id.button_ok);
        Button dontCancel = (Button) dialog.findViewById(R.id.button_no);
        final int[] i = {0};

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeProduct(listOfProduct.get(position));
                notifyDataSetChanged();
                dialog.cancel();
                i[0] =1;
            }
        });
        dontCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(position);
                dialog.cancel();
                i[0] = 1;
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView productName;
        TextView productNumber;
        ImageView productImage;
        View itemView;

        //the item of the view expanded
        private int originalHeight = 0;
        private boolean isViewExpanded = false;
        private TextView priceView = new TextView(context);
        private TextView barcodeView = new TextView(context);
        private TextView descriptionView = new TextView(context);
        private TextView otherAttributes = new TextView(context);

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(View itemView, TextView productName, TextView productNumber, ImageView productImage) {
            super(itemView);
            this.itemView = itemView;
            this.productName = productName;
            this.productNumber = productNumber;
            this.productImage = productImage;


            priceView = (TextView)itemView.findViewById(R.id.price_text_view);
            barcodeView = (TextView)itemView.findViewById(R.id.barcode_text_view);
            descriptionView = (TextView)itemView.findViewById(R.id.description_text_view);
            otherAttributes = (TextView)itemView.findViewById(R.id.otherAttributes_text_view);


            if (isViewExpanded == false) {
                // Set Views to View.GONE and .setEnabled(false)
                priceView.setVisibility(View.GONE);
                priceView.setEnabled(false);
                barcodeView.setVisibility(View.GONE);
                barcodeView.setEnabled(false);
                descriptionView.setVisibility(View.GONE);
                descriptionView.setEnabled(false);
                otherAttributes.setVisibility(View.GONE);
                otherAttributes.setEnabled(false);
                itemView.setElevation(14);
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onClick(final View view){

            if (originalHeight == 0) {
                originalHeight = ShowProductByCattegoryAdapter.this.defaultHeight;
            }
            ValueAnimator valueAnimator;
            if(isViewExpanded==false){
                priceView.setVisibility(View.VISIBLE);
                priceView.setEnabled(true);
                barcodeView.setVisibility(View.VISIBLE);
                barcodeView.setEnabled(true);
                descriptionView.setVisibility(View.VISIBLE);
                descriptionView.setEnabled(true);
                otherAttributes.setVisibility(View.VISIBLE);
                otherAttributes.setEnabled(true);
                isViewExpanded = true;
                valueAnimator = ValueAnimator.ofInt(originalHeight, originalHeight + (int) (originalHeight * 2.0)); // These values in this method can be changed to expand however much you like
                view.setElevation(50);
                //originalHeight = originalHeight*3;

                Product product = ShowProductByCattegoryAdapter.this.listOfProduct.get(super.getAdapterPosition());
                ShowProductByCattegoryAdapter.this.selectedProduct.add(product);

            }
            else{
                isViewExpanded = false;

                Product product = ShowProductByCattegoryAdapter.this.listOfProduct.get(super.getAdapterPosition());
                ShowProductByCattegoryAdapter.this.selectedProduct.remove(product);

                if(itemView.getResources().getConfiguration().orientation==2){
                    valueAnimator = ValueAnimator.ofInt(originalHeight + (int) (originalHeight * 2.0), originalHeight-30);
                }
                else {
                    valueAnimator = ValueAnimator.ofInt(originalHeight + (int) (originalHeight * 2.0), originalHeight);
                }
                Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out
                a.setDuration(300);
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        priceView.setVisibility(View.GONE);
                        priceView.setEnabled(false);
                        barcodeView.setVisibility(View.GONE);
                        barcodeView.setEnabled(false);
                        descriptionView.setVisibility(View.GONE);
                        descriptionView.setEnabled(false);
                        otherAttributes.setVisibility(View.GONE);
                        otherAttributes.setEnabled(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                priceView.startAnimation(a);
                barcodeView.startAnimation(a);
                descriptionView.startAnimation(a);
                otherAttributes.startAnimation(a);
                view.setElevation(14);
            }
            valueAnimator.setDuration(300);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    view.getLayoutParams().height = value.intValue();
                    view.requestLayout();
                }
            });
            valueAnimator.start();
        }

    }

}

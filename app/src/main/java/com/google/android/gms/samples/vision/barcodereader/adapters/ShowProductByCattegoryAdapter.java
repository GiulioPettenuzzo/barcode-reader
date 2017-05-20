package com.google.android.gms.samples.vision.barcodereader.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.adapters.helpers.ItemTouchHelperAdapter;
import com.google.android.gms.samples.vision.barcodereader.entities.Position;
import com.google.android.gms.samples.vision.barcodereader.entities.Product;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by giuliopettenuzzo on 17/05/17.
 */

public class ShowProductByCattegoryAdapter extends RecyclerView.Adapter<ShowProductByCattegoryAdapter.ViewHolder> implements ItemTouchHelperAdapter {


    private Context context;
    private ArrayList<Product> listOfProduct;


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
        return viewHolder;
    }

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
        String url = product.getImageURL();
        new DownloadImageTask((ImageView) holder.productImage)
                .execute(url);

    }


    @Override
    public int getItemCount() {
        return listOfProduct.size();
    }



    @Override
    public void onItemSwiped(int position) {
        Toast toast = Toast.makeText(context,"item swiped", Toast.LENGTH_SHORT);
        toast.show();
        notifyItemChanged(position);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        TextView productNumber;
        ImageView productImage;
        View itemView;

        public ViewHolder(View itemView,TextView productName,TextView productNumber,ImageView productImage) {
            super(itemView);
            this.itemView = itemView;
            this.productName = productName;
            this.productNumber = productNumber;
            this.productImage = productImage;
        }
    }
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

}

package com.example.loresflickr;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LoremAdapter extends ArrayAdapter<LoremObject> {

    private Context context;
    private LayoutInflater inflater;
    private int item_layout;
    //private final RequestQueue requestQueue;
    //private final ImageLoader imageLoader;

    public LoremAdapter(@NonNull Context context, int resource, @NonNull ArrayList<LoremObject> objects) {
        super(context, resource, objects);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        item_layout = resource;
        this.context = context;

        // requestQueue = Volley.newRequestQueue(context);

        /* without cache
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });*/

        /* with cache
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

         */
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final LoremObject loremObject = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(item_layout, parent, false);
        }

        TextView tv_owner = convertView.findViewById(R.id.textView_owner);
        TextView tv_license = convertView.findViewById(R.id.textView_license);

        tv_owner.setText(loremObject.owner);
        tv_license.setText(loremObject.license);

        ImageView imageView = convertView.findViewById(R.id.imageView);
        Picasso.get()
                .load(loremObject.file)
                .placeholder(android.R.drawable.picture_frame)
                .error(android.R.drawable.stat_notify_error)
                .noFade()
                .fit()
                .into(imageView);
/*
        NetworkImageView networkImageView = convertView.findViewById(R.id.imageView);
        //       imageLoader.get(loremObject.file, ImageLoader.getImageListener(networkImageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        networkImageView.setImageUrl(loremObject.file, imageLoader);
*/
        return convertView;
    }
}

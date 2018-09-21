package com.example.bhavin.anroidstudiophotos68;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class PhotoAdapter extends ArrayAdapter

{
    private final Context context;
    private final ArrayList<Photo> data_list;


    public PhotoAdapter(@NonNull Context context, ArrayList<Photo> data) {
        super(context, R.layout.grid_layout, data);
        this.context = context;
        this.data_list = data;

    }



    private class ViewHolder {
        ImageView iv;
        TextView tv;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //ViewHolder holder;
        if (convertView == null){
            LayoutInflater i = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //convertView = i.inflate(R.layout.grid_layout, parent, false);
            convertView = i.inflate(R.layout.grid_layout, null);
            /*holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.image);
            holder.tv = (TextView) convertView.findViewById(R.id.caption);
            convertView.setTag(holder);*/
        }

        //Photo rowItem = (Photo) data_list.get(position);
        /*holder.tv.setText(rowItem.getCaption());*/
       // Bitmap mybitmap = BitmapFactory.decodeFile(String.valueOf(data_list.get(position)));
        //holder.iv.setImageBitmap(rowItem.getImage());
        //holder.iv.setImageBitmap(mybitmap);

        //holder.iv.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(rowItem.getUrl()),100,100));

        ImageView iv = (ImageView) convertView.findViewById(R.id.image);
        TextView tv = (TextView) convertView.findViewById(R.id.caption);
        Photo photo = (Photo) data_list.get(position);
        //iv.setImageURI(Uri.parse(String.valueOf(data_list.get(position))));
        iv.setImageBitmap(photo.getImage());


        //iv.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(photo.getUrl()),50,50));
        tv.setText(photo.getCaption());


        return convertView;
    }
}
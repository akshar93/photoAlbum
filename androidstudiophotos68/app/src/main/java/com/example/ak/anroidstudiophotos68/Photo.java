package com.example.bhavin.anroidstudiophotos68;
/*
 * @author Bhavin Patel
 * @author Akshar Patel
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Photo implements Serializable
{
    private static final long serialVersionUID = 1L;

    String url;
    String date;
    String caption;
    ArrayList<TagIdentity> tag;

    transient Bitmap image;
    /*
     * const take path url of the photo and initialize array list of tag identity
     */
    public Photo(String link)
    {
        url = link;
        caption = url;
        tag = new ArrayList<TagIdentity>();

    }

    /*
     * return url
     */
    public String getUrl()
    {
        return url;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return this.url;
    }

    /*
     * to check if empty
     */
    public boolean isEmpty()
    {
        return url.length() == 0;
    }

    /*
     * get copy of the photo
     */
    public Photo getCopy()
    {
        Photo pt = new Photo(this.getUrl());
        return pt;
    }

    /*
     * get date of the photo
     */
    public String getDate()
    {
        return date;
    }

    /*
     * get caption of the pic
     */
    public String getCaption()
    {
        return caption;
    }

    /*
     * set the caption
     */
    public void setCaption(String caption)
    {
        this.caption = caption;

    }

    /*
     * get tag name and value
     */
    public ArrayList<TagIdentity> getTag()
    {
        return tag;
    }

    /*
     * set tag name and value
     */
    public void setTag(ArrayList<TagIdentity> tag)
    {
        this.tag = tag;
        //dateHasChanged();
        return;
    }

    /*
     * add tag
     */
    public void addTag(TagIdentity tg)
    {
        tag.add(tg);
        //dateHasChanged();
    }
    /*
     * delete tag
     */
    public void deleteTag(TagIdentity tg)
    {
        tag.remove(tg);
    }
    /*
     * compare to method to compare two objects
     */
    public int compareTo(Object o)
    {
        if(o == null || !(o instanceof Photo))
        {
            return -1;
        }
        Photo p = (Photo)o;
        return url.compareTo(p.getUrl());
    }
    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        int b;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        while((b = ois.read()) != -1)
            byteStream.write(b);
        byte bitmapBytes[] = byteStream.toByteArray();
        image = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
    }

    /**
     * Method which will be used to serialize the user's photos. The image's pixels are serialized and stored onto disk.
     * @param oos
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        if(image != null){
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
            byte bitmapBytes[] = byteStream.toByteArray();
            oos.write(bitmapBytes, 0, bitmapBytes.length);
        }
    }
    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
        if (o == null || !( o instanceof Photo))
        {
            return false;
        }
        Photo object2 = (Photo)(o);
        return object2.url.equalsIgnoreCase(this.url);
    }

}


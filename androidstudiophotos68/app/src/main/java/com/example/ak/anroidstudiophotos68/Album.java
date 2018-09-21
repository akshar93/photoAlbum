package com.example.bhavin.anroidstudiophotos68;

/*
 * @author Bhavin Patel
 * @author Akshar Patel
 */

import java.io.Serializable;
import java.util.ArrayList;

public class Album extends ArrayList implements Serializable, Comparable<Album>
{
    private static final long serialVersionUID = 1L;

    String albumName ;
    ArrayList<Photo> photo;

    ArrayList<String> album = new ArrayList<String>();


    int totalPics;

    /*
     * const which takes album name and date and set the total pics to 0 initially
     */
    public Album(String albumName)
    {
        photo = new ArrayList<Photo>();

        this.albumName = albumName;
        this.totalPics = 0;

    }
    /*
     * return total number of pics
     */
    public int getTotalPics()
    {
		/*System.out.println("photo"+photo.size());
		if(photo.size() == 0)
		{
			return 0;
		}
		System.out.println(totalPics);*/
        return this.totalPics;
    }

    /*
     * set number of pics
     */
    public void settotalPics(int totalPics)
    {
        //this.totalPics = photo.size();
        this.totalPics = totalPics;
    }

    /*
     * returns album name
     */
    public String getAlbumName()
    {
        return albumName;
    }


    /*
     * set album name
     */
    public void setAlbumName(String albumName)
    {
        this.albumName = albumName;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return this.albumName;
    }

    /*
     * returns photo
     */
    public ArrayList<Photo> getPhotos()
    {
        return photo;
    }

    /*
     * add photo
     */
    public void addPhoto(Photo obj)
    {
        photo.add(obj);
        totalPics++;
    }



    public void addAlbum(String albumToAdd)
    {
        album.add(albumToAdd);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
        if (o == null || !( o instanceof Album))
        {
            return false;
        }
        Album object2 = (Album)(o);
        return object2.albumName.equalsIgnoreCase(this.albumName);
    }
    /*
     * just to check if album has duplicate photo
     */
    public boolean hasPhoto(Photo obj)
    {
        int s = this.photo.size();
        if(this.photo == null || s == 0)
        {
            return false;
        }
        for(int i=0;i<s;i++)
        {
            if(obj.compareTo(this.photo.get(i))==0)
            {
                return true;
            }
        }
        return false;
    }
    @Override
    public int compareTo(Album arg0) {
        if (this.albumName.equalsIgnoreCase(arg0.albumName))
        {
            return 0;
        }
        return this.albumName.compareTo(arg0.albumName);
    }

}


package com.example.bhavin.anroidstudiophotos68;
import android.content.Context;
import android.support.annotation.Nullable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Save implements Serializable
{
    private static final long serialVersionUID = 0L;
    public static final String fileName = "photoalbum.srl";
    public ArrayList<Album> albums;
    public static Album searchResults = new Album("results");

    @Nullable
    public static Save loadFromDisk(Context context){
        Save pa = null;
        try {
            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            pa = (Save) ois.readObject();

            if (pa.albums == null) {
                pa.albums = new ArrayList<Album>();
            }
            fis.close();
            ois.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
        return pa;
    }

    public void saveToDisk(Context context){
        ObjectOutputStream oos;
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

package com.example.bhavin.anroidstudiophotos68;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class openAlbum extends AppCompatActivity
{

    static final int PICK_IMAGE = 1;
    private GridView gridView;
    Album currentAlbum;
    Photo selectedPhoto;
    final Context context = this;
    TextView toolbarTitle;
    int selection;
    String album_name;

    ArrayList<Album> list = new ArrayList<Album>();
    ArrayList<Photo> photo_list = new ArrayList<>();
    private PhotoAdapter adapter;

    ArrayList<Album> movePhoto = new ArrayList<Album>();

    int totalPics;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_album);

        try {

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(new File(getFilesDir(),"")+File.separator + "photoalbum.ser" )));
            list = (ArrayList<Album>) ois.readObject();

            ois.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        Bundle bundle = getIntent().getExtras();
        selection  = bundle.getInt("index");
        currentAlbum = list.get(selection);
        album_name = bundle.getString("name");

        for(Album check : list)
        {
            if(check.getAlbumName().equalsIgnoreCase(currentAlbum.getAlbumName()))
            {
                photo_list = check.getPhotos();
                break;
            }
        }
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new PhotoAdapter(this,photo_list);
        gridView.setAdapter(adapter);

        movePhoto = MainActivity.list;

        totalPics = photo_list.size();
        currentAlbum.settotalPics(totalPics);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Album: "+ currentAlbum.getAlbumName()+"  -  "+ currentAlbum.getTotalPics()+" photo(s)");
        setSupportActionBar(toolbar);
        //toolbar.inflateMenu(R.menu.album_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savingToDisk();
                Intent intent = new Intent(openAlbum.this, MainActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton addPhotoButton = (FloatingActionButton) findViewById(R.id.addPhotoButton);
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_IMAGE);
                    //gridView.setAdapter(adapter);
                }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                savingToDisk();
                Intent intent = new Intent(openAlbum.this, slideShow.class);
                intent.putExtra("index", position);
                intent.putExtra("albumIndex",selection);
                startActivity(intent);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.album_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.delete_photo)
        {
            deletePhoto();
            return true;
        }
        if(id == R.id.move_photo)
        {
            movePhoto();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deletePhoto()
    {
        Toast.makeText(context, "Please select the Photo", Toast.LENGTH_LONG).show();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int pos1 = i;
                final Photo del = photo_list.get(pos1);

                AlertDialog.Builder inputBox = new AlertDialog.Builder(context);
                inputBox.setTitle("Are you sure you want to delete the Album");
                inputBox.setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                photo_list.remove(del);
                                //arrayAdapter = new ArrayAdapter<Album>(this,adpater_int,list);

                                gridView.setAdapter(adapter);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert = inputBox.create();
                alert.show();

                totalPics--;
                currentAlbum.settotalPics(totalPics);
                toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
                toolbarTitle.setText("Album: "+ currentAlbum.getAlbumName()+"  -  "+ currentAlbum.getTotalPics()+" photo(s)");

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        savingToDisk();
                        Intent intent = new Intent(openAlbum.this, slideShow.class);
                        intent.putExtra("index", position);
                        intent.putExtra("albumIndex",selection);
                        startActivity(intent);
                    }

                });
            }
        });
    }

    public void movePhoto()
    {
        Toast.makeText(context, "Please select the Photo", Toast.LENGTH_LONG).show();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int pos1 = i;
                final Photo del = photo_list.get(pos1);

                final String[] items= new String[movePhoto.size()];

                for(int k=0;k<movePhoto.size();k++)
                {
                    items[k] = movePhoto.get(k).getAlbumName();
                }

                AlertDialog.Builder inputBox = new AlertDialog.Builder(context);
                inputBox.setTitle("Select An Album");
                inputBox.setCancelable(false)
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selection = which;
                    }
                });
                inputBox.setPositiveButton("Move", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int selectedPosition = ((AlertDialog)dialogInterface).getListView().getCheckedItemPosition();

                                Album al = movePhoto.get(selectedPosition);
                                if(al.equals(currentAlbum))
                                {
                                    AlertDialog.Builder warning = new AlertDialog.Builder(context);
                                    warning.setTitle("Warning");
                                    warning.setMessage("Can not move to the same album");
                                    warning.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            return;
                                        }
                                    });
                                    warning.show();
                                    return;
                                }

                                moveSetPhoto(list.get(selectedPosition), del);

                                /*MainActivity.list.get(selectedPosition).addPhoto(del);
                                savingToDisk();

                                photo_list.remove(del);

                                gridView.setAdapter(adapter);*/
                                totalPics--;
                                currentAlbum.settotalPics(totalPics);
                                toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
                                toolbarTitle.setText("Album: "+ currentAlbum.getAlbumName()+"  -  "+ currentAlbum.getTotalPics()+" photo(s)");


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                AlertDialog alert = inputBox.create();
                alert.show();

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        savingToDisk();
                        Intent intent = new Intent(openAlbum.this, slideShow.class);
                        intent.putExtra("index", position);
                        intent.putExtra("albumIndex",selection);
                        startActivity(intent);
                    }

                });
            }
        });
    }
    public void onPause()
    {
        super.onPause();
        savingToDisk();

        //savingToDisk();
    }

    @Override
    public void onResume() {
        savingToDisk();
        super.onResume();
    }

    public void onStop()
    {
        savingToDisk();
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        savingToDisk();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        String filename = "not found";
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            try {
                //The address of the image
                Uri selectedImage = data.getData();
                //we are getting an input stream based on the URI of the Image
                final InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                //get the bitmap from the stream
                final Bitmap selectedImageGal = BitmapFactory.decodeStream(imageStream);
                String[] column = {MediaStore.MediaColumns.DISPLAY_NAME};
                ContentResolver c = getApplicationContext().getContentResolver();
                Cursor cursor = c.query(selectedImage, column,
                        null, null, null);

                if(cursor != null) {
                    try
                    {
                        if(cursor.moveToFirst())
                        {
                            filename = cursor.getString(0);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }


                Photo photoToAdd = new Photo(filename);
                File f = new File(selectedImage.getPath());
                String pathID = f.getAbsolutePath();
                //String filename = pathToFileName(selectedImage);
                photoToAdd.setCaption(filename);
                photoToAdd.setImage(selectedImageGal);

                ArrayList<Photo> allPhotos = new ArrayList<Photo>();
                allPhotos.addAll(currentAlbum.getPhotos());

                if(!allPhotos.contains(photoToAdd))
                {
                    currentAlbum.addPhoto(photoToAdd);
                    savingToDisk();
                    gridView = (GridView) findViewById(R.id.gridView);
                    adapter = new PhotoAdapter(this, currentAlbum.getPhotos());
                    gridView.setAdapter(adapter);

                    totalPics++;
                    currentAlbum.settotalPics(totalPics);
                }
                else
                {
                    AlertDialog.Builder warning = new AlertDialog.Builder(context);
                    warning.setTitle("Duplicate Warning");
                    warning.setMessage("Photo already exists!");
                    warning.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            return;
                        }
                    });
                    warning.show();
                }

                toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
                toolbarTitle.setText("Album: "+ currentAlbum.getAlbumName()+"  -  "+ currentAlbum.getTotalPics()+" photo(s)");
                //savingToDisk();

            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
            }
        }


    }
   /* private String pathToFileName(Uri selectedImage){


        String filename = "not found";

        String[] column = {MediaStore.MediaColumns.DISPLAY_NAME};

        ContentResolver contentResolver = getApplicationContext().getContentResolver();

        Cursor cursor = contentResolver.query(selectedImage, column,
                null, null, null);

        if(cursor != null) {
            try {
                if (cursor.moveToFirst()){
                    filename = cursor.getString(0);
                }
            } catch (Exception e){

            }
        }

        return filename;

    }*/

 public void moveSetPhoto(Album album, Photo pic)
 {
    album.addPhoto(pic);
    photo_list.remove(pic);

    gridView.setAdapter(new PhotoAdapter(this,photo_list));
 }

    public void savingToDisk()
    {
        try {

            FileOutputStream fos = new FileOutputStream(new File(new File(getFilesDir(),"")+ File.separator +"photoalbum.ser"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(list);
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

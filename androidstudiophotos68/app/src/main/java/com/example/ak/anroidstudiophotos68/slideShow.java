package com.example.bhavin.anroidstudiophotos68;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.bhavin.anroidstudiophotos68.R.layout.tag_int;

public class slideShow extends AppCompatActivity {

    int selection;
    int currentAlbumIndex;
    ArrayList<Album> list;

    Album currentAlbum;
    Photo currentPhoto;
    TextView toolbarTitle;

    ListView tagListView;
    ArrayAdapter<TagIdentity> tagAdapter;
    ArrayList<TagIdentity> tagArray = new ArrayList<TagIdentity>();
    ArrayList<TagIdentity> delete = new ArrayList<TagIdentity>();

    int sel;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_show);

        try {

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(new File(getFilesDir(),"")+File.separator + "photoalbum.ser" )));
            list = (ArrayList<Album>) ois.readObject();

            ois.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tagListView = (ListView) findViewById(R.id.tagslistView);

        Bundle bundle = getIntent().getExtras();
        selection  = bundle.getInt("index");
        currentAlbumIndex = bundle.getInt("albumIndex");

        currentAlbum = list.get(currentAlbumIndex);
        currentPhoto = currentAlbum.getPhotos().get(selection);

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(currentPhoto.getCaption());

        //tagArray.addAll(currentPhoto.getTag());
        tagAdapter = new ArrayAdapter<TagIdentity>(context, tag_int, (List<TagIdentity>) currentPhoto.getTag());
        tagListView.setAdapter(tagAdapter);

        sel = selection;

        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(slideShow.this, openAlbum.class);
                intent.putExtra("name",currentAlbum.getAlbumName());
                intent.putExtra("index", currentAlbumIndex);
                startActivity(intent);
            }
        });

        final ImageView iv = (ImageView) findViewById(R.id.fullImageView);
        iv.setImageBitmap(currentPhoto.getImage());

        FloatingActionButton leftArrow = (FloatingActionButton) findViewById(R.id.leftButton);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((sel-1) >= 0)
                {
                    Photo prev = currentAlbum.getPhotos().get(sel-1);
                    iv.setImageBitmap(prev.getImage());
                    sel = sel-1;
                    tagAdapter = new ArrayAdapter<TagIdentity>(context, tag_int, (List<TagIdentity>) currentAlbum.getPhotos().get(sel).getTag());
                    tagListView.setAdapter(tagAdapter);
                }

            }
        });

        FloatingActionButton rightArrow = (FloatingActionButton) findViewById(R.id.rightButton);
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((sel+1) < currentAlbum.getPhotos().size())
                {
                    Photo prev = currentAlbum.getPhotos().get(sel+1);
                    iv.setImageBitmap(prev.getImage());
                    sel = sel+1;
                    tagAdapter = new ArrayAdapter<TagIdentity>(context, tag_int, (List<TagIdentity>) currentAlbum.getPhotos().get(sel).getTag());
                    tagListView.setAdapter(tagAdapter);
                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.slide_show_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.add_tag)
        {
            addTag();
            return true;
        }
        if(id == R.id.delete_tag)
        {
            deleteTag();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addTag()
    {
        final Photo current = currentAlbum.getPhotos().get(sel);

        final String[] items = {"Person","Location"};
        AlertDialog.Builder inputBox = new AlertDialog.Builder(this);
        inputBox.setTitle("Select Tag type and Enter Tag");
        inputBox.setCancelable(false)
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selection = which;
                    }
                });
        LayoutInflater input = LayoutInflater.from(this);
        final View dialogue = input.inflate(R.layout.tag_dialogue, null);

        inputBox.setView(dialogue);

        final EditText tagInput = (EditText) dialogue.findViewById(R.id.editTextDialogUserInput);
        inputBox.setPositiveButton("Add Tag", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int selectedPosition = ((AlertDialog) dialogInterface).getListView().getCheckedItemPosition();

                String tagValue = String.valueOf(tagInput.getText());

                if (selectedPosition < 0 || tagValue == null || tagValue.trim().equals("")) {
                    AlertDialog.Builder warning = new AlertDialog.Builder(context);
                    warning.setTitle("Warning");
                    warning.setMessage("Need to select Tag type/ Tag value is empty");
                    warning.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            return;
                        }
                    });
                    warning.show();
                    return;
                }
                if (selectedPosition == 0) {
                    TagIdentity tag = new TagIdentity("Person", tagValue);
                    current.addTag(tag);
                    savingToDisk();

                   // tagArray.addAll(current.getTag());
                    tagAdapter = new ArrayAdapter<TagIdentity>(context, tag_int,(List<TagIdentity>) current.getTag());
                    tagListView.setAdapter(tagAdapter);

                }
                if (selectedPosition == 1) {
                    TagIdentity tag = new TagIdentity("Location", tagValue);
                    current.addTag(tag);
                    savingToDisk();

                    //tagArray.addAll(current.getTag());
                    tagAdapter = new ArrayAdapter<TagIdentity>(context, tag_int, (List<TagIdentity>)current.getTag());
                    tagListView.setAdapter(tagAdapter);

                }

            }
        });
        inputBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        AlertDialog alert = inputBox.create();
        alert.show();
    }

    public void deleteTag()
    {
        int size = currentAlbum.getPhotos().get(sel).getTag().size();
        String[] items = new String[size];

        for(int k=0;k<size;k++)
        {
            TagIdentity value = currentAlbum.getPhotos().get(sel).getTag().get(k);
            items[k] = String.valueOf(value);
        }
        AlertDialog.Builder inputBox = new AlertDialog.Builder(context);
        inputBox.setTitle("Select Tag");
        inputBox.setCancelable(false)
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selection = which;
                    }
                });
        inputBox.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int selectedPosition = ((AlertDialog)dialogInterface).getListView().getCheckedItemPosition();

                TagIdentity rem = currentAlbum.getPhotos().get(sel).getTag().get(selectedPosition);
                currentAlbum.getPhotos().get(sel).getTag().remove(rem);
                currentAlbum.getPhotos().get(sel).setTag(currentAlbum.getPhotos().get(sel).getTag());
                savingToDisk();

                tagAdapter = new ArrayAdapter<TagIdentity>(context,tag_int,(List<TagIdentity>)currentAlbum.getPhotos().get(sel).getTag());
                tagListView.setAdapter(tagAdapter);

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

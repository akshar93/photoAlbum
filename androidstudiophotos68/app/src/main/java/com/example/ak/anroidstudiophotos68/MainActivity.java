package com.example.bhavin.anroidstudiophotos68;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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

import static com.example.bhavin.anroidstudiophotos68.R.layout.adpater_int;
import static com.example.bhavin.anroidstudiophotos68.R.layout.tag_int;

public class MainActivity extends AppCompatActivity {
    //private static final long serialVersionUID = 0L;
    final Context context = this;
    String name ="";
    ListView albumNames;
    static ArrayAdapter<Album> arrayAdapter;
    static ArrayList<Album> list = new ArrayList<Album>();
    Bundle saved;
    //public static Save pa;
    public static final String storage = "photoalbum.ser";
    ArrayAdapter<TagIdentity> tagAdapter;
    ArrayList<TagIdentity> ar = new ArrayList<TagIdentity>();
    static ArrayList<Photo> subPhotos = new ArrayList<Photo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //loadFromDisk(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        albumNames = (ListView)findViewById(R.id.photo_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextView heading = (TextView) findViewById(R.id.toolbar_title);

        //toolbar.inflateMenu(R.menu.menu_main);
        setSupportActionBar(toolbar);
        heading.setText("Photo Album");

        Album al =null;
        try {

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(new File(getFilesDir(),"")+File.separator + "photoalbum.ser" )));
            list = (ArrayList<Album>) ois.readObject();

            ois.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }

        albumNames.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        albumNames.setSelection(0);
        //list.add(al);
        arrayAdapter = new ArrayAdapter<Album>(this,adpater_int,list);
        albumNames.setAdapter(arrayAdapter);

        albumNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, openAlbum.class);
                final Album alb_name = arrayAdapter.getItem(position);
                String s_name = alb_name.getAlbumName();
                intent.putExtra("name",s_name);
                intent.putExtra("index", position);
                startActivity(intent);
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.addAlbum)
        {
            addAlbum();
            return true;
        }
        if(id == R.id.renameAlbum)
        {
            renameAlbum();
            return true;
        }
        if(id == R.id.deleteAlbum)
        {
            deleteAlbum();
            return true;
        }
        if(id == R.id.search)
        {
            searchPhoto();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addAlbum()
    {
        LayoutInflater input = LayoutInflater.from(context);
        final View dialogue = input.inflate(R.layout.input_dialogue, null);

        AlertDialog.Builder inputBox = new AlertDialog.Builder(context);
        inputBox.setView(dialogue);

        final EditText albumInput = (EditText) dialogue.findViewById(R.id.editTextDialogUserInput);
        inputBox.setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        name = String.valueOf(albumInput.getText());
                        Album al = new Album(name);

                        if (name == null || name.trim().equals("")) {
                            AlertDialog.Builder warning = new AlertDialog.Builder(context);
                            warning.setTitle("Empty Warning");
                            warning.setMessage("Album name is Empty");
                            warning.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    return;
                                }
                            });
                            warning.show();
                        }

                        if (list != null) {
                            for (int a = 0; a < list.size(); a++) {
                                Album item = list.get(a);
                                if (item.equals(al)) {
                                    AlertDialog.Builder warning = new AlertDialog.Builder(context);
                                    warning.setTitle("Duplicate Warning");
                                    warning.setMessage("Album name already exists");
                                    warning.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            return;
                                        }
                                    });
                                    warning.show();
                                    return;
                                }
                            }

                        }
                        list.add(al);


                        arrayAdapter = new ArrayAdapter<Album>(context, adpater_int, list);
                        albumNames.setAdapter(arrayAdapter);

                        try {
                            savingToDisk();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

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
        albumNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                savingToDisk();
                Intent intent = new Intent(MainActivity.this, openAlbum.class);
                final Album alb_name = arrayAdapter.getItem(position);
                String s_name = alb_name.getAlbumName();
                intent.putExtra("name",s_name);
                intent.putExtra("index", position);
                startActivity(intent);

            }

        });

    }
    public void renameAlbum()
    {
        Toast.makeText(context, "Please select the Album", Toast.LENGTH_LONG).show();
        albumNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int pos = i;
                final Album oldName = arrayAdapter.getItem(pos);
                // final Album maybe = new Album(oldName);
                LayoutInflater input = LayoutInflater.from(context);
                final View dialogue = input.inflate(R.layout.rename_dio, null);

                AlertDialog.Builder inputBox = new AlertDialog.Builder(context);
                inputBox.setView(dialogue);
                inputBox.setTitle("Current Album Name: " + arrayAdapter.getItem(pos).getAlbumName());

                final EditText albumInput = (EditText) dialogue.findViewById(R.id.renameEdit);
                inputBox.setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                name = albumInput.getText().toString();
                                Album obj = new Album(name);
                                if (name == null || name.trim().equals("")) {
                                    AlertDialog.Builder warning = new AlertDialog.Builder(context);
                                    warning.setTitle("Empty Warning");
                                    warning.setMessage("Name is Empty");
                                    warning.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            return;
                                        }
                                    });
                                    warning.show();
                                } else {
                                    for (int k = 0; k < list.size(); k++) {
                                        Album check = list.get(k);

                                        if (check.equals(obj)) {
                                            AlertDialog.Builder warning = new AlertDialog.Builder(context);
                                            warning.setTitle("Duplicate Warning");
                                            warning.setMessage("Album name already exists");
                                            warning.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    return;
                                                }
                                            });
                                            warning.show();
                                            return;
                                        }
                                    }

                                }
                                arrayAdapter.getItem(pos).setAlbumName(name);
                                savingToDisk();
                                /*list.remove(oldName);
                                list.add(obj);
                                savingToDisk();*/


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

                albumNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        savingToDisk();
                        Intent intent = new Intent(MainActivity.this, openAlbum.class);
                        final Album alb_name = arrayAdapter.getItem(position);
                        String s_name = alb_name.getAlbumName();
                        intent.putExtra("name",s_name);
                        intent.putExtra("index", position);
                        startActivity(intent);

                    }

                });
            }
        });

    }

    public void deleteAlbum()
    {
        Toast.makeText(context, "Please select the Album", Toast.LENGTH_LONG).show();
        albumNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int pos1 = i;
                final Album del = arrayAdapter.getItem(pos1);

                AlertDialog.Builder inputBox = new AlertDialog.Builder(context);
                inputBox.setTitle("Are you sure you want to delete the Album");
                inputBox.setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                list.remove(del);
                                //arrayAdapter = new ArrayAdapter<Album>(this,adpater_int,list);

                                albumNames.setAdapter(arrayAdapter);
                                savingToDisk();
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

                albumNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        savingToDisk();
                        Intent intent = new Intent(MainActivity.this, openAlbum.class);
                        final Album alb_name = arrayAdapter.getItem(position);
                        String s_name = alb_name.getAlbumName();
                        intent.putExtra("name",s_name);
                        intent.putExtra("index", position);
                        startActivity(intent);
                    }

                });
            }
        });

    }
    protected void onPause()
    {
        savingToDisk();
        super.onPause();

    }
    protected void onStop()
    {
        savingToDisk();
        super.onStop();

    }
    public void searchPhoto()
    {
        final ArrayList<Photo> allPhotos = new ArrayList<Photo>();

        for(int i=0;i<list.size();i++)
        {
            allPhotos.addAll(list.get(i).getPhotos());
        }

        final Dialog dialogBox = new Dialog(context);
        dialogBox.setContentView(R.layout.search_dialogue);

        dialogBox.setTitle("Search Photos");

        final AutoCompleteTextView textView = (AutoCompleteTextView) dialogBox.findViewById(R.id.tagValue);
        Button searchTag = (Button) dialogBox.findViewById(R.id.dialogSearch);
        final Button cancel = (Button) dialogBox.findViewById(R.id.dialogCancel);
        final Spinner tagType = (Spinner) dialogBox.findViewById(R.id.dialog_spinner);
        Button addSearchTag = (Button) dialogBox.findViewById(R.id.addSearchTag);
        final ListView tagsList = (ListView) dialogBox.findViewById(R.id.tagsList);

        ArrayList<String> loc = new ArrayList<String>();
       // ArrayList<String> person = new ArrayList<String>();

        for(int i=0;i<allPhotos.size();i++) {
            ArrayList<TagIdentity> photoTag = allPhotos.get(i).getTag();
            for (int j = 0; j < photoTag.size(); j++) {
                String sTag = photoTag.get(j).getName();
                String sKey = photoTag.get(j).getValue();

               /* if(sTag.equalsIgnoreCase("person"))
                {
                    if(!person.contains(sKey))
                    {
                        person.add(sKey);
                    }
                }*/
                if(!loc.contains(sKey))
                {
                    loc.add(sKey);
                }
            }
        }

       /* if(tagType.getSelectedItem().toString().equalsIgnoreCase("person"))
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line, person);
            textView.setAdapter(adapter);
        }*/

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line, loc);
        textView.setAdapter(adapter);

        addSearchTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText tagValue = (EditText) dialogBox.findViewById(R.id.tagValue);
                String value = String.valueOf(tagValue.getText());
                if(value == null || value.trim().equals(""))
                {
                    AlertDialog.Builder warning = new AlertDialog.Builder(context);
                    warning.setTitle("Empty Warning");
                    warning.setMessage("Tag is Empty");
                    warning.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            return;
                        }
                    });
                    warning.show();
                    return;
                }
                TagIdentity identity = new TagIdentity(tagType.getSelectedItem().toString(),value);
                ar.add(identity);
                tagAdapter = new ArrayAdapter<TagIdentity>(context,tag_int,ar);
                tagsList.setAdapter(tagAdapter);
            }
        });

        searchTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<allPhotos.size();i++) {
                    ArrayList<TagIdentity> photoTag = allPhotos.get(i).getTag();
                    for (int j = 0; j < photoTag.size(); j++) {
                        String sTag = photoTag.get(j).getName();
                        String sKey = photoTag.get(j).getValue();

                        for (int k = 0; k < ar.size(); k++) {
                            TagIdentity tg = ar.get(k);
                            String tag = tg.name;
                            String value = tg.value;

                            if (sTag.equalsIgnoreCase(tag) && sKey.equalsIgnoreCase(value)) {
                                if (!(subPhotos.contains(allPhotos.get(i)))) {
                                    subPhotos.add(allPhotos.get(i));
                                }
                            }
                        }
                    }
                }

                if(subPhotos.size() == 0)
                {
                    AlertDialog.Builder warning = new AlertDialog.Builder(context);
                    warning.setTitle("Empty Warning");
                    warning.setMessage("Result is Empty");
                    warning.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            return;
                        }
                    });
                    warning.show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, searchPhoto.class);
                intent.putExtra("index", subPhotos.size());
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox.dismiss();
            }
        });
        dialogBox.show();

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

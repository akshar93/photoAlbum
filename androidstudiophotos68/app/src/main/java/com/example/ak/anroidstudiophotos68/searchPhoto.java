package com.example.bhavin.anroidstudiophotos68;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class searchPhoto extends AppCompatActivity {

    final Context context = this;
    TextView heading;
    int size;

    PhotoAdapter photoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_photo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(searchPhoto.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        size  = bundle.getInt("index");

        heading = (TextView)findViewById(R.id.toolbar_title);
        heading.setText("Search Result  -- " + size +" Photo(s)");

        GridView gridView = gridView = (GridView) findViewById(R.id.gridView);
        photoAdapter = new PhotoAdapter(this,MainActivity.subPhotos);

        gridView.setAdapter(photoAdapter);


    }

}

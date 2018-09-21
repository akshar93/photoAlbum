package com.example.bhavin.anroidstudiophotos68;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Thread time = new Thread()
        {
          public void run()
          {
              try{
                  sleep(3000);

              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              finally {
                  Intent intent = new Intent(LoadScreen.this, MainActivity.class);
                  startActivity(intent);
              }

          }
        };
        time.start();
    }
}

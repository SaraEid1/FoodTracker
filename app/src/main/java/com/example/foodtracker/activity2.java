package com.example.foodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

public class activity2 extends AppCompatActivity {
    private Button button1;
    private Button AddFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity2);
        button1 = (Button) findViewById(R.id.button1);
        AddFood = (Button) findViewById (R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openactivity3();
            }
        });
        AddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openactivity4();
            }
        });
 }

    public void openactivity3() {
        Intent intent = new Intent(this, activity3_myitems.class);
        startActivity(intent);
    }

    public void openactivity4() {
        Intent intent = new Intent(this, AddFood.class);
        startActivity(intent);
    }

}
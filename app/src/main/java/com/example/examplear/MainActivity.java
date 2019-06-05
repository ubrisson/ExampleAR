package com.example.examplear;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton imgBtn = findViewById(R.id.imageButton);
        imgBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent arIntent = new Intent(this,ARActivity.class);
        startActivity(arIntent);
    }
}

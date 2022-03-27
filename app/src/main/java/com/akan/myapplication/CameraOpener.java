package com.akan.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

public class CameraOpener extends AppCompatActivity {
    final private int PHOTO_CLICK = 122;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_opener);
        button = findViewById(R.id.IdCamera);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CameraOpener.this, MainActivity.class);
                intent.putExtra("OpenCamera","true");
                startActivity(intent);
            }
        });
    }
}
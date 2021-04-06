package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CardView extends AppCompatActivity {

    ImageView mainImage;
    TextView title, description;

    String data1, data2;
    int myImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        mainImage = findViewById(R.id.card_image);
        title = findViewById(R.id.card_title_text);
        description = findViewById(R.id.card_des_text);

        getData();
        setData();
    }

    private void getData() {
        if (getIntent().hasExtra("myImage") && getIntent().hasExtra("data1") && getIntent().hasExtra("data2")) {
            data1 = getIntent().getStringExtra("data1");
            data2 = getIntent().getStringExtra("data2");
            myImage = getIntent().getIntExtra("myImage", 1);
        } else {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData() {
        title.setText(data1);
        description.setText(data2);
        mainImage.setImageResource(myImage);
    }
}
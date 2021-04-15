package com.example.app1.Exercise;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app1.R;

public class CardView extends AppCompatActivity {

    ImageView mainImage1,mainImage2;
    TextView title, description;

    String data1, data2;
    int myImage1,myImage2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mainImage1 = findViewById(R.id.card_image);
        mainImage2 = findViewById(R.id.card_image2);
        title = findViewById(R.id.card_title_text);
        description = findViewById(R.id.card_des_text);

        getData();
        setData();
    }

    private void getData() {
        if (getIntent().hasExtra("image") && getIntent().hasExtra("text1") && getIntent().hasExtra("text2")) {
            data1 = getIntent().getStringExtra("text1");
            data2 = getIntent().getStringExtra("text2");
            myImage1 = getIntent().getIntExtra("image", 1);
            myImage2 = getIntent().getIntExtra("image2", 1);
        } else {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData() {
        title.setText(data1);
        description.setText(data2);
        mainImage1.setImageResource(myImage1);
        mainImage2.setImageResource(myImage2);
    }
}
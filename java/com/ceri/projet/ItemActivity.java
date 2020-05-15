package com.ceri.projet;


import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class ItemActivity extends AppCompatActivity {

    private TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        this.desc = findViewById(R.id.itemDesc);
//        this.desc.setMovementMethod(new ScrollingMovementMethod());

    }

}

package com.ceri.projet;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ItemActivity extends AppCompatActivity {

    private TextView txtName, txtBrand, txtYear, txtCategories, txtDesc, txtDetails, txtLastUpdate;
    private ImageView ivThumbnail;
    private SliderAdapter adapter;
    private SliderView sliderView;

    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        this.item = getIntent().getParcelableExtra(Item.TAG);

        this.txtName = findViewById(R.id.itemName);
        this.ivThumbnail = findViewById(R.id.itemThumbnail);
        this.txtBrand = findViewById(R.id.itemBrand);
        this.txtYear = findViewById(R.id.itemYear);
        this.txtCategories = findViewById(R.id.itemCategories);
        this.txtDesc = findViewById(R.id.itemDesc);
        this.txtDetails = findViewById(R.id.itemDetails);
        this.txtLastUpdate = findViewById(R.id.itemLastUpdate);

        this.adapter = new SliderAdapter(this, new ArrayList<ItemImage>());
        this.sliderView = findViewById(R.id.itemPicturesSliderView);
        this.sliderView.setSliderAdapter(adapter);
//        this.sliderView.startAutoCycle();
//        this.sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
//        this.sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

        this.updateView();
    }

    private void updateView() {
        this.txtName.setText(this.item.getName());
        this.txtBrand.setText(this.item.getBrand());
        if(this.item.getYear() == Item.NULL_YEAR)
            this.txtYear.setText("");
        else
            this.txtYear.setText(String.valueOf(this.item.getYear()));
        String categories = "";
        int i = 0;
        for( ; i < this.item.getCategories().size() - 1 ; ++i)
            categories += this.item.getCategories().get(i) + ", ";
        categories += this.item.getCategories().get(i);
        this.txtCategories.setText(categories);
        this.txtDesc.setText(this.item.getDesc());
        String details = "Détails techniques : \n";
        for(i = 0 ; i < this.item.getTechnicalDetails().size() - 1 ; ++i)
            details += "-" + this.item.getTechnicalDetails().get(i) + "\n";
        details += "-" + this.item.getTechnicalDetails().get(i);
        this.txtDetails.setText(details);

        if(this.item.getPictures().isEmpty())
            this.item.getPictures().add(new ItemImage(ItemImage.NO_PICTURES_DESC, ItemImage.NO_PICTURES_IMAGE));

        for(ItemImage image : this.item.getPictures())
            this.adapter.addItem(image);



        String lastUpdate = "Dernière mise à jour : " + this.item.getLastUpdate();
        this.txtLastUpdate.setText(lastUpdate);

        Glide.with(this)
                .load(this.item.getThumbnail())
                .centerCrop()
                .into(this.ivThumbnail);
    }
}

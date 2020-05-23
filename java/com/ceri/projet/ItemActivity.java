package com.ceri.projet;


import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;



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
        this.txtDesc.setText(this.item.getDesc());

        if(this.item.getYear() == Item.NULL_YEAR)
            this.txtYear.setText("");
        else
            this.txtYear.setText(String.valueOf(this.item.getYear()));

        String categories = "";
        if(!this.item.getCategories().isEmpty()) {
            int i = 0;
            for( ; i < this.item.getCategories().size() - 1 ; ++i)
                categories += this.item.getCategories().get(i) + ", ";
            categories += this.item.getCategories().get(i);
        }
        this.txtCategories.setText(categories);

        String details = "Détails techniques : \n";
        if(!this.item.getTechnicalDetails().isEmpty()) {
            int i = 0;
            for(i = 0 ; i < this.item.getTechnicalDetails().size() - 1 ; ++i)
                details += "-" + this.item.getTechnicalDetails().get(i) + "\n";
            details += "-" + this.item.getTechnicalDetails().get(i);
        }
        this.txtDetails.setText(details);

        if(this.item.getPictures().isEmpty())
            this.item.getPictures().add(new ItemImage(ItemImage.NO_PICTURES_DESC, ItemImage.NO_PICTURES_IMAGE));
        for(ItemImage image : this.item.getPictures())
            this.adapter.addItem(image);

        String lastUpdate = "Dernière mise à jour : " + this.item.getLastUpdate();
        this.txtLastUpdate.setText(lastUpdate);

        GlideBny.loadFromWeb(this, this.item.getThumbnail(), this.ivThumbnail, GlideBny.Center.CROP);
    }
}

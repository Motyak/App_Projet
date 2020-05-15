package com.ceri.projet;


import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.smarteist.autoimageslider.SliderView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ItemActivity extends AppCompatActivity {

    private TextView txtName, txtBrand, txtYear, txtCategories, txtDesc, txtDetails, txtLastUpdate;
    private SliderAdapter adapter;
    private SliderView sliderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        this.txtName = findViewById(R.id.itemName);
        this.txtBrand = findViewById(R.id.itemBrand);
        this.txtYear = findViewById(R.id.itemYear);
        this.txtCategories = findViewById(R.id.itemCategories);
        this.txtDesc = findViewById(R.id.itemDesc);
        this.txtDetails = findViewById(R.id.itemDetails);
        this.txtLastUpdate = findViewById(R.id.itemLastUpdate);

        this.txtName.setText("Lecteur de cartouches amovibles 88 Mio");
        this.txtBrand.setText("SyQuest Technology");
        this.txtYear.setText(String.valueOf(1991));
        this.txtDesc.setText("Les cartouches SyQuest dans leurs versions 44 et 88 Mo constituaient la solution la plus répandue (en particulier dans le monde Macintosh) pour les échanges de données volumineuses. Elles étaient très utilisées dans les domaines de la publication assistée par ordinateur et du multimédia.\nLes cartouches contenaient les plateaux de disques durs, les têtes de lecture/écriture étant dans le lecteur.");
        this.txtCategories.setText("périphérique," + " support de stockage," + " SCSI," + " some stuff");
        this.txtDetails.setText("Détails techniques : \n" + "-Cartouches à disque dur, format 5¼ pouces\n" + "-Capacité de 88 Mo par cartouche\n" + "-Connexion SCSI");
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        this.txtLastUpdate.setText("Dernière mise à jour : " + dateFormat.format(currentTime));

        List<ItemImage> itemImages = Arrays.asList(new ItemImage("description", "imageUrl"), new ItemImage("description2", "imageUrl2"));
        this.adapter = new SliderAdapter(this, itemImages);
        this.sliderView = findViewById(R.id.itemPicturesSliderView);
        this.sliderView.setSliderAdapter(adapter);
//        this.sliderView.startAutoCycle();
//        this.sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
//        this.sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

    }

}

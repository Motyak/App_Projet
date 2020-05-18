package com.ceri.projet;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Process the response to a GET request to the Web service
 * https://demo-lia.univ-avignon.fr/cerimuseum/items/..itemWebId..
 * Responses must be provided in JSON.
 *
 */


public class JSONResponseHandlerItem {

    private static final String TAG = JSONResponseHandlerItem.class.getSimpleName();

    private Item item;


//    On suppose que l'item qui est passé possède un web id
    public JSONResponseHandlerItem(Item item) {
        this.item = item;
        this.item.clearAllLists();
        try {
            this.item.setThumbnail(WebServiceUrl.buildSearchThumbnail(this.item.getWebId()).toString());
        } catch (MalformedURLException e) { e.printStackTrace(); }
        this.item.setLastUpdate();
    }

    public Item getItem() { return this.item; }

    /**
     * @param response done by the Web service
     * @return An item with attributes filled with the collected information if response was
     * successfully analyzed
     */
    public void readJsonStream(InputStream response) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(response, "UTF-8"));
        try {
            readItem(reader);
        } finally {
            reader.close();
        }
    }

    public void readItem(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals("categories") && reader.peek() != JsonToken.NULL) {
                readStringArray(reader, this.item.getCategories());
            }
            else if(name.equals("technicalDetails") && reader.peek() != JsonToken.NULL) {
                readStringArray(reader, this.item.getTechnicalDetails());
            }
            else if(name.equals("timeFrame") && reader.peek() != JsonToken.NULL) {
                readIntArray(reader, this.item.getTimeFrame());
            }
            else if(name.equals("pictures") && reader.peek() != JsonToken.NULL) {
                readImages(reader, this.item.getPictures());
            }
            else {
                readValue(reader, name);
            }
        }
        reader.endObject();

        if(this.item.getYear() == Item.NULL_YEAR && this.item.getTimeFrame().size() > 0)
            this.item.setYear(this.item.getTimeFrame().get(0));
    }


    private void readStringArray(JsonReader reader, ArrayList<String> l) throws IOException {
        reader.beginArray();
        while (reader.hasNext() ) {
            while (reader.hasNext()) {
                l.add(reader.nextString());
            }
        }
        reader.endArray();
    }

    private void readIntArray(JsonReader reader, ArrayList<Integer> l) throws IOException {
        reader.beginArray();
        while (reader.hasNext() ) {
            while (reader.hasNext()) {
                l.add(reader.nextInt());
            }
        }
        reader.endArray();
    }

    private void readImages(JsonReader reader, ArrayList<ItemImage> l) throws IOException {
        reader.beginObject();
            while (reader.hasNext()) {
                String imageUrl = WebServiceUrl.buildSearchImage(this.item.getWebId(), reader.nextName()).toString();
                String description = reader.nextString();
                l.add(new ItemImage(description, imageUrl));
            }
        reader.endObject();
    }

    private void readValue(JsonReader reader, String name) throws IOException {
        if(name.equals("description")) {
            if(reader.peek() == JsonToken.NULL) {
                this.item.setDesc("");
                reader.nextNull();
            }
            else
                this.item.setDesc(reader.nextString());
        }
        else if(name.equals("brand")) {
            if(reader.peek() == JsonToken.NULL) {
                this.item.setBrand("");
                reader.nextNull();
            }
            else
                this.item.setBrand(reader.nextString());
        }
        else if(name.equals("name") && reader.peek() != JsonToken.NULL) {
            this.item.setName(reader.nextString());
        }
        else if(name.equals("year")) {
            if(reader.peek() == JsonToken.NULL) {
                this.item.setYear(9999);
                reader.nextNull();
            }
            else
                this.item.setYear(reader.nextInt());
        }
        else {
            reader.skipValue();
        }
    }

}
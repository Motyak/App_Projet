package com.ceri.projet;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Process the response to a GET request to the Web service
 * https://demo-lia.univ-avignon.fr/cerimuseum/catalog
 * Responses must be provided in JSON.
 *
 */


public class JSONResponseHandlerCatalog {

    private static final String TAG = JSONResponseHandlerCatalog.class.getSimpleName();

    private JSONResponseHandlerItem jsonItem;
    private Item tmpItem;
    private ArrayList<Item> catalog;


    public JSONResponseHandlerCatalog() {
        this.catalog = new ArrayList<>();
        this.tmpItem = new Item();
    }

    public ArrayList<Item> getItems() { return this.catalog; }

    /**
     * @param response done by the Web service
     * @return Items with attributes filled with the collected information if response was
     * successfully analyzed
     */
    public void readJsonStream(InputStream response) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(response, "UTF-8"));
        try {
            readCatalog(reader);
        } finally {
            reader.close();
        }
    }

    public void readCatalog(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String webId = reader.nextName();
            this.tmpItem = new Item();
            this.tmpItem.setWebId(webId);
//                requiert le champ web id pour pouvoir faire le traitement
            this.jsonItem = new JSONResponseHandlerItem(this.tmpItem);
            this.jsonItem.readItem(reader);
            this.catalog.add(this.tmpItem);
        }
        reader.endObject();
    }
}
package com.ceri.projet;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class WebServiceUrl {
    private static final String HOST = "demo-lia.univ-avignon.fr";
    private static final String PATH = "cerimuseum";

    private static Uri.Builder commonBuilder() {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .authority(HOST)
                .appendPath(PATH);
        return builder;
    }

    // Get information on a specific item
    // https://demo-lia.univ-avignon.fr/cerimuseum/items/hsv
    private static final String SEARCH_ITEM = "items";

    // Build URL to get information for a specific item
    public static URL buildSearchItem(String itemWebId) throws MalformedURLException {
        Uri.Builder builder = commonBuilder();
        builder.appendPath(SEARCH_ITEM)
                .appendPath(itemWebId);
        URL url = new URL(builder.build().toString());
        return url;
    }

    // Get information on all items
    // https://demo-lia.univ-avignon.fr/cerimuseum/catalog
    private static final String SEARCH_CATALOG = "catalog";

    // Build URL to get information for all items
    public static URL buildSearchCatalog() throws MalformedURLException {
        Uri.Builder builder = commonBuilder();
        builder.appendPath(SEARCH_CATALOG);
        URL url = new URL(builder.build().toString());
        return url;
    }

    // Get a photo from item id and image name
    // https://demo-lia.univ-avignon.fr/cerimuseum/items/hsv/images/IMG_1683
    private static final String SEARCH_IMAGE = "images";

    // Build URL to get a specific image from an item
    public static URL buildSearchImage(String itemWebId, String imageName) throws MalformedURLException {
        Uri.Builder builder = commonBuilder();
        builder.appendPath(SEARCH_ITEM)
                .appendPath(itemWebId)
                .appendPath(SEARCH_IMAGE)
                .appendPath(imageName);
        URL url = new URL(builder.build().toString());
        return url;
    }

}

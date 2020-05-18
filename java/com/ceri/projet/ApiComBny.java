package com.ceri.projet;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Mk.HttpCon;

//classes qui contient les scénarios avec l'API
public class ApiComBny {

    public static boolean updateItem(Item item) throws IOException {
        URL searchItemUrl = WebServiceUrl.buildSearchItem(item.getWebId());
        String res = HttpCon.request(HttpCon.Type.GET, searchItemUrl.toString(), null, null);
        InputStream is = new ByteArrayInputStream(res.getBytes("UTF-8"));

        System.out.println(res);    //debug

//        JSONResponseHandlerItem jsonItem = new JSONResponseHandlerItem(item);
//        jsonItem.readJsonStream(is);
//
//        item = jsonItem.getItem();

//        si ca s'est mal passé retourner false


        return true;
    }

    public static boolean getAllItems() throws IOException {
        URL searchCatalogUrl = WebServiceUrl.buildSearchCatalog();
        String res = HttpCon.request(HttpCon.Type.GET, searchCatalogUrl.toString(), null, null);
        InputStream is = new ByteArrayInputStream(res.getBytes("UTF-8"));

        System.out.println(res);    //debug

//        JSONResponseHandlerCatalog jsonCatalog = new JSONResponseHandlerCatalog(catalog);
//        jsonCatalog.readJsonStream(is);
//
//        catalog = jsonCatalog.getItems();

//        si ca s'est mal passé retourner false


        return true;
    }

}

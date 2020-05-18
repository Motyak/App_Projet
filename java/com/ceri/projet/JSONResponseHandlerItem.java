//package com.ceri.projet;
//
//import android.util.JsonReader;
//import android.util.JsonToken;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
///**
// * Process the response to a GET request to the Web service
// * https://www.thesportsdb.com/api/v1/json/1/searchteams.php?t=R
// * Responses must be provided in JSON.
// *
// */
//
//
//public class JSONResponseHandlerItem {
//
//    private static final String TAG = JSONResponseHandlerItem.class.getSimpleName();
//
//    private Item item;
//
//
//    public JSONResponseHandlerItem(Item item) {
//        this.item = item;
//    }
//
//    public Item getItem() { return this.item; }
//
//    /**
//     * @param response done by the Web service
//     * @return A Team with attributes filled with the collected information if response was
//     * successfully analyzed
//     */
//    public void readJsonStream(InputStream response) throws IOException {
//        JsonReader reader = new JsonReader(new InputStreamReader(response, "UTF-8"));
//        try {
//            readTeams(reader);
//        } finally {
//            reader.close();
//        }
//    }
//
//    public void readTeams(JsonReader reader) throws IOException {
//        reader.beginObject();
//        while (reader.hasNext()) {
//            String name = reader.nextName();
//            if (name.equals("teams") && reader.peek() != JsonToken.NULL) {
//                readArrayTeams(reader);
//            } else {
//                reader.skipValue();
//            }
//        }
//        reader.endObject();
//    }
//
//
//    private void readArrayTeams(JsonReader reader) throws IOException {
//        reader.beginArray();
//        int nb = 0; // only consider the first element of the array
//        while (reader.hasNext() ) {
//            reader.beginObject();
//            while (reader.hasNext()) {
//                String name = reader.nextName();
//                if (nb==0) {
//                    if (name.equals("idTeam")) {
//                        item.setIdTeam(reader.nextLong());
//                    } else if (name.equals("strTeam")) {
//                        item.setName(reader.nextString());
//                    } else if (name.equals("strLeague")) {
//                        item.setLeague(reader.nextString());
//                    } else if (name.equals("idLeague")) {
//                        item.setIdLeague(reader.nextLong());
//                    } else if (name.equals("strStadium")) {
//                        item.setStadium(reader.nextString());
//                    } else if (name.equals("strStadiumLocation")) {
//                        item.setStadiumLocation(reader.nextString());
//                    } else if (name.equals("strTeamBadge")) {
//                        item.setTeamBadge(reader.nextString());
//                    } else {
//                        reader.skipValue();
//                    }
//                }  else {
//                    reader.skipValue();
//                }
//            }
//            reader.endObject();
//            nb++;
//        }
//        reader.endArray();
//    }
//
//}
package com.ceri.projet;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Process the response to a GET request to the Web service
 * https://www.thesportsdb.com/api/v1/json/1/eventslast.php?id=135209
 * Responses must be provided in JSON.
 *
 */
public class JSONResponseHandlerLastEvents {
    private static final String TAG = JSONResponseHandlerLastEvents.class.getSimpleName();

    private Item item;
    private Match match;

    public JSONResponseHandlerLastEvents(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return this.item;
    }

    /**
     * @param response done by the Web service
     */
    public void readJsonStream(InputStream response) throws IOException {
        this.match = new Match();
        JsonReader reader = new JsonReader(new InputStreamReader(response, "UTF-8"));
        try {
            readResults(reader);
        } finally {
            reader.close();
        }
        this.item.setLastEvent(this.match);
    }

    public void readResults(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("results")) {
                readArrayResults(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
    }


    private void readArrayResults(JsonReader reader) throws IOException {
        boolean foundEventWithNonNullScores = false;
        reader.beginArray();
        while (reader.hasNext() ) {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (!foundEventWithNonNullScores) {
                    if (name.equals("idEvent")) {
                        match.setId(reader.nextLong());
                    } else if (name.equals("strEvent")) {
                        match.setLabel(reader.nextString());
                    } else if (name.equals("strHomeTeam")) {
                        match.setHomeTeam(reader.nextString());
                    } else if (name.equals("strAwayTeam")) {
                        match.setAwayTeam(reader.nextString());
                    } else if (name.equals("intHomeScore") && reader.peek() != JsonToken.NULL) {
                        match.setHomeScore(reader.nextInt());
                    } else if (name.equals("intAwayScore") && reader.peek() != JsonToken.NULL) {
                        match.setAwayScore(reader.nextInt());
                        foundEventWithNonNullScores = true;
                    } else {
                        reader.skipValue();
                    }
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        }
        reader.endArray();
//        si on trouve aucun match 'complet' (complet <=> aucune valeur null), alors on retourne un match par défaut
        if(!foundEventWithNonNullScores)
            match = new Match(-1, "", "", "", -1, -1);
    }
}

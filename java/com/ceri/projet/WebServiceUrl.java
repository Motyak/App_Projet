package com.ceri.projet;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class WebServiceUrl {

    // constant string used as s parameter for lookuptable
    private static final String CUR_YEAR = "1920";

    private static final String HOST = "www.thesportsdb.com";
    private static final String PATH_1 = "api";
    private static final String PATH_2 = "v1";
    private static final String PATH_3 = "json";
    private static final String PATH_4 = "1";

    private static Uri.Builder commonBuilder() {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .authority(HOST)
                .appendPath(PATH_1)
                .appendPath(PATH_2)
                .appendPath(PATH_3)
                .appendPath(PATH_4);
        return builder;
    }

    // Get information on a specific team
    // https://www.thesportsdb.com/api/v1/json/1/searchteams.php?t=RC%20toulonnais
    private static final String SEARCH_TIMES = "searchteams.php";
    private static final String SEARCH_TIMES_PARAM1 = "t";

    // Build URL to get information for a specific team
    public static URL buildSearchTeam(String teamName) throws MalformedURLException {
        Uri.Builder builder = commonBuilder();
        builder.appendPath(SEARCH_TIMES)
                .appendQueryParameter(SEARCH_TIMES_PARAM1, teamName);
        URL url = new URL(builder.build().toString());
        return url;
    }

    // Get last scores of a given team
    // https://www.thesportsdb.com/api/v1/json/1/eventslast.php?id=133604
    private static final String TEAM_LAST_EVENT = "eventslast.php";
    private static final String TEAM_LAST_EVENT_PARAM1 = "id";
    public static URL buildSearchLastEvents(long idTeam) throws MalformedURLException {
        Uri.Builder builder = commonBuilder();
        builder.appendPath(TEAM_LAST_EVENT)
                .appendQueryParameter(TEAM_LAST_EVENT_PARAM1, String.valueOf(idTeam));
        URL url = new URL(builder.build().toString());
        return url;
    }

    // Get current team ranking in a championship for a given season
    private static final String CHAMPIONSHIP_TABLE = "lookuptable.php";
    private static final String CHAMPIONSHIP_TABLE_PARAM1 = "l";
    private static final String CHAMPIONSHIP_TABLE_PARAM2 = "s";
    public static URL buildGetRanking(long idChampionship) throws MalformedURLException {
        Uri.Builder builder = commonBuilder();
        builder.appendPath(CHAMPIONSHIP_TABLE)
                .appendQueryParameter(CHAMPIONSHIP_TABLE_PARAM1, String.valueOf(idChampionship))
                .appendQueryParameter(CHAMPIONSHIP_TABLE_PARAM2, CUR_YEAR);
        URL url = new URL(builder.build().toString());
        return url;
    }

}

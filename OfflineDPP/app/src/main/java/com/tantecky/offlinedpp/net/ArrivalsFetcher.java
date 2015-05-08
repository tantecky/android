package com.tantecky.offlinedpp.net;

import com.tantecky.offlinedpp.Utils;
import com.tantecky.offlinedpp.model.Line;

import org.jsoup.nodes.Document;

public final class ArrivalsFetcher {
    //region static members
    private final static String sARRIVALS_FROM_PAGE = "http://spojeni.dpp.cz/ZjrForm.aspx";

    public static void fetch(Line line) {
        HttpClient client = new HttpClient();
        addSessionInfo(client);
    }

    private static void addSessionInfo(HttpClient client)
    {
        String html = client.get(sARRIVALS_FROM_PAGE);
        //TODO check null raise Exception

        obtainSessionInfo(html);
    }

    private static void obtainSessionInfo(String html)
    {
        //TODO html parsing
    }

    //region tests helpers
    public static void fromTestCallObtainSessionInfo(String html)
    {
        obtainSessionInfo(html);
    }
    //endregion
    //endregion


    /**
     * Class contains ony static methods
     */
    private ArrivalsFetcher() {
    }
}

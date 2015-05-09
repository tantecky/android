package com.tantecky.offlinedpp.net;


import com.tantecky.offlinedpp.Utils;
import com.tantecky.offlinedpp.model.Line;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ArrivalsFetcher {
    //region static members
    private final static String sARRIVALS_FROM_PAGE = "http://spojeni.dpp.cz/ZjrForm.aspx";

    public static void fetch(Line line) {
        HttpClient client = new HttpClient();

        // TODO exception handling here
        addSessionInfo(client);
    }

    private static void addSessionInfo(HttpClient client) {
        String html = client.get(sARRIVALS_FROM_PAGE);
        //TODO check null raise Exception

        Map<String, String> sessionInfo = obtainSessionInfo(html);

        for (Map.Entry<String, String> item : sessionInfo.entrySet())
        {

        }
    }

    private static Map<String, String> obtainSessionInfo(String html)
            throws IllegalArgumentException {
        //TODO check null raise Exception
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("form input[type=hidden]");

        if (elements.size() < 1)
            throw new IllegalArgumentException("Empty session");

        Map<String, String> sessionInfo = new LinkedHashMap<>(elements.size());

        for (org.jsoup.nodes.Element element : elements) {
            String name = element.attr("name");
            String value = element.attr("value");

            if (Utils.isNullOrEmpty(name))
                throw new IllegalArgumentException("Invalid name attribute");

            sessionInfo.put(name, value);
        }

        return sessionInfo;
    }

    //region tests helpers
    public static void fromTestCallObtainSessionInfo(String html) {
        obtainSessionInfo(html);
    }
    //endregion
    //endregion


    /**
     * Class contains only static methods
     */
    private ArrivalsFetcher() {
    }
}

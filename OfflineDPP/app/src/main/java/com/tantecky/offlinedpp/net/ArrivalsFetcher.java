package com.tantecky.offlinedpp.net;


import com.tantecky.offlinedpp.Utils;
import com.tantecky.offlinedpp.model.Arrival;
import com.tantecky.offlinedpp.model.DayType;
import com.tantecky.offlinedpp.model.Line;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ArrivalsFetcher {
    //region static members
    private final static String sARRIVALS_FROM_PAGE = "http://spojeni.dpp.cz/ZjrForm.aspx";

    public static List<Arrival> fetch(Line line) {
        HttpClient client = new HttpClient();

        try {
            addSessionInfo(client);
            String htmlArrivals = fetchArrivals(client, line);
            return parseArrivalsTables(htmlArrivals);
        } catch (ParserException | IOException ex) {
            // TODO add line info
            ex.printStackTrace();
            return null;
        }
    }

    private static List<Arrival> parseArrivalsTables(String htmlArrivals) throws ParserException {
        Document doc = Jsoup.parse(htmlArrivals);
        Elements tables = doc.select("table.zastavkovy-jr-podrobny");

        if (tables.size() < 1)
            throw new ParserException("No arrivals tables");

        List<Arrival> arrivals = new ArrayList<Arrival>();

        for (Element table : tables) {
            addArrivals(table, arrivals);
        }

        return arrivals;
    }

    private static void addArrivals(Element table, List<Arrival> arrivals) throws ParserException {
        DayType dayType = getDayType(table);

        Elements rows = table.select("tbody tr");

        if (rows.size() < 1)
            throw new ParserException("Arrivals table has no rows");

        for (Element row : rows) {
            Elements hourElement = row.select("td:eq(0)");

            if (hourElement.size() != 1)
                throw new ParserException("Missing hour column");

            int hour;

            try {
                hour = Integer.parseInt(hourElement.text());
            } catch (NumberFormatException ex) {
                throw new ParserException("Unable to convert hour to int: " + hourElement.text());
            }

            if (hour < 0 || hour > 23)
                throw new ParserException("Invalid hour value: " + Integer.toString(hour));

            Elements minuteElements = row.select("td:eq(1) span");

            for (Element minuteElement : minuteElements) {
                // TODO new method addEntry
            }
        }

    }

    private static DayType getDayType(Element table) throws ParserException {
        Elements element = table.select("thead th:eq(1)");

        if (element.size() != 1)
            throw new ParserException("Missing table header");

        try {
            DayType dayType = DayType.fromString(element.text());
            return dayType;
        } catch (IllegalArgumentException ex) {
            throw new ParserException(ex.getMessage());
        }
    }

    private static String fetchArrivals(HttpClient client, Line line) throws IOException {
        client.addFormData("txtLine", line.getName());
        client.addFormData("txtFrom", line.getFrom());
        client.addFormData("txtTo", line.getTo());
        client.addFormData("txtDate", txtDate());
        client.addFormData("chkWholeWeek", "on");
        client.addFormData("cmdSearch", "vyhledat");

        String htmlArrivals = client.fetch(sARRIVALS_FROM_PAGE);

        if (htmlArrivals == null)
            throw new IOException("HttpClient fetch failed");

        Utils.log(htmlArrivals);

        return htmlArrivals;
    }

    public static String txtDate() {
        Calendar today = GregorianCalendar.getInstance();
        return String.format("%d.%d.%d", today.get(Calendar.DAY_OF_MONTH),
                today.get(Calendar.MONTH) + 1, // Java thing...
                today.get(Calendar.YEAR));
    }

    private static void addSessionInfo(HttpClient client) throws ParserException,
            IOException {
        String html = client.fetch(sARRIVALS_FROM_PAGE);

        if (html == null)
            throw new IOException("HttpClient fetch failed");


        Map<String, String> sessionInfo = obtainSessionInfo(html);

        for (Map.Entry<String, String> item : sessionInfo.entrySet()) {
            client.addFormData(item.getKey(), item.getValue());
        }
    }

    private static Map<String, String> obtainSessionInfo(String html)
            throws ParserException {

        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("form input[type=hidden]");

        if (elements.size() < 1)
            throw new ParserException("Empty session");

        Map<String, String> sessionInfo = new LinkedHashMap<>(elements.size());

        for (org.jsoup.nodes.Element element : elements) {
            String name = element.attr("name");
            String value = element.attr("value");

            if (Utils.isNullOrEmpty(name))
                throw new ParserException("Invalid name attribute");

            sessionInfo.put(name, value);
        }

        return sessionInfo;
    }

    //region tests helpers
    public static void fromTestCallObtainSessionInfo(String html) throws ParserException {
        obtainSessionInfo(html);
    }

    public static List<Arrival> fromTestCallParseArrivalsTables(String html) throws ParserException {
        return parseArrivalsTables((html));
    }
    //endregion
    //endregion


    /**
     * Class contains only static methods
     */
    private ArrivalsFetcher() {
    }
}

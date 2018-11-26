package com.example.android.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving news items data from the Guardian.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static final int URL_CONNECT_READ_TIME_OUT = 10000;
    public static final int URL_CONNECTION_CONNECT_TIME_OUT = 15000;
    public static final int OK_RESPONSE_CODE = 200;
    public static final String REQUEST_VERB = "GET";
    public static final int THREAD_SLEEP_TIME = 2000;
    /**constructor made private since no need of creating an instance of this class.*/
    private QueryUtils() {
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(URL_CONNECT_READ_TIME_OUT /* milliseconds */);
            urlConnection.setConnectTimeout(URL_CONNECTION_CONNECT_TIME_OUT /* milliseconds */);
            urlConnection.setRequestMethod(REQUEST_VERB);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == OK_RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    /**
     * Return a list of {@link NewsItem} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<NewsItem> extractNewsItems(String requestUrl) {
        try {
            Thread.sleep(THREAD_SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<NewsItem> newsItems = new ArrayList<>();

        // Try to parse the JSONResponse. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // build up a list of NewsItem objects with the corresponding data.
            //Convert SAMPLE_JSON_RESPONSE String into a JSONObject
            JSONObject myApiResponseJsonObject = new JSONObject(jsonResponse);
            //Extract “response” JSONObject
            JSONObject responseJSONObject = myApiResponseJsonObject.optJSONObject("response");
            //Extract "results" JSONArray
            JSONArray resultsJSONArray = responseJSONObject.optJSONArray("results");
            //Loop through each feature in the array
            for (int i = 0; i < resultsJSONArray.length(); i++){
                //Get earthquake JSONObject at position i
                JSONObject currentNewsItemFeatures = resultsJSONArray.optJSONObject(i);
                //Extract “sectionName” for section name
                String currentSectionName = currentNewsItemFeatures.optString("sectionName");
                //Extract “webPublicationDate” for publication date
                String currentWebPublicationDate = currentNewsItemFeatures.optString("webPublicationDate");
                //Extract “webTitle” for title
                String currentWebTitle = currentNewsItemFeatures.optString("webTitle");
                //Extract "webUrl" for the stories url
                String currentWebUrl = currentNewsItemFeatures.optString("webUrl");
                //Extract "pillarName" for the news type
                String currentPillarName = currentNewsItemFeatures.optString("pillarName");
                //Extract fields
                JSONObject currentFields = currentNewsItemFeatures.optJSONObject("fields");
                //Extract author name from 'byline' key.
                String currentAuthorName = "";
                if (currentFields != null){
                    currentAuthorName = currentFields.optString("byline");
                }
                /**
                 * Create NewsItem java object from section name, web publication date,
                 * web title, web url, pillar name and author name
                 * Add news item to list of news items*/
                    newsItems.add(new NewsItem(currentSectionName, currentWebPublicationDate,
                            currentWebTitle, currentWebUrl, currentPillarName, currentAuthorName));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news page JSON results", e);
        }

        // Return the list of earthquakes
        return newsItems;
    }

}
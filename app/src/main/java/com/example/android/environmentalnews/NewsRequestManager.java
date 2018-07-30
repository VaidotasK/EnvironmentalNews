package com.example.android.environmentalnews;

import android.text.TextUtils;
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
import java.util.List;

import static com.example.android.environmentalnews.MainActivity.LOG_TAG;


/**
 * Helper methods for requesting and receiving Environmental news data from theguardian
 */
public final class NewsRequestManager {

    private static final String REQUEST_METHOD_GET = "GET";
    private static final int URL_CONNECTION_READ_TIMEOUT = 10000;
    private static final int URL_CONNECTION_CONNECT_TIMEOUT = 15000;
    private static final int RESPONSE_CODE_OK = 200;
    //Set default variable for author
    private static String articleAuthor = "";


    /**
     * Create a private constructor. It's purpose is only hold static variables and methods,
     * which can be accessed directly from the class name {@link NewsRequestManager}
     * and an object instance of NewsRequestManager is not needed).
     */
    private NewsRequestManager() {
    }

    /**
     * Query the theguardian data set and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);

        // Perform HTTP request and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeGetHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        // Extract relevant information from the JSON response and create a list of {@link News}
        List<News> news = extractDataFromJSON(jsonResponse);
        return news;
    }

    /**
     * Return new URL object made from given string url
     *
     * @param stringUrl - given string url
     * @return new URL object
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
            return url;
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building URL", e);
            return url;
        }
    }


    /**
     * Make an HTTP request to the given URL and return a String as response.
     *
     * @param url given URL
     * @return json response in String variable.
     * @throws IOException
     */
    private static String makeGetHttpRequest(URL url) throws IOException {
        // Check if url is null, if yes return early.
        String jsonResponse = "";
        if (null == url) {
            return jsonResponse;
        }
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(REQUEST_METHOD_GET);
            httpURLConnection.setReadTimeout(URL_CONNECTION_READ_TIMEOUT);
            httpURLConnection.setConnectTimeout(URL_CONNECTION_CONNECT_TIMEOUT);
            httpURLConnection.connect();

            // Check if the request was successful: response code = 200,
            // if yes, read the input stream and parse the response.
            if (RESPONSE_CODE_OK == httpURLConnection.getResponseCode()) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code" + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (null != httpURLConnection) {
                httpURLConnection.disconnect();
            }
            if (null != inputStream) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert {@link InputStream} into String which contains whole JSON response.
     */
    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder outputOfInpStr = new StringBuilder();
        if (null != inputStream) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (null != line) {
                outputOfInpStr.append(line);
                line = bufferedReader.readLine();
            }
        }

        //Return String containing JSON response
        return outputOfInpStr.toString();
    }


    /**
     * Return a list of {@link News} objects that have data whose were get from
     * parsing the given JSON response.
     */
    private static List<News> extractDataFromJSON(String newsJson) {
        //Check if String which should contain jsonRequest isn't empty, if yes return early
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }

        // Create an empty ArrayList to which we can add News objects
        List<News> news = new ArrayList<>();

        // Try to parse String with JSONresponse. If there's a problem with how JSON
        // is formatted, a JSONException exception object will be thrown.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject root = new JSONObject(newsJson);

            // Create a JSONObject containing all information
            JSONObject response = root.getJSONObject("response");

            // Extract the JSONArray, using key called "results",
            // which shows a list of information on article.
            JSONArray results = response.getJSONArray("results");

            // For each article(new) in the resultsArray, create an {@link News} object
            for (int i = 0; i < results.length(); i++) {
                JSONObject article = results.getJSONObject(i);

                //Parse information from JSON object article: article name, section name,
                // publish date, url address and save in new String variables
                String articleTitle = article.getString("webTitle");
                String articleSectionName = article.getString("sectionName");
                String articlePublishedDate = article.getString("webPublicationDate");
                String articleUrl = article.getString("webUrl");

                //From JSONobject article create tagsArray, which represents list of tags,
                // for this case article author
                JSONArray tags = article.getJSONArray("tags");
                for (int y = 0; y < tags.length(); y++) {
                    JSONObject tagsObj = tags.getJSONObject(y);

                    //Check if author is mentioned, if yes parse author and store in new String
                    if (tagsObj.getString("webTitle") != null) {
                        articleAuthor = tagsObj.getString("webTitle");
                    }
                }
                //Create new News object with parsed data
                News environmentalNew = new News(articleTitle, articleSectionName, articlePublishedDate, articleUrl, articleAuthor);

                //Build up list with News objects
                news.add(environmentalNew);
            }
        } catch (JSONException e) {
            Log.e("NewsRequestManager", "Problem while parsing News JSON results", e);
        }
        // Return the list of news
        return news;
    }
}

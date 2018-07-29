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
 * Helper methods related to requesting and receiving Environmental news data from theguardian
 */
public final class NewsRequestManager {

    private NewsRequestManager(){
    }

    public static List<News> fetchNewsData(String requestUrl){

        URL url = createUrl (requestUrl);

        String jsonResponse = null;
        try{
            jsonResponse = makeGetHttpRequest(url);
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        List<News> news = extractDataFromJSON(jsonResponse);
        return news;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
            return url;
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building URL", e);
            return url;
        }
    }


    private static String makeGetHttpRequest(URL url) throws IOException{
        String jsonResponse = "";
        if(null == url) {
            return jsonResponse;
        }
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try{
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.connect();

            if(200 == httpURLConnection.getResponseCode()){
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            }
            else {
                Log.e(LOG_TAG, "Error response code" + httpURLConnection.getResponseCode());
            }
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.",  e);
        }
        finally {
            if(null != httpURLConnection){
                httpURLConnection.disconnect();
            }
            if(null != inputStream){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromInputStream (InputStream inputStream) throws IOException{
        StringBuilder outputOfInpStr = new StringBuilder();
        if(null != inputStream){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (null != line){
                outputOfInpStr.append(line);
                line = bufferedReader.readLine();
            }
        }
        return outputOfInpStr.toString();
    }


    private static List<News> extractDataFromJSON(String newsJson){
        if(TextUtils.isEmpty(newsJson)){
            return null;
        }
        List<News> news = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(newsJson);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for(int i = 0; i < results.length(); i++){
                JSONObject article = results.getJSONObject(i);

                String articleTitle = article.getString("WebTitle");
                String articleSectionName = article.getString("sectionName");
                String articlePublishedDate = article.getString("webPublicationDate");
                String articleUrl = article.getString("webUrl");


                JSONArray tags = article.getJSONArray("tags");
                    JSONObject tagsObj = tags.getJSONObject(0);
                   String articleAuthor = tagsObj.getString("webTitle");


                News environmentalNew = new News(articleTitle, articleSectionName, articlePublishedDate, articleUrl, articleAuthor);

                news.add(environmentalNew);            }
        }
        catch (JSONException e){
            Log.e("NewsRequestManager", "Problem while parsing News JSON results", e);
        }
        return news;
    }
}

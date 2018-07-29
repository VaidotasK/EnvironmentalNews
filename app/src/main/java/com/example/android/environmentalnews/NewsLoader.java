package com.example.android.environmentalnews;

import android.content.AsyncTaskLoader;
import android.content.Context;


import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /** Tag used for log messages */
    private static final String LOG_TAG = NewsLoader.class.getName();

    /** Query URL */
   private String url;


  public NewsLoader(Context context, String url){
       super(context);
       this.url = url;
   }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if(null == url){
          return null;
        }
        return  NewsRequestManager.fetchNewsData(url);
    }
}

package com.example.android.environmentalnews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /** Tag used for log messages */
    private static final String LOG_TAG = NewsLoader.class.getName();

    /** Query URL */
   private String url;


   NewsLoader(Context context, String url){
       super(context);
       this.url = url;
   }

    @Nullable
    @Override
    public List<News> loadInBackground() {
        if(null == url){
          return null;
        }
        return NewsRequestManager.fetchNewsData;
    }
}

package com.example.android.environmentalnews;



import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{

    public static final String LOG_TAG = MainActivity.class.getName();

    private static final int NEWS_LOADER_IN = 1;

    private NewsAdapter adapter;

    private TextView emptyStateTextView;

    private ProgressBar progressBar;

    private static final String URL_TO_JSON_DATA = "http://content.guardianapis.com/search?section=environment&page-size=99&order-by=newest&show-tags=contributor&q=climate%20change&api-key=e770d08a-16eb-438e-b077-93eee8193153";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(null != activeNetwork && activeNetwork.isConnected()){
            getLoaderManager().initLoader(NEWS_LOADER_IN, null, this);
        }
        else {
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
            emptyStateTextView = (TextView) findViewById(R.id.empty_state_textView);
            emptyStateTextView.setText("No Internet connection");
        }

        ListView newsListView = (ListView) findViewById(R.id.list);

        emptyStateTextView =   (TextView) findViewById(R.id.empty_state_textView);
        newsListView.setEmptyView(emptyStateTextView);

        adapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(adapter);


        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNew = adapter.getItem(position);

                Uri newsUri = Uri.parse(currentNew.getArticleUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });
    }

    @Override

    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this, URL_TO_JSON_DATA);
    }


    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        emptyStateTextView.setText("No article about environment was found");
        adapter.clear();

        if(null != news && !news.isEmpty()){
            adapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

        adapter.clear();
    }
}

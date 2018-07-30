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


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Constant value for Loader ID
     */

    private static final int NEWS_LOADER_IN = 1;
    /**
     * URL for article(new) data from the theguardian data set
     */
    private static final String URL_TO_JSON_DATA = "https://content.guardianapis.com/search?section=environment&page-size=200&order-by=newest&show-tags=contributor&q=climate%20change&api-key=e770d08a-16eb-438e-b077-93eee8193153";
    /**
     * Adapter for the list of news
     */
    private NewsAdapter adapter;
    /**
     * TextView that is displayed when the list is empty
     */
    private TextView emptyStateTextView;
    /**
     * Progress Bar that is displayed while waiting data form theguardian page
     */
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (null != activeNetwork && activeNetwork.isConnected()) {
            getLoaderManager().initLoader(NEWS_LOADER_IN, null, this);
        }
        //If device can't connect to the internet show msg: no internet
        else {
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
            emptyStateTextView = findViewById(R.id.empty_state_textView);
            emptyStateTextView.setText(R.string.msg_no_internet);

        }

        ListView newsListView = findViewById(R.id.list);

        emptyStateTextView = findViewById(R.id.empty_state_textView);
        newsListView.setEmptyView(emptyStateTextView);

        adapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(adapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website for full article on theguardian page
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNew = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNew.getArticleUrl());

                // Create a new intent to view the article URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });
    }

    @Override

    // Create a new loader for the given URL
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this, URL_TO_JSON_DATA);
    }


    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {

        //Hide loading indicator because articles were loaded
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found"
        emptyStateTextView.setText(R.string.msg_no_articles_found);

        // Clear the adapter of previous earthquake data
        adapter.clear();


        // If there is a right/valid list of {@link News} obj, then add them to the adapter's
        // data set. Which will trigger the ListView to update.
        if (null != news && !news.isEmpty()) {
            adapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

        // Loader reset, we can clear out our existing data.
        adapter.clear();
    }
}

package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAppActivity extends AppCompatActivity
        implements LoaderCallbacks <ArrayList<NewsItem>>{
    private NewsItemAdapter newsItemAdapter;
    private TextView emptyTextView;
    private ProgressBar progressView;
    private ListView newsItemsListView;
    private static final int NEWS_ITEM_LOADER_ID = 1;
    private static final String GUARDIAN_API_URL =
            "https://content.guardianapis.com/search?";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_app_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.news_action_settings) {
            Intent settingsIntent = new Intent(this, NewsAppSettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_app_activity);

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        newsItemAdapter = new NewsItemAdapter(this, new ArrayList<NewsItem>());
        newsItemsListView = findViewById(R.id.news_items_list);
        newsItemsListView.setAdapter(newsItemAdapter);
        progressView = findViewById(R.id.news_items_loading_indicator);


        if (isConnected){
            LoaderManager myLoaderManager = getLoaderManager();
            myLoaderManager.initLoader(NEWS_ITEM_LOADER_ID, null, this);
            newsItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NewsItem currentNewsItem = newsItemAdapter.getItem(position);
                    Uri newsItemUrl = Uri.parse(currentNewsItem.getWebUrl());
                    Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, newsItemUrl);
                    startActivity(openUrlIntent);
                }
            });
        }else{
            emptyTextView = findViewById(R.id.no_news);
            emptyTextView.setText(R.string.no_news);
            newsItemsListView.setEmptyView(emptyTextView);
            progressView.setVisibility(View.GONE);

        }
    }

    @Override
    public Loader<ArrayList<NewsItem>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String pageSize = sharedPrefs.getString(
                getString(R.string.settings_items_per_page_key),
                getString(R.string.settings_items_per_page_default));

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_API_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //add query param page-size
        uriBuilder.appendQueryParameter(getString(R.string.page_size), pageSize);
        // Append query parameter api-key to url.
        uriBuilder.appendQueryParameter(getString(R.string.api_key), getString(R.string.api_key_value));


        // Return the completed uri `https://content.guardianapis.com/search?"api-key=91f5184b-3d15-4c77-b1b8-662f584d7776'
        return new NewsItemLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NewsItem>> loader, ArrayList<NewsItem> newsItems) {
        progressView.setVisibility(View.GONE);
        newsItemAdapter.clear();
        if (newsItems != null && !newsItems.isEmpty()) {
            newsItemAdapter.addAll(newsItems);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NewsItem>> loader) {
        newsItemAdapter.clear();
    }
}

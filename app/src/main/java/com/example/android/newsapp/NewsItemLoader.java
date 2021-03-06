package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class NewsItemLoader extends AsyncTaskLoader<ArrayList<NewsItem>> {
    private String myUrl;
    public NewsItemLoader(Context context, String url) {
        super(context);
        myUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<NewsItem> loadInBackground() {
        if(myUrl == null){
            return null;
        }
        final ArrayList<NewsItem> newsItems = QueryUtils.extractNewsItems(myUrl);
        return newsItems;
    }
}
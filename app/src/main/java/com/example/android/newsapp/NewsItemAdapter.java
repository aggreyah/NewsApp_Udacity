package com.example.android.newsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsItemAdapter extends ArrayAdapter<NewsItem> {
    public NewsItemAdapter(Activity context, ArrayList<NewsItem> newsItems) {
        super(context, 0, newsItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);
        }

        NewsItem currentNewsItem = getItem(position);

        TextView sectionNameTextView =  convertView.findViewById(R.id.section_name);
        String sectionNameText = currentNewsItem.getSectionName();
        sectionNameTextView.setText(sectionNameText);

        TextView webTitleTextView =  convertView.findViewById(R.id.web_title);
        String webTitleText = currentNewsItem.getWebTitle();
        webTitleTextView.setText(webTitleText);

        TextView newsTypeTextView =  convertView.findViewById(R.id.news_type);
        String newsTypeText = currentNewsItem.getNewsType();
        newsTypeTextView.setText(newsTypeText);

        TextView yearOfPublicationTextView =  convertView.findViewById(R.id.web_publication_year);
        int yearOfPublicationText = currentNewsItem.getPublicationYear();
        yearOfPublicationTextView.setText(String.valueOf(yearOfPublicationText));

        TextView monthOfPublicationTextView =  convertView.findViewById(R.id.web_publication_month);
        int monthOfPublicationText = currentNewsItem.getPublicationMonth();
        monthOfPublicationTextView.setText(String.valueOf(monthOfPublicationText));

        TextView dayOfPublicationTextView =  convertView.findViewById(R.id.web_publication_day_of_the_month);
        int dayOfPublicationText = currentNewsItem.getPublicationDayOfMonth();
        dayOfPublicationTextView.setText(String.valueOf(dayOfPublicationText));

        TextView authorTitleTextView = convertView.findViewById(R.id.author_title);
        TextView authorTextView = convertView.findViewById(R.id.author_value);
        if (currentNewsItem.getAuthor().isEmpty()) {
            authorTitleTextView.setVisibility(View.GONE);
            authorTextView.setVisibility(View.GONE);
        } else {
            String authorText = currentNewsItem.getAuthor();
            authorTextView.setText(authorText);
            authorTitleTextView.setVisibility(View.VISIBLE);
            authorTextView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

}
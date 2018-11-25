package com.example.android.newsapp;

public class NewsItem {
    //section name field
    private String mSectionName;
    //web publication date field
    private String mWebPublicationDate;
    //web title field
    private String mWebTitle;
    //web url field
    private String mWebUrl;
    //pillar Id field
    private String mNewsType;

    /**Constructor for the NewsItem object*/
    public NewsItem(String section, String date, String title, String url, String type){
        mSectionName = section;
        mWebPublicationDate = date;
        mWebTitle = title;
        mWebUrl = url;
        mNewsType = type;
    }

    /**get the news item section name*/
    public String getSectionName(){
        return mSectionName;
    }

    /**get the news item publication year*/
    public int getPublicationYear(){
        String date = mWebPublicationDate.split("T", 2)[0];
        int year = Integer.parseInt(date.split("-")[0]);
        return year;
    }

    /**get the news item publication month*/
    public int getPublicationMonth(){
        String date = mWebPublicationDate.split("T", 2)[0];
        int month = Integer.parseInt(date.split("-")[1]);
        return month;
    }

    /**get the news item publication day of the month*/
    public int getPublicationDayOfMonth(){
        String date = mWebPublicationDate.split("T")[0];
        int dayOfMonth = Integer.parseInt(date.split("-")[2]);
        return dayOfMonth;
    }

    /**get the news item web title*/
    public String getWebTitle(){
        if (mWebTitle.contains("|")){
            String title = mWebTitle.split("\\|")[0];
            return title;
        }else{
            return  mWebTitle;
        }
    }

    /**get the news item author*/
    public String getAuthor(){
        String author = "";
        if (mWebTitle.contains("|")){
            author = mWebTitle.split("\\|")[1];
            return author;
        }
        return author;
    }

    /**get the news item web url*/
    public String getWebUrl(){
        return mWebUrl;
    }

    /**get the news item news type*/
    public String getNewsType(){
        return mNewsType;
    }

}

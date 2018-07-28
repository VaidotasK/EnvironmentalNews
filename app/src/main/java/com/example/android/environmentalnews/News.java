package com.example.android.environmentalnews;

import java.security.SecureRandom;

public class News {

    private String articleName;
    private String articleSectionName;
    private String articlePublishedDate;
    private String articleUrl;


    private String articleAuthor;

    //Constant variable if no author is provided
    private static final String NO_AUTHOR = "";


    //    Full constructor to show all information of the article in list view
    public News (String articleName, String articleSectionName, String articlePublishedDate, String articleUrl, String articleAuthor) {
        this.articleName = articleName;
        this.articleSectionName = articleSectionName;
        this.articlePublishedDate = articlePublishedDate;
        this.articleUrl = articleUrl;
        this.articleAuthor = articleAuthor;
    }

    //Constructor which doesn't involve article contributor
    public News (String articleName, String articleSectionName, String articlePublishedDate, String articleUrl) {
        this.articleName = articleName;
        this.articleSectionName = articleSectionName;
        this.articlePublishedDate = articlePublishedDate;
        this.articleUrl = articleUrl;
        this.articleAuthor = NO_AUTHOR;
    }

    public String getArticleName() {
        return articleName;
    }

    public String getArticleSectionName() {
        return articleSectionName;
    }

    public String getArticlePublishedDate() {
        return articlePublishedDate;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public boolean hasAuthor(){
        return articleAuthor != NO_AUTHOR;

    }
}

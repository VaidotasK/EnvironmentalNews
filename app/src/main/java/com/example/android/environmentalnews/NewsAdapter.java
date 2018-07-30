package com.example.android.environmentalnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Constants for separators, used in article published date data
     */
    private static final String DATE_SEPARATOR = "T";
    private static final String DATE_EXTRA_CHAR = "Z";

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (null == convertView) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News currentNew = getItem(position);

        //Set text for article title
        TextView articleTitleTextView = convertView.findViewById(R.id.article_title_textView);
        articleTitleTextView.setText(currentNew.getArticleTitle());

        //Set text for article section name
        TextView articleSectionName = convertView.findViewById(R.id.section_name_textView);
        articleSectionName.setText(currentNew.getArticleSectionName());


        TextView articlePublishDate = convertView.findViewById(R.id.published_data_textView);


        /**Create private variable for published date, which comes in format (yyyy-mm-ddThh:mm:ssZ)*/
        String fullPublishDate = currentNew.getArticlePublishedDate();

        StringBuilder dateTimeStringBuilder = new StringBuilder();

        /**String array variable for date and time, after {@link fullPublishDate} is split*/
        String[] dataTimeParts;

        //Deletes Z from {@link fullPublishDate}
        if (fullPublishDate.contains(DATE_EXTRA_CHAR)) {
            dataTimeParts = fullPublishDate.split(DATE_EXTRA_CHAR);
            fullPublishDate = dataTimeParts[0];
        }

        //Spilt {@link fullPublishDate} into date format yyyy:mm:dd and time hh:mm:ss
        if (fullPublishDate.contains(DATE_SEPARATOR)) {
            dataTimeParts = fullPublishDate.split(DATE_SEPARATOR);
            dateTimeStringBuilder.append(dataTimeParts[0] + "\n" + dataTimeParts[1]);
        }

            /*Set text for published date/time at different lines
            yyyy:mm:dd
            hh:mm:ss
             */
        articlePublishDate.setText(dateTimeStringBuilder.toString());


        //Check if article has author, if yes set text to TextView
        TextView articleAuthor = convertView.findViewById(R.id.author_textView);
        if (currentNew.hasAuthor()) {
            articleAuthor.setText(getContext().getString(R.string.by) + currentNew.getArticleAuthor());
        }

        return convertView;
    }
}

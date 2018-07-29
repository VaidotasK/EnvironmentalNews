package com.example.android.environmentalnews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    private static final String DATE_SEPARATOR = "T";
    private static final String DATE_EXTRA_CHAR = "Z";

    public NewsAdapter (Context context, List<News> news){
        super(context, 0, news);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(null == convertView){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News currentNew = getItem(position);

        TextView articleTitleTextView = convertView.findViewById(R.id.article_title_textView);
        articleTitleTextView.setText(currentNew.getArticleTitle());

        TextView articleSectionName = convertView.findViewById(R.id.section_name_textView);
        articleSectionName.setText(currentNew.getArticleSectionName());



        TextView articlePublishDate = convertView.findViewById(R.id.published_data_textView);


        String fullPublishDate = currentNew.getArticlePublishedDate();

        StringBuilder dateTimeStringBuilder = new StringBuilder();

        String[] dataTimeParts;

        if (fullPublishDate.contains(DATE_EXTRA_CHAR)) {
            dataTimeParts = fullPublishDate.split(DATE_EXTRA_CHAR);
            fullPublishDate = dataTimeParts[0];
        }
        if (fullPublishDate.contains(DATE_SEPARATOR)) {
            dataTimeParts = fullPublishDate.split(DATE_SEPARATOR);
                dateTimeStringBuilder.append(dataTimeParts [0] + "\n" + dataTimeParts[1]);
            }
        articlePublishDate.setText(dateTimeStringBuilder.toString());


        TextView articleAuthor = convertView.findViewById(R.id.author_textView);
        if(currentNew.hasAuthor()){
            articleAuthor.setText(currentNew.getArticleAuthor());
        }

        return convertView;
    }
}

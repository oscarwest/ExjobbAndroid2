package com.example.owe_macpro.exjobbandroid2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by owe-macpro on 05/04/16.
 */
public class RssFeedArrayAdapter extends ArrayAdapter<RssItem> {

    public RssFeedArrayAdapter(Context context, List<RssItem> rssItems) {
        super(context, R.layout.listrow_rss_feed, rssItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View listView = inflater.inflate(R.layout.listrow_rss_feed, parent, false);

        TextView feedItemTitle = (TextView) listView.findViewById(R.id.feedItemTitle);
        TextView feedItemDescription = (TextView) listView.findViewById(R.id.feedItemDescription);

        String title = getItem(position).getTitle();
        String description = getItem(position).getDescription();

        feedItemTitle.setText(title);
        feedItemDescription.setText(description.substring(0, 100));

        return listView;
    }
}

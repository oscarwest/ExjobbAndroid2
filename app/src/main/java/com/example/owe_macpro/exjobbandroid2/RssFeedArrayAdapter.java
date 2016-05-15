package com.example.owe_macpro.exjobbandroid2;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by owe-macpro on 05/04/16.
 */
public class RssFeedArrayAdapter extends ArrayAdapter<RssItem> {

    private Context context;

    public RssFeedArrayAdapter(Context context, List<RssItem> rssItems) {
        super(context, R.layout.listrow_rss_feed, rssItems);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View listView = inflater.inflate(R.layout.listrow_rss_feed, parent, false);

        ImageView feedItemImage = (ImageView) listView.findViewById(R.id.feedItemImage);
        TextView feedItemTitle = (TextView) listView.findViewById(R.id.feedItemTitle);
        TextView feedItemDescription = (TextView) listView.findViewById(R.id.feedItemDescription);

        String title = getItem(position).getTitle();
        String description = getItem(position).getDescription();
        String imageUrl = getItem(position).getEnclosure_url();

        feedItemTitle.setText(title);
        feedItemDescription.setText(description.substring(0, 100));

        // Get the image
        Picasso.with(context).load(imageUrl).into(feedItemImage);
        //feedItemImage.setImageResource(imageUrl);

        return listView;
    }
}

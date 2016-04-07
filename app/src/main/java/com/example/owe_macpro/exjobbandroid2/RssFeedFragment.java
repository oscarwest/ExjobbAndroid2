package com.example.owe_macpro.exjobbandroid2;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.List;


public class RssFeedFragment extends Fragment {

    private ListView rssListView;
    private TextView feedTitle;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_rss_feed, container, false);

        feedTitle = (TextView) v.findViewById(R.id.feedTitle);
        rssListView = (ListView) v.findViewById(R.id.feedListView);

        // Parse rss feed
        List<RssItem> rssItems = null;

        try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            InputStream is = getResources().openRawResource(R.raw.agenda);
            rssItems = parser.parse(is);

            rssListView.setAdapter(new RssFeedArrayAdapter(getActivity().getApplicationContext(), rssItems));
            feedTitle.setText(rssItems.get(0).getFeed_title());
        } catch (Exception exception) {
            Log.d("RSS FEED ERROR: ", exception.toString());
            exception.printStackTrace();
        }

        return v;
    }

    public static RssFeedFragment newInstance() {

        RssFeedFragment f = new RssFeedFragment();
        Bundle b = new Bundle();
        b.putString("msg", "Testing123");

        f.setArguments(b);

        return f;
    }

}



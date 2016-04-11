package com.example.owe_macpro.exjobbandroid2;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class RssFeedFragment extends Fragment implements View.OnClickListener {

    private ListView rssListView;
    private TextView feedTitle;
    private View v;
    private List<RssItem> rssItems;
    private long lStartTime;
    private long lStopTime;
    private TextView executionTimeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_rss_feed, container, false);

        feedTitle = (TextView) v.findViewById(R.id.feedTitle);
        rssListView = (ListView) v.findViewById(R.id.feedListView);

        Button executeRssBtn = (Button) v.findViewById(R.id.run_rss_btn);
        Button resetRssBtn = (Button) v.findViewById(R.id.reset_rss_btn);
        executionTimeView = (TextView) v.findViewById(R.id.feedExecutionTime);

        executeRssBtn.setOnClickListener(this);
        resetRssBtn.setOnClickListener(this);

        // Initialize rss feed
        rssItems = null;

        return v;
    }

    @Override
    public void onClick(View v) {
        Log.d("WHAAT", String.valueOf(v.getId()));

        switch (v.getId()) {
            case R.id.run_rss_btn:
                // Start timer
                lStartTime = System.nanoTime();

                // Parse xml
                try {
                    XMLPullParserHandler parser = new XMLPullParserHandler();
                    InputStream is = getResources().openRawResource(R.raw.agenda);
                    rssItems = parser.parse(is);

                    rssListView.setAdapter(new RssFeedArrayAdapter(getActivity().getApplicationContext(), rssItems));
                    feedTitle.setText(rssItems.get(0).getFeed_title());

                    // Stop timer
                    lStopTime = System.nanoTime();
                    double rssFeedExecutionTime = ((double)lStopTime - (double)lStartTime) / 1000000;
                    executionTimeView.setText(String.valueOf(rssFeedExecutionTime) + "ms");
                    Log.d("CREATION", "RSS execution time: " + String.valueOf(rssFeedExecutionTime) + "ms");

                } catch (Exception exception) {
                    Log.d("RSS FEED ERROR: ", exception.toString());
                    exception.printStackTrace();
                }
                break;

            case R.id.reset_rss_btn:
                rssItems = new ArrayList<RssItem>();
                rssListView.setAdapter(new RssFeedArrayAdapter(getActivity().getApplicationContext(), rssItems));
                feedTitle.setText("Reset");
                executionTimeView.setText("0ms");
                break;
        }

    }

    public static RssFeedFragment newInstance() {

        RssFeedFragment f = new RssFeedFragment();
        Bundle b = new Bundle();
        b.putString("msg", "Testing123");

        f.setArguments(b);

        return f;
    }

}



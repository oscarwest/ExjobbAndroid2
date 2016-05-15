package com.example.owe_macpro.exjobbandroid2;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
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

    private NonScrollListView rssListView;
    private TextView feedTitle;
    private View v;
    private List<RssItem> rssItems;
    private long lStartTime;
    private long lStopTime;
    private TextView executionTimeView;
    private Handler handler;
    private Runnable runnable;
    private Boolean runnableActive = false;
    private int testCount = 0;
    private int rssFileResource = R.raw.rss10;
    private String rssFeedLength = "10";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_rss_feed, container, false);

        feedTitle = (TextView) v.findViewById(R.id.feedTitle);
        rssListView = (NonScrollListView) v.findViewById(R.id.feedListView);

        final Button executeRssBtn = (Button) v.findViewById(R.id.run_rss_btn);
        Button resetRssBtn = (Button) v.findViewById(R.id.reset_rss_btn);
        executionTimeView = (TextView) v.findViewById(R.id.feedExecutionTime);

        executeRssBtn.setOnClickListener(this);
        resetRssBtn.setOnClickListener(this);

        // Initialize rss feed
        rssItems = null;

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                // Reset scrolllist
                rssListView = null;
                rssListView = (NonScrollListView) v.findViewById(R.id.feedListView);
                executeRssBtn.performClick();
                testCount++;
                handler.postDelayed(this, 700);
            }
        };

        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.run_rss_btn:
                // Start timer
                lStartTime = System.nanoTime();

                // Parse xml
                try {
                    XMLPullParserHandler parser = new XMLPullParserHandler();
                    InputStream is = getResources().openRawResource(rssFileResource);
                    rssItems = parser.parse(is);

                    rssListView.setAdapter(new RssFeedArrayAdapter(getActivity().getApplicationContext(), rssItems));
                    feedTitle.setText(rssItems.get(0).getFeed_title());

                    // Stop timer
                    lStopTime = System.nanoTime();
                    double rssFeedExecutionTime = ((double)lStopTime - (double)lStartTime) / 1000000;
                    executionTimeView.setText(String.valueOf(rssFeedExecutionTime) + "ms");
                    Log.d("CREATION", "RSS execution time: " + String.valueOf(rssFeedExecutionTime) + "ms");
                    // Add to db
                    SimpleHttpPost postClass = new SimpleHttpPost();
                    postClass.setPostParameters("app_type=android&app_function=rss&feed_length=" + rssFeedLength.toString() + "&exec_time=" + Double.toString(rssFeedExecutionTime));
                    postClass.httpPost();

                    // Automate data collection, initialize runnable only the first time we click it
                    if (runnableActive == false) {
                        runnableActive = true;
                        handler.postDelayed(runnable, 1000);
                    }

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
                handler.removeCallbacks(runnable);
                runnableActive = false;
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



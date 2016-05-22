package com.example.owe_macpro.exjobbandroid2;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class RssFeedFragment extends Fragment implements View.OnClickListener {

    private NonScrollListView rssListView;
    private TextView feedTitle;
    private View v;
    private long lStartTime;
    private long lStopTime;
    private TextView executionTimeView;
    private int testCount = 0;
    private int rssFileResource = R.raw.rss100;
    private String rssFeedLength = "100";
    private Button executeRssBtn;
    private Button resetRssBtn;
    private Handler handler;
    private Runnable runnable;
    private AsyncTask<Void, Void, List<RssItem>> rssFeedTask;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_rss_feed, container, false);

        // Grab views
        feedTitle = (TextView) v.findViewById(R.id.feedTitle);
        rssListView = (NonScrollListView) v.findViewById(R.id.feedListView);
        executeRssBtn = (Button) v.findViewById(R.id.run_rss_btn);
        resetRssBtn = (Button) v.findViewById(R.id.reset_rss_btn);
        executionTimeView = (TextView) v.findViewById(R.id.feedExecutionTime);

        // Add eventlisteners
        executeRssBtn.setOnClickListener(this);
        resetRssBtn.setOnClickListener(this);

        // Set default values
        executionTimeView.setText("");

        // Define handler and runnable for data gathering automation
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                executeRssBtn.performClick();
            }
        };

        return v;
    }

    @Override
    public void onClick(View v) {
        // Declare asynctask so we can cancel it with a reset button
        switch (v.getId()) {
            case R.id.run_rss_btn:
                // Create asynctask to do the calc and post to db and automate
                rssFeedTask = new AsyncTask<Void, Void, List<RssItem>>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        Log.d("AsyncTask", "Preparing Feed Parsing..");

                        // Start timer
                        lStartTime = System.nanoTime();
                    }

                    @Override
                    protected List<RssItem> doInBackground(Void... params) {
                        Log.d("AsyncTask", "Doing Feed Parsing..");
                        List<RssItem> rssItems = new ArrayList<>();
                        // Parse xml
                        try {
                            XMLPullParserHandler parser = new XMLPullParserHandler();
                            InputStream is = getResources().openRawResource(rssFileResource);
                            rssItems = parser.parse(is);
                            testCount++;
                        } catch (Exception exception) {
                            Log.d("RSS FEED ERROR: ", exception.toString());
                            exception.printStackTrace();
                        }

                        return rssItems;
                    }

                    @Override
                    protected void onPostExecute(List<RssItem> rssItems) {
                        Log.d("AsyncTask", "Updating UI with Feed..");

                        rssListView.setAdapter(new RssFeedArrayAdapter(getActivity().getApplicationContext(), rssItems));
                        feedTitle.setText(rssItems.get(0).getFeed_title());

                        // Stop timer
                        lStopTime = System.nanoTime();

                        // Convert the execution time to double from long
                        double rssFeedExecutionTime = ((double)lStopTime - (double)lStartTime) / 1000000;
                        executionTimeView.setText(Double.toString(rssFeedExecutionTime) + "ms");

                        Log.d("CREATION", "RSS execution time: " + Double.toString(rssFeedExecutionTime) + "ms");
                        // Add to db
                        SimpleHttpPost postClass = new SimpleHttpPost();
                        postClass.setPostParameters("app_type=android&app_function=rss&feed_length=" + rssFeedLength.toString() + "&exec_time=" + Double.toString(rssFeedExecutionTime));
                        postClass.httpPost();

                        // Automate data collection
                        if (testCount < 1000) {
                            handler.postDelayed(runnable, 1000);
                        }
                    }
                };

                // Execute the task
                if(Build.VERSION.SDK_INT >= 11)
                    rssFeedTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    rssFeedTask.execute();

                break;

            case R.id.reset_rss_btn:
                rssFeedTask.cancel(true);
                handler.removeCallbacks(runnable);
                rssListView.setAdapter(new RssFeedArrayAdapter(getActivity().getApplicationContext(), new ArrayList<RssItem>()));
                feedTitle.setText("Reset");
                executionTimeView.setText("");
                testCount = 0;
                break;
        }
    }

    public static RssFeedFragment newInstance() {

        RssFeedFragment f = new RssFeedFragment();
        Bundle b = new Bundle();

        f.setArguments(b);

        return f;
    }

}



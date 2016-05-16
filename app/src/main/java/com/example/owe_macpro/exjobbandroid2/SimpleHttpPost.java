package com.example.owe_macpro.exjobbandroid2;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by owe-macpro on 09/05/16.
 */
public class SimpleHttpPost {

    private String postParameters;
    private URL urlToRequest;

    public void setPostParameters(String postParameters) {
        this.postParameters = postParameters;
    }

    public SimpleHttpPost() {
        // Set the url
        try {
            this.urlToRequest = new URL("http://192.168.1.109:80/exjobb-data-api/index.php");
        } catch (MalformedURLException e) {
            Log.d("DEBUG", "Incorrect URL set");
            e.printStackTrace();
        }
    }

    public void httpPost() {
        // Create asynctask and do http post to mysql
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d("AsyncTask", "Preparing DB Post..");
            }
            @Override
            protected String doInBackground(Void... params) {
                Log.d("AsyncTask", "Doing DB Post..");
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);

                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(postParameters);
                    out.close();

                    // handle issues
                    int statusCode = urlConnection.getResponseCode();
                    if (statusCode != HttpURLConnection.HTTP_OK) {
                        Log.d("DEBUG", "Failed to post to server");
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
            @Override
            protected void onPostExecute(String msg) {
                Log.d("AsyncTask", "Finished DB Post..");

            }
        };

        // Execute the task
        if(Build.VERSION.SDK_INT >= 11)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
    }
}

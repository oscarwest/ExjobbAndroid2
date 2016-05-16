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
import android.widget.ListView;

import java.util.ArrayList;


public class PrimeCalcFragment extends Fragment implements View.OnClickListener {

    private ListView primeNumberListView;
    private TextView primeNumberResultTextView;
    private int testCount = 0;
    private Button calculatePrimesButton;
    private Button resetPrimesButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_prime_calc, container, false);

        // Grab views
        primeNumberListView = (ListView) v.findViewById(R.id.primeNumberListView);
        primeNumberResultTextView = (TextView) v.findViewById(R.id.resultsTextView);

        // Initialize view components
        primeNumberListView.setAdapter(new PrimeCalcArrayAdapter(getActivity().getApplicationContext(), new ArrayList<Integer>()));
        primeNumberResultTextView.setText("");

        // Add listeners to buttons
        calculatePrimesButton = (Button) v.findViewById(R.id.run_calc_btn);
        resetPrimesButton = (Button) v.findViewById(R.id.reset_calc_btn);

        calculatePrimesButton.setOnClickListener(this);
        resetPrimesButton.setOnClickListener(this);


        return v;
    }

    public static PrimeCalcFragment newInstance() {
        PrimeCalcFragment f = new PrimeCalcFragment();
        Bundle b = new Bundle();

        f.setArguments(b);

        return f;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.run_calc_btn:

                // Create asynctask to do the calc and post to db and automate
                AsyncTask<Void, Void, PrimeNumbers> task = new AsyncTask<Void, Void, PrimeNumbers>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        Log.d("AsyncTask", "Preparing Calculation..");
                    }

                    @Override
                    protected PrimeNumbers doInBackground(Void... params) {
                        Log.d("AsyncTask", "Doing Calculation..");
                        PrimeNumbers PrimeNumbers = new PrimeNumbers();
                        PrimeNumbers.setPrimeLimit(100);
                        PrimeNumbers.generatePrimes();
                        testCount++;

                        return PrimeNumbers;
                    }

                    @Override
                    protected void onPostExecute(PrimeNumbers PrimeNumbers) {
                        Log.d("AsyncTask", "Finished Calculation..");

                        primeNumberResultTextView.setText(Double.toString(PrimeNumbers.getExecutionTime()) + "ms");

                        // Add to db
                        SimpleHttpPost postClass = new SimpleHttpPost();
                        postClass.setPostParameters("primes="+Integer.toString(PrimeNumbers.getPrimeLimit())+"&app_type=android&app_function=prime&exec_time="+Double.toString(PrimeNumbers.getExecutionTime()));
                        postClass.httpPost();

                        // Automate data collection
                        if (testCount < 1000) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    calculatePrimesButton.performClick();
                                }
                            }, 1000);
                        }
                    }
                };

                // Execute the task
                if(Build.VERSION.SDK_INT >= 11)
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    task.execute();

                break;
            case R.id.reset_calc_btn:
                Log.d("RESETBTN", "Resetting primes");
                primeNumberListView.setAdapter(new PrimeCalcArrayAdapter(getActivity().getApplicationContext(), new ArrayList<Integer>()));
                primeNumberResultTextView.setText("");
                break;

        }
    }
}

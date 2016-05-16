package com.example.owe_macpro.exjobbandroid2;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by owe-macpro on 16/05/16.
 */
public class PrimeNumbers {

    private int primeLimit;
    private double executionTime;
    private ArrayList<Integer> primesList;

    public PrimeNumbers() {
    }

    public void generatePrimes() {
        // sieve of eratosthenes
        final int numPrimes = countPrimesUpperBound(primeLimit);
        primesList = new ArrayList<Integer>(numPrimes);
        boolean [] isComposite = new boolean [primeLimit];   // all false
        final int sqrtLimit = (int)Math.sqrt(primeLimit); // floor
        long lStartTime = System.nanoTime();

        for (int i = 2; i <= sqrtLimit; i++) {
            if (!isComposite [i]) {
                primesList.add(i);
                for (int j = i*i; j < primeLimit; j += i) // `j+=i` can overflow
                    isComposite [j] = true;
            }
        }
        for (int i = sqrtLimit + 1; i < primeLimit; i++)
            if (!isComposite [i])
                primesList.add(i);

        long lEndTime = System.nanoTime();
        long temp = lEndTime - lStartTime;
        setExecutionTime((double) temp / 1000000);
        Log.d("CREATION", "Prime execution time: " + Double.toString(getExecutionTime()) + "ms");

    }

    public double getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime;
    }

    public int getPrimeLimit() {
        return primeLimit;
    }

    private static int countPrimesUpperBound(int max) {
        return max > 1 ? (int)(1.25506 * max / Math.log((double)max)) : 0;
    }

    public void setPrimeLimit(int primeLimit) {
        this.primeLimit = primeLimit;
    }
}

package com.example.owe_macpro.exjobbandroid2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by owe-macpro on 05/04/16.
 */
public class PrimeCalcArrayAdapter extends ArrayAdapter<Integer> {

    public PrimeCalcArrayAdapter(Context context, ArrayList<Integer> primeNumbers) {
        super(context, R.layout.listrow_prime_calc, primeNumbers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View listView = inflater.inflate(R.layout.listrow_prime_calc, parent, false);

        TextView textViewRow = (TextView) listView.findViewById(R.id.textViewRow);
        Integer singlePrimeNumber = getItem(position);
        textViewRow.setText(singlePrimeNumber.toString());

        return listView;
    }
}

package com.example.jeffster.cronch;

import android.app.Activity;
import android.bluetooth.le.AdvertiseData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Spinner exercise1, exercise2;
    private EditText edit1, edit2;
    private TextView calories, burgers;
    private ArrayAdapter<CharSequence> adapter1, adapter2;
    private String[] myExercises;
    private int[] myRates;
    private static final int maxVal = 999999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myExercises = getResources().getStringArray(R.array.exercises);
        myRates = getResources().getIntArray(R.array.rates);

        adapter1 = ArrayAdapter.createFromResource(this, R.array.exercises,
                android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.exercises,
                android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        exercise1 = (Spinner) findViewById(R.id.exercise1);
        exercise2 = (Spinner) findViewById(R.id.exercise2);
        edit1 = (EditText) findViewById(R.id.e1Amt);
        edit2 = (EditText) findViewById(R.id.e2Amt);
        calories = (TextView) findViewById(R.id.calories);
        burgers = (TextView) findViewById(R.id.burgers);

        edit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateNumbers(R.id.e1Amt);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateNumbers(R.id.e2Amt);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        exercise1.setAdapter(adapter1);
        exercise2.setAdapter(adapter2);
        exercise1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> Parent, View view, int pos, long id) {
                updateNumbers(R.id.exercise1);
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });
        exercise2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> Parent, View view, int pos, long id) {
                updateNumbers(R.id.exercise2);
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateNumbers(int idChanged) {
        int e1 = myRates[exercise1.getSelectedItemPosition()];
        int e2 = myRates[exercise2.getSelectedItemPosition()];

        if (edit1.getText().toString().length() > 0 || edit2.getText().toString().length() > 0) {
            int v1 = edit1.getText().toString().length() > 0 ?
                    Integer.parseInt(edit1.getText().toString()) : 0;
            int v2 = edit2.getText().toString().length() > 0 ?
                    Integer.parseInt(edit2.getText().toString()) : 0;

            if ((v1 == 0 && v2 == 0) ||
                    (edit1.getText().toString().length() > 0 &&
                    edit2.getText().toString().length() > 0)) {
                if ((int) Math.ceil((e2 / (float) e1) * v1) == v2 ||
                        (int) Math.ceil((e1 / (float) e2) * v2) == v1) {return;}
            }

            if (idChanged == R.id.e2Amt || idChanged == R.id.exercise1) {
                int cals = (int) ((1 / (e2 / 100.0)) * v2);
                calories.setText(Integer.toString(cals));
                burgers.setText(Integer.toString((int) (cals / 670.0)));
                int convertedAmount = Math.min((int) Math.ceil((e1 / (float) e2) * v2), maxVal);
                if (v2 < maxVal) { edit1.setText(Integer.toString(convertedAmount)); }
            } else if (idChanged == R.id.e1Amt || idChanged == R.id.exercise2) {
                int cals = (int) ((1 / (e1 / 100.0)) * v1);
                burgers.setText(Integer.toString((int) (cals / 670.0)));
                calories.setText(Integer.toString(cals));
                int convertedAmount = Math.min((int) Math.ceil((e2 / (float) e1) * v1), maxVal);
                if (v1 < maxVal) { edit2.setText(Integer.toString(convertedAmount)); }
            }

            calories.setTextSize(110 - calories.getText().length() * 10);
        }
    }
}

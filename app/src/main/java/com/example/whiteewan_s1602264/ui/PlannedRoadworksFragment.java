package com.example.whiteewan_s1602264.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.example.whiteewan_s1602264.R;
import com.example.whiteewan_s1602264.helperClasses.ParsingItems;
import com.example.whiteewan_s1602264.models.TrafficScotlandDataItem;
import com.example.whiteewan_s1602264.models.TrafficScotlandListAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Ewan White
 * S1602264
 * */

public class PlannedRoadworksFragment extends Fragment implements View.OnClickListener {

    private List<TrafficScotlandDataItem> trafficScotlandDataItemList = new ArrayList<>();
    private List<TrafficScotlandDataItem> trafficScotlandDataItemListSearch = new ArrayList<>();
    private  TrafficScotlandDataItem trafficScotlandDataItem;

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    ParsingItems itemParser = new ParsingItems();

    private ListView dataListView;
    private EditText dateSelector;
    private DatePickerDialog selector;
    private Button searchButton;

    SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.planned_roadworks_fragment, container, false);

        dataListView = view.findViewById(R.id.listview);
        dateSelector = view.findViewById(R.id.dateSelector);

        dateSelector.setInputType(InputType.TYPE_NULL);
        dateSelector.setOnClickListener(v -> {onClick(v);});

        searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(viewSearch -> { onClick(viewSearch);});

        if (isNetworkAvailable()) {
            Log.i("Planned Fragment", "Beginning Progress");
            beginProgress();

        } else {
            try
            {
                AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("Check internet connection");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable();
    }

    public void beginProgress() {

        executor.execute(() -> {

            URL urla;
            URLConnection urlCon;
            BufferedReader in = null;

            try
            {
                urla = new URL("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
                urlCon = urla.openConnection();
                in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));

                trafficScotlandDataItemList = itemParser.parseItems(urlCon.getInputStream());

                in.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            handler.post(() -> {

                Collections.shuffle(trafficScotlandDataItemList);
                TrafficScotlandListAdapter trafficScotlandListAdapter = new TrafficScotlandListAdapter(this.getContext(), trafficScotlandDataItemList);
                dataListView.setAdapter(trafficScotlandListAdapter);
            });
        });
    }


    @Override
    public void onClick (View view)
    {
        int id = view.getId();//Beginning of the search button method
        if (id == R.id.dateSelector) {
            final Calendar calander = Calendar.getInstance();
            int day = calander.get(Calendar.DAY_OF_MONTH);
            int month = calander.get(Calendar.MONTH);
            int year = calander.get(Calendar.YEAR);

            selector = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    dateSelector.setText(day + "/" + (month + 1) + "/" + year);
                }
            }, year, month, day);


            selector.show();
        } else if (id == R.id.searchButton) {
            if (dateSelector.getText().toString().length() == 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
                alertDialog.setTitle("Caution");
                alertDialog.setMessage("You must pick a date before you can search");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            } else {
                Date temporaryDate = new Date();
                try {
                    temporaryDate = newDateFormat.parse(String.valueOf(dateSelector.getText()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                trafficScotlandDataItemListSearch.clear();

                for (TrafficScotlandDataItem trafficScotlandDataItem : trafficScotlandDataItemList) {

                    int matchesStartDate = trafficScotlandDataItem.getStartDate().compareTo(temporaryDate);
                    int matchedEndDate = trafficScotlandDataItem.getEndDate().compareTo(temporaryDate);
                    boolean differenceOfDate = false;

                    if (trafficScotlandDataItem.getStartDate().before(temporaryDate) && trafficScotlandDataItem.getEndDate().after(temporaryDate)) {
                        differenceOfDate = true;
                    }

                    if (0 == matchesStartDate || 0 == matchedEndDate || differenceOfDate) {
                        trafficScotlandDataItemListSearch.add(trafficScotlandDataItem);
                    }
                }

                if (trafficScotlandDataItemListSearch.isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
                    alertDialog.setTitle("Notification");
                    alertDialog.setMessage("There are no current incidents on your selected date ");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    TrafficScotlandListAdapter trafficScotlandListAdapter = new TrafficScotlandListAdapter(this.getContext(), trafficScotlandDataItemListSearch);
                    dataListView.setAdapter(trafficScotlandListAdapter);
                }
            }
        }
    }
}
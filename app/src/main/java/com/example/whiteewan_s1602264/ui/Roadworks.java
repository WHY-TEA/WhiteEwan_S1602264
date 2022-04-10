package com.example.whiteewan_s1602264.ui;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Ewan White
 * S1602264
 * */

public class Roadworks extends Fragment implements View.OnClickListener {

    private List<TrafficScotlandDataItem> trafficScotlandDataItemList = new ArrayList<>();
    private List<TrafficScotlandDataItem> trafficScotlandDataItemListSearch = new ArrayList<>();
    private  TrafficScotlandDataItem trafficScotlandDataItem;

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    ParsingItems itemParser = new ParsingItems();

    private ListView dataListView;
    private EditText searchBar;
    private Button searchButton;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.roadworks_fragment, container, false);

        dataListView = view.findViewById(R.id.searchedListview);
        searchBar = view.findViewById(R.id.search_bar2);
        searchButton = view.findViewById(R.id.searchButtonMotorway);
        searchButton.setOnClickListener(viewSearch -> { onClick(viewSearch);});


        if (!isNetworkAvailable()) {
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
        } else {
            Log.i("Planned Fragment", "Beginning Progress");
            beginProgress();

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
                urla = new URL("https://trafficscotland.org/rss/feeds/roadworks.aspx");
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

                Log.e("Roadworks List", trafficScotlandDataItemList.toString());
                Collections.shuffle(trafficScotlandDataItemList);
                TrafficScotlandListAdapter trafficScotlandListAdapter = new TrafficScotlandListAdapter(this.getContext(), trafficScotlandDataItemList);
                dataListView.setAdapter(trafficScotlandListAdapter);
            });
        });
    }


    @Override
    public void onClick (View viewSearch) {

        if (viewSearch.getId() == R.id.searchButtonMotorway) {
            if (searchBar.getText().toString().length() == 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Text must be inserted to search");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Continue",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {
                String searchingItem = searchBar.getText().toString();
                trafficScotlandDataItemListSearch.clear();

                for (TrafficScotlandDataItem trafficScotlandDataItem : trafficScotlandDataItemList) {
                    if (trafficScotlandDataItem.getDataTitle().contains(searchingItem))
                    {
                        trafficScotlandDataItemListSearch.add(trafficScotlandDataItem);
                    }
                }

                TrafficScotlandListAdapter trafficScotlandListAdapter = new TrafficScotlandListAdapter(this.getContext(), trafficScotlandDataItemListSearch);
                dataListView.setAdapter(trafficScotlandListAdapter);

            }
        }

    }

}
package com.example.whiteewan_s1602264.models;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.whiteewan_s1602264.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/*
 * Ewan White
 * S1602264
 * */

public class TrafficScotlandListAdapter extends ArrayAdapter<TrafficScotlandDataItem> implements OnClickListener
{
    private List<TrafficScotlandDataItem> dataList;
    public TrafficScotlandDataItem dataItem;
    private Context dataContext;


    SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyy", Locale.ENGLISH);


    public TrafficScotlandListAdapter(Context dataContextIn, List<TrafficScotlandDataItem> dataListIn)
    {
        super(dataContextIn,R.layout.view_listadapter, dataListIn);
        this.dataContext = dataContextIn;
        this.dataList = dataListIn;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) dataContext.getApplicationContext().getSystemService(dataContext.LAYOUT_INFLATER_SERVICE);
        View listView = inflater.inflate(R.layout.view_listadapter, parent, false);
        TextView dataTitle = listView.findViewById(R.id.cardview_dataTitle);
        TextView dataDescription = listView.findViewById(R.id.data_description1);
        TextView furtherDetailsLink = listView.findViewById(R.id.additionalLink);



        furtherDetailsLink.setOnClickListener(view -> { onClick(view); });

        dataItem = dataList.get(pos);

        dataTitle.setText(dataList.get(pos).getDataTitle());

        return listView;
    }

    @Override
    public void onClick(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
        alertDialog.setTitle("More Information");
        alertDialog.setMessage("Description: " + dataItem.getDataDescription() +
                "Link: " + dataItem.getLink() + "\n"
                + "Publish Date: " + dataItem.getPublishedDate().toString());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}

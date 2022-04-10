package com.example.whiteewan_s1602264.helperClasses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
* Ewan White
* S1602264
* */

public class DateConfiguration
{
    private String PreviousFormattedDate = "EEE, dd MMM yyyy hh:mm:ss zzz";
    private String NewFormarmattedDate = "dd/MM/yyyy";

    SimpleDateFormat formatPreviousDate = new SimpleDateFormat(PreviousFormattedDate, Locale.ENGLISH);
    SimpleDateFormat formatNewDate = new SimpleDateFormat(NewFormarmattedDate, Locale.ENGLISH);
    SimpleDateFormat completeDate = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

    public Date parseDateData(String temporaryIn)
    {
        String temporary = temporaryIn;
        Date dateTemporary = new Date();
        try
        {
            dateTemporary = formatPreviousDate.parse(temporary);
            String secondTemporary = formatNewDate.format(dateTemporary);
            dateTemporary = formatNewDate.parse(secondTemporary);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTemporary;
    }

    public Date parseStartingDate(String dataDescription)
    {
        String startingDateString;
        Date startingDate = new Date();

        try {
            int startOfDate = dataDescription.indexOf(",");
            int endOfDate = dataDescription.indexOf("-");

            startingDateString = dataDescription.substring(startOfDate + 2, endOfDate -1);

            startingDate = completeDate.parse(startingDateString);

            String startingDateTemporary = formatNewDate.format(startingDate);
            startingDate = formatNewDate.parse(startingDateTemporary);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return startingDate;
    }

    public Date parseEndingDate(String dataDescription)
    {
        String endingDateString;
        Date endingDate = new Date();

        try {
            int startOfDate = dataDescription.indexOf(",", dataDescription.indexOf(",") +1);
            int endOfDate = dataDescription.indexOf("-", dataDescription.indexOf("-") +1);

            endingDateString = dataDescription.substring(startOfDate + 2, endOfDate -1);

            endingDate = completeDate.parse(endingDateString);

            String endingDateTemporary = formatNewDate.format(endingDate);
            endingDate = formatNewDate.parse(endingDateTemporary);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return endingDate;
    }
}



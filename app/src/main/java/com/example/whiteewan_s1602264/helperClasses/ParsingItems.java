package com.example.whiteewan_s1602264.helperClasses;

/*
 * Ewan White
 * S1602264
 * */

import android.util.Log;

import com.example.whiteewan_s1602264.models.TrafficScotlandDataItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParsingItems
{
    private TrafficScotlandDataItem dataItem;
    private List<TrafficScotlandDataItem> dataList = new ArrayList<>();
    DateConfiguration dateConfig = new DateConfiguration();

    public List<TrafficScotlandDataItem> parseItems(InputStream dataToParse) {
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(dataToParse, null);
            int eventType = parser.getEventType();
            boolean insideItemTag = false;

            if (eventType != XmlPullParser.END_DOCUMENT) {
                do {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (parser.getName().equalsIgnoreCase("item")) {
                            dataItem = new TrafficScotlandDataItem();
                            insideItemTag = true;

                        } else if (parser.getName().equalsIgnoreCase("title")) {
                            if (insideItemTag) {
                                dataItem.setDataTitle(parser.nextText());
                            }

                        } else if (parser.getName().equalsIgnoreCase("description")) {
                            if (insideItemTag) {
                                String dataDescription = parser.nextText().replaceAll("<br />", "\\\n");
                                dataItem.setDataDescription(dataDescription);

                                if (dataDescription.substring(0, 5).equalsIgnoreCase("start")) {
                                    dataItem.setStartDate(dateConfig.parseStartingDate(dataDescription));
                                    dataItem.setEndDate(dateConfig.parseEndingDate(dataDescription));

                                } else {
                                    dataItem.setStartDate(new Date());
                                    dataItem.setEndDate(new Date());
                                    dataItem.setDurationTime(1);
                                }
                            }
                        } else if (parser.getName().equalsIgnoreCase("link")) {
                            if (insideItemTag) {
                                dataItem.setLink(parser.nextText());
                            }

                        } else if (parser.getName().equalsIgnoreCase("point")) {
                            if (insideItemTag) {
                                String dataLocation = parser.nextText();

                                int space = dataLocation.indexOf(" ");
                                String latitude = dataLocation.substring(0, space);
                                String longitude = dataLocation.substring(space + 1);

                                dataItem.setLatitude(Double.parseDouble(latitude));
                                dataItem.setLongitude(Double.parseDouble(longitude));
                            }

                        } else if (parser.getName().equalsIgnoreCase("pubDate")) {
                            if (insideItemTag) {
                                Date temporaryPublishedDate = dateConfig.parseDateData(parser.nextText());

                                dataItem.setPublishedDate(temporaryPublishedDate);

                            }
                        }

                    } else if (eventType == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("item")) {
                        insideItemTag = false;
                        dataList.add(dataItem);
                        Log.i("LOOK", "parseItems: " + dataItem);
                    }
                    eventType = parser.next();
                } while (eventType != XmlPullParser.END_DOCUMENT);
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }
}

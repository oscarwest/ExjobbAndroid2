package com.example.owe_macpro.exjobbandroid2;

/**
 * Created by owe-macpro on 06/04/16.
 */
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.example.owe_macpro.exjobbandroid2.RssItem;

public class XMLPullParserHandler {
    List<RssItem> rssItems;
    private RssItem rssItem;
    private String text;
    private String feedTitle;

    public XMLPullParserHandler() {
        rssItems = new ArrayList<RssItem>();
    }

    public List<RssItem> getRssItems() {
        return rssItems;
    }

    public List<RssItem> parse(InputStream is) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(is, null);

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("item")) {
                            // create a new instance of RssItem
                            rssItem = new RssItem();
                        }

                        if (tagname.equalsIgnoreCase("enclosure")) {
                            // can only get attributes on START_TAG
                            String image_url = parser.getAttributeValue(null, "url");
                            rssItem.setEnclosure_url(image_url);
                        }

                        break;

                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("item")) {
                            // add rssItem to rssItems
                            rssItems.add(rssItem);
                        }


                        if (rssItem != null) {
                            // Attach channel info
                            rssItem.setFeed_title(feedTitle);

                            // Actual items in feed
                            if (tagname.equalsIgnoreCase("title")) {
                                rssItem.setTitle(text);
                            } else if (tagname.equalsIgnoreCase("link")) {
                                rssItem.setLink(text);
                            } else if (tagname.equalsIgnoreCase("description")) {
                                rssItem.setDescription(text);
                            }
                        } else if(rssItem == null) {
                            // Channel information pre items in feed
                            if (tagname.equalsIgnoreCase("title")) {
                                // Set Channel title in fragment
                                feedTitle = text;
                            }
                        }
                        break;

                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rssItems;
    }
}
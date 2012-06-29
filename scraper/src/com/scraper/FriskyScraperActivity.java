package com.scraper;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class FriskyScraperActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /*Document doc = null;
		try {
			doc = Jsoup.connect("http://www.google.com/").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Element tabla = doc.select("table.sched").first();
        Log.d("scraper",tabla.toString()); 
*/
        
        Document doc = null;
		try {
			doc = Jsoup.connect("http://en.wikipedia.org/").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Elements newsHeadlines = doc.select("#mp-itn b a");
        Log.d("scraper",newsHeadlines.toString()); 
    }
}
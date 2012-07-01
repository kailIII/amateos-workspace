package com.scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

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

		List<SchedShow> schedule = new ArrayList<SchedShow>();

		Document doc = null;
		Date today = new Date();
		Log.d("fecha",
				"HOY -> " + today.toGMTString() + " DIA -> " + today.getDay());
		Calendar calendar = new GregorianCalendar();
		Log.d("fecha", "CALENDAR DAY -> " + calendar.get(Calendar.DAY_OF_WEEK));
		calendar.add(Calendar.DAY_OF_WEEK, 1);
		Log.d("fecha",
				"ADD CALENDAR DAY -> " + calendar.get(Calendar.DAY_OF_WEEK));
		Log.d("fecha",
				"TIMEZONE OFFSET -> " + calendar.get(Calendar.ZONE_OFFSET)
						/ 3600000);

		try {
			doc = Jsoup.connect("http://www.friskyradio.com/").get();
			Element table = doc.select("table.sched").first();

			Elements rows = doc.select("table.sched tr");
			int dayOfShow = 1; // Table starts in Monday
			String hourOfShow = "";
			String nameOfShow = "";
			String url = "";
			Log.d("scraper", "Got " + rows.size() + " rows");
			for (int i = 1; i < rows.size(); i++) {
				Log.d("scraper", "Row #" + i + " -- " + rows.get(i).toString());
				Element row = rows.get(i);
				
				if (isDayOsShow(row.child(0))) { //row is day
					dayOfShow++;
					/*Log.d("day", "Got DAY -> " + row.child(0).text()
							+ " -- Position: " + dayOfShow);
					Log.d("calendar", "**************************");
					Log.d("calendar", "Got DAY -> " + row.child(0).text());
					Log.d("calendar", "**************************");*/
				} else { //row is show
					/*Log.d("show", "Hour -> " + row.child(0) + " -- Show -> "
							+ row.child(1));*/

					hourOfShow = row.child(0).text();
					nameOfShow = row.child(1).text();
					url = "http://www.friskyradio.com"+row.select("a").attr("href").toString();
					Log.d("scraper","URL -> "+url);
					/*Log.d("show", "Got show -> hour: " + hourOfShow
							+ " -- name: " + nameOfShow);
					Calendar showCal = calculateDateOfShow(dayOfShow,
							hourOfShow);
					Log.d("calendar", "SHOW ->" + nameOfShow);
					Log.d("calendar",
							"  - D’a: " + showCal.get(Calendar.DAY_OF_WEEK)
									+ " - " + showCal.get(Calendar.DATE) + "/"
									+ showCal.get(Calendar.MONTH) + "/"
									+ showCal.get(Calendar.YEAR));
					Log.d("calendar", "  - Hora: " + showCal.get(Calendar.HOUR)
							+ ":" + showCal.get(Calendar.MINUTE) + " "
							+ showCal.get(Calendar.AM_PM));
					Log.d("calendar", "----------------------------");*/
					
					schedule.add(new SchedShow(calculateDateOfShow(dayOfShow,
							hourOfShow), nameOfShow, url));
				}
			}
			Log.d("show","GOT "+schedule.size()+" shows.");
			for(SchedShow s : schedule){
				Log.d("show",s.toString());
			}

		} catch (IOException e) {
			Log.e("scraper", "FALLO:" + e.getMessage());
			e.printStackTrace();
		}

	}

	private Boolean isDayOsShow(Element e) {
		Boolean isDate = false;

		if (e.className() != null && !e.className().isEmpty()
				&& e.className().equals("sched_day")) {
			isDate = true;
		}

		return isDate;
	}

	private Calendar calculateDateOfShow(int dayPosition, String hour) {

		Calendar friskyCalendar = new GregorianCalendar(
				TimeZone.getTimeZone("America/Los_Angeles"));

		int offset = dayPosition - friskyCalendar.get(Calendar.DAY_OF_WEEK);
		friskyCalendar.add(Calendar.DAY_OF_WEEK, offset);

		// Calculate hour
		friskyCalendar.set(Calendar.SECOND, 0);
		friskyCalendar.set(Calendar.MINUTE, 0);

		if (hour != null && !hour.equals("")) {
			int hourValue = Integer.parseInt(hour.substring(0, 2));
			friskyCalendar.set(Calendar.HOUR, hourValue);
			String AM_PM = hour.substring(2, 4);
			if (AM_PM.equals("AM")) {
				friskyCalendar.set(Calendar.AM_PM, Calendar.AM);
			} else {
				friskyCalendar.set(Calendar.AM_PM, Calendar.PM);
			}
		} else {
			friskyCalendar.set(Calendar.HOUR, 0);
			friskyCalendar.set(Calendar.AM_PM, Calendar.AM);
		}

		Calendar local = new GregorianCalendar();
		local.setTimeInMillis(friskyCalendar.getTimeInMillis());

		return local;
	}

	/*
	 * private Calendar calculateDateOfShow(int dayPosition, String hour) {
	 * Calendar calendar = new GregorianCalendar();
	 * 
	 * Log.d("calendar","PARAM HOUR -> "+hour);
	 * Log.d("calendar","VALOR -> "+hour.substring(0, 2) +
	 * " - PM/AM -> "+hour.substring(2, 4)); // Calculate day offset int offset
	 * = dayPosition - calendar.get(Calendar.DAY_OF_WEEK); Log.d("calendar",
	 * "OFFSET DAY ->"+offset); calendar.add(Calendar.DAY_OF_WEEK, offset);
	 * Log.d("calendar", "Nuevo d’a -> "+calendar.get(Calendar.DAY_OF_WEEK));
	 * 
	 * // Calculate hour calendar.set(Calendar.SECOND, 0);
	 * calendar.set(Calendar.MINUTE, 0);
	 * 
	 * Log.d("calendar",
	 * "Init hour -> "+calendar.get(Calendar.HOUR)+":"+calendar
	 * .get(Calendar.MINUTE));
	 * 
	 * if (hour != null && !hour.equals("")) { int hourValue =
	 * Integer.parseInt(hour.substring(0, 2));
	 * Log.d("calendar","HORA A SUMAR -> "+hourValue);
	 * calendar.set(Calendar.SECOND, 0); calendar.set(Calendar.MINUTE, 0);
	 * calendar.set(Calendar.HOUR, hourValue); String AM_PM = hour.substring(2,
	 * 4); if (AM_PM.equals("AM")) { calendar.set(Calendar.AM_PM, Calendar.AM);
	 * } else { calendar.set(Calendar.AM_PM, Calendar.PM); } }else{
	 * calendar.set(Calendar.SECOND, 0); calendar.set(Calendar.MINUTE, 0);
	 * calendar.set(Calendar.HOUR, 0); calendar.set(Calendar.AM_PM,
	 * Calendar.AM); }
	 * 
	 * Log.d("calendar",
	 * "Final hour -> "+calendar.get(Calendar.HOUR)+":"+calendar
	 * .get(Calendar.MINUTE)+ " "+calendar.get(Calendar.AM_PM));
	 * 
	 * // Add TimeZone offset int friskyOffset = -7;
	 * Log.d("calendar","DAY LIGHT -> "+calendar.get(Calendar.DST_OFFSET));
	 * 
	 * Log.d("calendar","OFFSET TIMEZONE -> "+(calendar.get(Calendar.ZONE_OFFSET)
	 * /3600000)+"-"+friskyOffset+"+"+ calendar.get(Calendar.DST_OFFSET) +
	 * " = "+((calendar.get(Calendar.ZONE_OFFSET)/3600000 )- friskyOffset +
	 * calendar.get(Calendar.DST_OFFSET))); calendar.add(Calendar.HOUR,
	 * (calendar.get(Calendar.ZONE_OFFSET)/3600000) - friskyOffset +
	 * calendar.get(Calendar.DST_OFFSET)/3600000); return calendar; }
	 */
}
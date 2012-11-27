package com.scraper;

import java.util.Calendar;

public class SchedShow {

	private Calendar calendar;
	private String name;
	private String url;
	
	public SchedShow(Calendar calendar, String name, String url){
		this.calendar = calendar;
		this.name = name;
		this.url = url;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		String schedShow = "SHOW: "+name+"\n";
		schedShow += "  -- Date: "+calendar.get(Calendar.DATE)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR)+"\n";
		schedShow += "  -- Hour: "+calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND);
		if(calendar.get(Calendar.AM_PM) == Calendar.AM){
			schedShow += " AM\n";
		}else{
			schedShow += " PM\n";
		}
		schedShow += "  -- Url: "+url+"\n";
		schedShow += "-------------------------------------";
		return schedShow;
	}
	
	
}

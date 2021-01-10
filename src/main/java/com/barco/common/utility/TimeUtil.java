package com.barco.common.utility;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Nabeel Ahmed
 */
public class TimeUtil {

	public static final String estDateFormat = "MM/dd/yyyy hh:mm a";
	public static final String TIME_PATTERN = "hh:mm a";

	public static String getStringFromDate(Date date, String dateFormatPattern) {
		DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
		String dateString = dateFormat.format(date);
		return dateString;
	}

	public static Date getTime(String str) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_PATTERN);
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_PATTERN);
		try {
			return sdf.format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
    
    public static Timestamp addHoursInTimeStamp(Timestamp timestamp, int hours) {
    	if (timestamp == null){
    		return null;
    	}
    	Calendar cal = Calendar.getInstance();
	    cal.setTimeInMillis(timestamp.getTime());
	    cal.setTimeInMillis(timestamp.getTime());
	    cal.add(Calendar.HOUR, hours);
	    timestamp = new Timestamp(cal.getTime().getTime());
	    return timestamp;
    }

}

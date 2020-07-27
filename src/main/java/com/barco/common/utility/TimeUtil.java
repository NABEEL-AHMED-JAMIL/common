package com.barco.common.utility;

import java.sql.Timestamp;
import java.util.Calendar;


public class TimeUtil {
    
    public static Timestamp addHoursInTimeStamp(Timestamp timestamp, int hours) {
    	if(timestamp == null){ return null; }
    	Calendar cal = Calendar.getInstance();
	    cal.setTimeInMillis(timestamp.getTime());
	    cal.setTimeInMillis(timestamp.getTime());
	    cal.add(Calendar.HOUR, hours);
	    timestamp = new Timestamp(cal.getTime().getTime());
	    return timestamp;
    }

}

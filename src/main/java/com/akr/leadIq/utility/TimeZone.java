package com.akr.leadIq.utility;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeZone {
    ZoneId zoneId;

    public TimeZone() {
        zoneId = ZoneId.of("GMT");
    }

    public String getTimeNow(){
        ZonedDateTime date = ZonedDateTime.now(zoneId);
        return date.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}

package com.newland.property.api.listener.floor;

import com.newland.property.utils.util.DateUtil;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class QueryFloorsListenerTest {

    @Test
    public void soService() {
        Date endTime = new Date();
        Calendar endCalender = Calendar.getInstance();
        endCalender.setTime(endTime);
        endCalender.add(Calendar.HOUR, 365*24*10+3);

        System.out.printf("time "+ DateUtil.getFormatTimeString(endCalender.getTime(),DateUtil.DATE_FORMATE_STRING_A));
    }
}
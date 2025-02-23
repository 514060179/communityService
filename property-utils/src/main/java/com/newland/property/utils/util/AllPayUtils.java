package com.newland.property.utils.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AllPayUtils {
    private static final Log logger = LogFactory.getLog(AllPayUtils.class);

    public static String getDate(){
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }
    public static String getTime(){
        return new SimpleDateFormat("HHmmdd").format(new Date());
    }

    public static String getUnSignStr(String characterEncoding, SortedMap<Object,Object> parameters){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(v);
            }
        }

        return sb.toString();
    }
}

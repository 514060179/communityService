package com.newland.property.utils.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtil {

    private static Logger logger = LoggerFactory.getLogger(IpUtil.class);

    public static String getIpAddress(){
        InetAddress inetAddress = null;
        String ip = null;
        try {
            inetAddress = InetAddress.getLocalHost();
            ip = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.error("获取机器IP异常");
            return ip;
        }
        return ip;
    }
}

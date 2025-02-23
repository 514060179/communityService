package com.newland.property.tcp.cmdRequest;

/**
 * @author Moonny
 */
public class CommonHeader {

    public static StringBuilder GetCmdHeader(int length) {
        StringBuilder request = new StringBuilder();
        request.append("HTTP/1.1 200 OK").append("\r\n");
        request.append("Content_Length:").append(length).append("\r\n");
        request.append("Content-Type:application/json").append("\r\n");
        request.append("\r\n");

        return request;
    }
}

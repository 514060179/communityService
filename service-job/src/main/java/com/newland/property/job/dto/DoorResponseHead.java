package com.newland.property.job.dto;

/**
 * @author simon feng
 * @date 2024/6/2 14:31
 * @description 道尔接口响应head数据
 */
public class DoorResponseHead {
    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

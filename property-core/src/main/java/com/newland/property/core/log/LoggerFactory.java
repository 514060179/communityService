package com.newland.property.core.log;

import com.newland.property.dto.system.SystemLogDto;
import org.slf4j.Logger;
import org.slf4j.Marker;

public class LoggerFactory implements Logger {

    private final Logger logger;

    private final String logPrefix = "";

    public LoggerFactory(Logger logger) {
        this.logger = logger;
    }

    public static Logger getLogger(Class<?> clazz) {
        Logger logger = org.slf4j.LoggerFactory.getLogger(clazz);
        return new LoggerFactory(logger);
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return SystemLogDto.getLogSwatch();
    }


    @Override
    public boolean isDebugEnabled() {
        return SystemLogDto.getLogSwatch();
    }

    @Override
    public boolean isInfoEnabled() {
        return SystemLogDto.getLogSwatch();
    }

    @Override
    public boolean isErrorEnabled() {
        return SystemLogDto.getLogSwatch();
    }


    @Override
    public boolean isErrorEnabled(Marker marker) {
        return this.isDebugEnabled(marker);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return this.logger.isWarnEnabled(marker);
    }


    @Override
    public boolean isDebugEnabled(Marker marker) {
        return this.logger.isDebugEnabled(marker);
    }
    @Override
    public void trace(String message) {
        if (this.isTraceEnabled()) {
            this.logger.trace(this.logPrefix + message);
        }

    }

    @Override
    public void trace(String message, Object arg) {
        if (this.isTraceEnabled()) {
            this.logger.trace(this.logPrefix + message, arg);
        }

    }

    @Override
    public void trace(String message, Object arg1, Object arg2) {
        if (this.isTraceEnabled()) {
            this.logger.trace(this.logPrefix + message, arg1, arg2);
        }

    }

    @Override
    public void trace(String message, Object... args) {
        if (this.isTraceEnabled()) {
            this.logger.trace(this.logPrefix + message, args);
        }

    }

    @Override
    public void trace(String msg, Throwable t) {
        if (this.isTraceEnabled()) {
            this.logger.trace(this.logPrefix + msg, t);
        }

    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return this.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        if (this.isTraceEnabled()) {
            this.logger.trace(marker, this.logPrefix + msg);
        }

    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        if (this.isTraceEnabled()) {
            this.logger.trace(marker, this.logPrefix + format, arg);
        }

    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        if (this.isTraceEnabled()) {
            this.logger.trace(marker, this.logPrefix + format, arg1, arg2);
        }

    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        if (this.isTraceEnabled()) {
            this.logger.trace(marker, this.logPrefix + format, argArray);
        }

    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        if (this.isTraceEnabled()) {
            this.logger.trace(marker, this.logPrefix + msg, t);
        }

    }


    @Override
    public void debug(String message) {
        if (this.isDebugEnabled()) {
            this.logger.debug(this.logPrefix + message);
        }

    }

    @Override
    public void debug(String message, Object arg) {
        if (this.isDebugEnabled()) {
            this.logger.debug(this.logPrefix + message, arg);
        }

    }

    @Override
    public void debug(String message, Object arg1, Object arg2) {
        if (this.isDebugEnabled()) {
            this.logger.debug(this.logPrefix + message, arg1, arg2);
        }

    }

    @Override
    public void debug(String message, Object... args) {
        if (this.isDebugEnabled()) {
            this.logger.debug(this.logPrefix + message, args);
        }

    }

    @Override
    public void debug(String msg, Throwable t) {
        if (this.isDebugEnabled()) {
            this.logger.debug(this.logPrefix + msg, t);
        }

    }


    @Override
    public void debug(Marker marker, String msg) {
        if (this.isDebugEnabled()) {
            this.logger.debug(marker, this.logPrefix + msg);
        }

    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        if (this.isDebugEnabled()) {
            this.logger.debug(marker, this.logPrefix + format, arg);
        }

    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        if (this.isDebugEnabled()) {
            this.logger.debug(marker, this.logPrefix + format, arg1, arg2);
        }

    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        if (this.isDebugEnabled()) {
            this.logger.debug(marker, this.logPrefix + format, arguments);
        }

    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        if (this.isDebugEnabled()) {
            this.logger.debug(marker, this.logPrefix + msg, t);
        }

    }



    @Override
    public void warn(String message) {
        if(this.isWarnEnabled()) {
            this.logger.warn(this.logPrefix + message);
        }
    }

    @Override
    public void warn(String message, Object arg) {
        this.logger.warn(this.logPrefix + message, arg);
    }

    @Override
    public void warn(String message, Object arg1, Object arg2) {
        this.logger.warn(this.logPrefix + message, arg1, arg2);
    }

    @Override
    public void warn(String message, Object... args) {
        if(this.isWarnEnabled()) {
            this.logger.warn(this.logPrefix + message, args);
        }
    }

    @Override
    public void warn(String msg, Throwable t) {
        this.logger.warn(this.logPrefix + msg, t);
    }



    @Override
    public void warn(Marker marker, String msg) {
        this.logger.warn(marker, this.logPrefix + msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        this.logger.warn(marker, this.logPrefix + format, arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        this.logger.warn(marker, this.logPrefix + format, arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        this.logger.warn(marker, this.logPrefix + format, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        this.logger.warn(marker, this.logPrefix + msg, t);
    }



    @Override
    public void error(String message) {
        if (this.logger.isErrorEnabled()) {
            this.logger.error(this.logPrefix + message);
        }
    }

    @Override
    public void error(String message, Object arg) {
        this.logger.error(this.logPrefix + message, arg);
    }

    @Override
    public void error(String message, Object arg1, Object arg2) {
        this.logger.error(this.logPrefix + message, arg1, arg2);
    }

    @Override
    public void error(String message, Object... args) {
        if (this.isErrorEnabled()) {
            this.logger.error(this.logPrefix + message, args);
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        this.logger.error(this.logPrefix + msg, t);
    }



    @Override
    public void error(Marker marker, String msg) {
        this.logger.error(marker, this.logPrefix + msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        this.logger.error(marker, this.logPrefix + format, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        this.logger.error(marker, this.logPrefix + format, arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        this.logger.error(marker, this.logPrefix + format, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        this.logger.error(marker, this.logPrefix + msg, t);
    }

    @Override
    public void info(String message) {
        if (this.isInfoEnabled()) {
            this.logger.info(this.logPrefix + message);
        }
    }

    @Override
    public void info(String message, Object arg) {
        this.logger.info(this.logPrefix + message, arg);
    }

    @Override
    public void info(String message, Object arg1, Object arg2) {
        this.logger.info(this.logPrefix + message, arg1, arg2);
    }

    @Override
    public void info(String message, Object... args) {
        if (this.isInfoEnabled()) {
            this.logger.info(this.logPrefix + message, args);
        }
    }

    @Override
    public void info(String msg, Throwable t) {
        this.logger.info(this.logPrefix + msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return false;
    }

    @Override
    public void info(Marker marker, String msg) {
        this.logger.info(marker, this.logPrefix + msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        this.logger.info(marker, this.logPrefix + format, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        this.logger.info(marker, this.logPrefix + format, arg1, arg2);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        this.logger.info(marker, this.logPrefix + format, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        this.logger.info(marker, this.logPrefix + msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }
}

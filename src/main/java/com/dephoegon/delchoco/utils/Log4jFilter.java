package com.dephoegon.delchoco.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;
import org.jetbrains.annotations.NotNull;

public class Log4jFilter implements Filter {
    public Filter.Result filter(@NotNull LogEvent event) {
        Message m = event.getMessage();
        if (m.toString().contains("Chocobo moved wrongly") || m.getFormattedMessage().contains("Chocobo moved wrongly")) { return Filter.Result.DENY; }
        return null;
    }
    public State getState() { return null; }
    public void initialize() { }
    public void start() { }
    public void stop() { }
    public boolean isStarted() { return false; }
    public boolean isStopped() { return false; }
    public Result getOnMismatch() { return null; }
    public Result getOnMatch() { return null; }
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) { return null; }
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0) { return null; }
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1) { return null; }
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2) { return null; }
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) { return null; }
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) { return null; }
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) { return null; }
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) { return null; }
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) { return null; }
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) { return null; }
    public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) { return null; }
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) { return null; }
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) { return null; }
    public static void init() { ((Logger) LogManager.getRootLogger()).addFilter(new Log4jFilter()); }
}
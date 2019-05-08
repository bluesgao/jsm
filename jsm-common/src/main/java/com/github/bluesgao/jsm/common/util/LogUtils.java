package com.github.bluesgao.jsm.common.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtils {
    public static void warn2agent(Logger logger, String msg, Throwable thrown) {
        logger.log(Level.WARNING, Constants.AGENT_ERROR_PREFIX + msg, thrown);
    }

    public static void warn2agent(Logger logger, String msg) {
        logger.log(Level.WARNING, Constants.AGENT_ERROR_PREFIX + msg);
    }

    public static void info2agent(Logger logger, String msg) {
        logger.log(Level.INFO, Constants.AGENT_INFO_PREFIX + msg);
    }

    public static void debug2agent(Logger logger, String msg) {
        logger.log(Level.FINE, Constants.AGENT_INFO_PREFIX + msg);
    }

    public static void warn2Client(Logger logger, String msg, Throwable thrown) {
        logger.log(Level.WARNING, Constants.CLIENT_ERROR_PREFIX + msg, thrown);
    }

    public static void warn2Client(Logger logger, String msg) {
        logger.log(Level.WARNING, Constants.CLIENT_ERROR_PREFIX + msg);
    }

    public static void info2Client(Logger logger, String msg) {
        logger.log(Level.INFO, Constants.CLIENT_INFO_PREFIX + msg);
    }

    public static void debug2Client(Logger logger, String msg) {
        logger.log(Level.FINE, Constants.CLIENT_INFO_PREFIX + msg);
    }
}

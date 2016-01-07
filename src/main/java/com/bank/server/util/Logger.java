package com.bank.server.util;

//import org.apache.log4j.*;

import org.apache.log4j.PropertyConfigurator;

public class Logger {
    static {
        PropertyConfigurator.configure("log4j.properties");
    }
    public static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Logger.class);
}

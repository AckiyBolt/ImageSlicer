package ua.bolt.tbot.util;

import com.pengrad.telegrambot.model.Update;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by kbelentsov on 12.01.2016.
 */
public class LoggingUtil {

    public static Logger getLogger(Class clazz) {
        return LogManager.getLogger(clazz);
    }

    public static Logger getLogger(Object instance) {
        return LogManager.getLogger(instance.getClass());
    }
}

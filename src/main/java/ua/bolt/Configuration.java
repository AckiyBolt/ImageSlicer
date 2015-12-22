package ua.bolt;

import java.util.InputMismatchException;

/**
 * Created by ackiybolt on 22.12.15.
 */
public class Configuration {

    public String tempdir;
    public Api api;


    public static class Api {
        public String token;
        public Integer limit;
        public Integer timeout;
        public Integer period;
    }
}

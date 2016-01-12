package ua.bolt.tbot.slicer;

import com.google.gson.GsonBuilder;

/**
 * Created by ackiybolt on 22.12.15.
 */
public class Configuration {

    public Integer imagesizelimit;
    public String tempdir;
    public Api api;

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

    public static class Api {
        public String token;
        public Integer limit;
        public Integer timeout;
        public Integer period;
    }
}

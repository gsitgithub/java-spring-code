package dev.gsitgithub.util;

public final class TimeUtils
{
    public static String intervalToString(long timeInterval)
    {
        if(timeInterval < 1000)
            return "less than one second";
        if(timeInterval < 60000)
            return (timeInterval / 1000) + " seconds";
        return "about " + (timeInterval / 60000) + " minutes";
    }
}

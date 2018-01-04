package com.heepie.soundhub.utils;

/**
 * Created by Heepie on 2017. 10. 19..
 */

public class TimeUtil {
    public static String secondToMMSS(int secDuration) {
        int sec = secDuration % 60;
        int minute = secDuration / 60;

        return String.format("%02d" + ":" +"%02d", minute, sec);
    }
}

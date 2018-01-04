package com.heepie.soundhub.utils;

/**
 * Created by Heepie on 2017. 12. 16..
 */

public class MusicUtil {
    public static Float durationToPercent(int curProgress, int maxDuration) {
        return curProgress*(100/Float.parseFloat(maxDuration+""));
    }

    public static Float percentToDuration(float curPercent, int maxDuration) {
        return (curPercent * Float.parseFloat(maxDuration+"")) / 100;
    }
}

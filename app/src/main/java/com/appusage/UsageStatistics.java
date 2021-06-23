package com.appusage;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class UsageStatistics {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            "M-d-yyyy HH:mm:ss", Locale.US);
    public static final String TAG = UsageStatistics.class.getSimpleName();

    public static List<UsageStats> getUsageStatsList(Context context){
        UsageStatsManager usageStatsManager = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();

        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.HOUR_OF_DAY, -calendar.get(Calendar.HOUR_OF_DAY));
        calendar.add(Calendar.MINUTE, -calendar.get(Calendar.MINUTE));
        calendar.add(Calendar.SECOND, -calendar.get(Calendar.SECOND));
        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + simpleDateFormat.format(startTime));
        Log.d(TAG, "Range end:" + simpleDateFormat.format(endTime));

        return usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                startTime,
                endTime);
    }

    public static String printUsageStats(List<UsageStats> usageStatsList) {
        long youtube = 0;
        for(UsageStats u : usageStatsList) {
//            com.google.android.youtube
            if(u.getPackageName().equals("com.google.android.youtube")){
                System.out.println(u.getTotalTimeInForeground());
                youtube += u.getTotalTimeInForeground();
            }
//            Log.d(TAG, "Pkg: " + u.getPackageName() + "\t"
//                    + "ForegroundTime: "
//                    + u.getTotalTimeInForeground()
//            );
        }
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(youtube),
                TimeUnit.MILLISECONDS.toMinutes(youtube) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(youtube)),
                TimeUnit.MILLISECONDS.toSeconds(youtube) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(youtube)));
        return hms;
    }

    public static long returnUsageStats(List<UsageStats> usageStatsList) {
        long youtube = 0;
        for(UsageStats u : usageStatsList) {
            //            com.google.android.youtube
            if(u.getPackageName().equals("com.google.android.youtube")){
                System.out.println(u.getTotalTimeInForeground());
                youtube += u.getTotalTimeInForeground();
            }
//            Log.d(TAG, "Pkg: " + u.getPackageName() + "\t"
//                    + "ForegroundTime: "
//                    + u.getTotalTimeInForeground()
//            );
        }
        return youtube;
    }

    public static long returnCurrentUsageStatus(Context context){
        return returnUsageStats(getUsageStatsList(context));
    }

    public static String printCurrentUsageStatus(Context context){
        return printUsageStats(getUsageStatsList(context));
    }

    private static UsageStatsManager getUsageStatsManager(Context context) {
        return (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
//        return (UsageStatsManager) context.getSystemService("usagestats");
    }
}

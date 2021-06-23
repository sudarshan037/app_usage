package com.appusage;

import android.app.Notification;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_LONG).show();
        super.onCreate();
//        startForeground(1, new Notification());
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, com.appusage.MainActivity.class);
        packageManager.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        try {
            while (true) {
//                com.google.android.youtube
                long youtube = UsageStatistics.returnCurrentUsageStatus(getApplicationContext());
                long threshold = 5400000;
                if (youtube > threshold) {
                    if (printForegroundTask(getApplicationContext()).equals("com.google.android.youtube")) {
                        System.out.println("Youtube Running: " + youtube);
                        Intent startHomeScreen = new Intent(Intent.ACTION_MAIN);
                        startHomeScreen.addCategory(Intent.CATEGORY_HOME);
                        startHomeScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startHomeScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplication().startActivity(startHomeScreen);
                    }
                    TimeUnit.SECONDS.sleep(2);
                }
                else {
                    TimeUnit.SECONDS.sleep((90*60) - youtube/1000);
                }
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "Service Stopped", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public String printForegroundTask(Context context) {
        String currentApp = "NULL";
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!mySortedMap.isEmpty()) {
                currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
        }

        Log.e("adapter", "Current App in foreground is: " + currentApp);
        return currentApp;
    }

}
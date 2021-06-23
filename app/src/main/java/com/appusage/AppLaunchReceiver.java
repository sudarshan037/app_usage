package com.appusage;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class AppLaunchReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Intent i = new Intent("com.appusage.MyService");
            i.setClass(context, MyService.class);
            context.startService(i);
        }
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            PackageManager packageManager = context.getPackageManager();
            Intent launchIntent = packageManager.getLaunchIntentForPackage("com.appusage");
            launchIntent.putExtra("some_data", "value");
            context.startActivity(launchIntent);
            Toast.makeText(context, "Action: " + intent.getAction(), Toast.LENGTH_LONG).show();
        }
//        if(Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())){
//            Toast.makeText(context, "Blah", Toast.LENGTH_LONG).show();
//        }
//        Thread starter = new Thread(() -> {
//            while (true) {
//                System.out.println(intent.getAction());
//                if (printForegroundTask(context).equals("com.google.android.youtube")) {
//                    System.out.println("Youtube Running");
//                    Intent startHomeScreen = new Intent(Intent.ACTION_MAIN);
//                    startHomeScreen.addCategory(Intent.CATEGORY_HOME);
//                    startHomeScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startHomeScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(startHomeScreen);
//                }
//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
//            Toast.makeText(context , "Boot Finished", Toast.LENGTH_LONG).show();
//        }
//        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
//            starter.start();
//            Toast.makeText(context , "User Unlocked", Toast.LENGTH_LONG).show();
//        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
//            Toast.makeText(context , "User Locked", Toast.LENGTH_LONG).show();
//        }
//        if(Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())){
//            Log.e("TAG", "onReceive: ");
//            System.out.println("Youtube?");
//            if (printForegroundTask(context).equals("com.google.android.youtube")) {
//                System.out.println("Youtube Running");
//                Intent startHomeScreen = new Intent(Intent.ACTION_MAIN);
//                startHomeScreen.addCategory(Intent.CATEGORY_HOME);
//                startHomeScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startHomeScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(startHomeScreen);
//            }
//        }
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
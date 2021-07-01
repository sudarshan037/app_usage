package com.appusage;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import static com.appusage.UsageStatistics.getUsageStatsList;

public class MainActivity extends AppCompatActivity {
    AppLaunchReceiver appLaunchReceiver;
    ImageButton usage_stats;
    TextView usage_output;
    long usage_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if permission enabled
        if (getUsageStatsList(this).isEmpty()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }

        // registerReceiver
//        appLaunchReceiver = new AppLaunchReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("android.intent.action.USER_PRESENT");
//        filter.addAction("android.intent.action.SCREEN_ON");
//        filter.addAction("android.intent.action.SCREEN_OFF");
//        filter.addAction("android.intent.action.ACTION_BOOT_COMPLETED");
//        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
//        registerReceiver(appLaunchReceiver, filter);

        // ImageButton and TextView objects
        usage_stats = findViewById(R.id.usage_icon);
        usage_output = findViewById(R.id.usage_output);

        // Update TextView on App Launch
//        usage_output.setText(UsageStatistics.printCurrentUsageStatus(MainActivity.this));
//        usage_output.setText(UsageStatistics.printUsageStats(getUsageStatsList(MainActivity.this)));
         String package_name = "com.appusage";
        usage_time = UsageStatistics.appUsageTime(getUsageStatsList(MainActivity.this),
                package_name);
        usage_output.setText(parse_milli_to_time(usage_time));

        // ImageButton Listener
        usage_stats.setOnClickListener(v -> {
//            usage_output.setText(UsageStatistics.printCurrentUsageStatus(MainActivity.this));
//            usage_output.setText(UsageStatistics.printUsageStats(getUsageStatsList(MainActivity.this)));
            usage_time = UsageStatistics.appUsageTime(getUsageStatsList(MainActivity.this),
                    package_name);
            usage_output.setText(parse_milli_to_time(usage_time));
        });
    }

    public String parse_milli_to_time(long usage_time){
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(usage_time),
                TimeUnit.MILLISECONDS.toMinutes(usage_time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(usage_time)),
                TimeUnit.MILLISECONDS.toSeconds(usage_time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(usage_time)));
        return hms;
    }
}
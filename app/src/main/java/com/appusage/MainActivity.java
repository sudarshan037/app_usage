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

public class MainActivity extends AppCompatActivity {
    AppLaunchReceiver appLaunchReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if permission enabled
        if (UsageStatistics.getUsageStatsList(this).isEmpty()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }

        appLaunchReceiver = new AppLaunchReceiver();
        IntentFilter filter = new IntentFilter();
//        filter.addAction("android.intent.action.USER_PRESENT");
        filter.addAction("android.intent.action.SCREEN_ON");
//        filter.addAction("android.intent.action.SCREEN_OFF");
//        filter.addAction("android.intent.action.ACTION_BOOT_COMPLETED");
//        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(appLaunchReceiver, filter);

        ImageButton usage_stats = findViewById(R.id.usage_icon);
        String youtube = UsageStatistics.printCurrentUsageStatus(MainActivity.this);
        TextView usage_output = findViewById(R.id.usage_output);
        usage_output.setText(youtube);
        usage_stats.setOnClickListener(v -> {
            String youtube1 = UsageStatistics.printCurrentUsageStatus(MainActivity.this);
            PackageManager packageManager = getPackageManager();
            ComponentName componentName = new ComponentName(MainActivity.this, com.appusage.MainActivity.class);
            packageManager.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            usage_output.setText(youtube1);
        });
    }
}
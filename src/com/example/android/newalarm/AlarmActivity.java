/*
* Basé sur development/samples/Alarm, créer un dir dans samples/
* il n'y a pas d'Android.mk dans Alarm j'en ai copié un d'un dir à côté, modif le nom de local pkg et ça marche très bien
* copier le dir Alarm/res/ (sinon erreurs du type "AlarmVvnx/src/com/example/android/newalarm/AlarmActivity.java:125.24: R cannot be resolved to a variable")
* marche en lunch aosp_arm64 et lineage_mido
* 
* adb uninstall com.example.android.newalarm 
* 
* ou (si installé en system app)
* rm /system/app/AlarmVvnx/AlarmVvnx.apk
* rm -rf /data/data/com.example.android.newalarm/
* reboot
* 
* adb install out/target/product/mido/system/app/AlarmVvnx/AlarmVvnx.apk
* 
* #lancement en shell sans se faire chier avec une UI:
* am start-activity com.example.android.newalarm/.AlarmActivity
* 
* #arrêter
* am force-stop com.example.android.newalarm
* 
* logcat -s AlarmVvnx
* 
* ToDo
* installer sur un telephone non rooté
* 
* 
* 
* ***Anciennes notes***
* 
* Faire des réglages de contentProvider Settings.Global.DEVICE_IDLE_CONSTANTS (DeviceIdleController.java dans le server dans frameworks)
*   mConstants = new Constants(mHandler, getContext().getContentResolver());
* 		donc c'est du content provider pour accéder à tout les settings
*   dumpsys deviceidle -> au début: current settings visibles
* 	settings get global device_idle_constants -> tant que t'as rien rajouté ya des defaults probablement mais tu les vois pas.
*   settings put global device_idle_constants inactive_to=700000,sensing_to=180000
*  
* #marche même connecté par adb
* dumpsys deviceidle force-idle -> on force en idle et si pas whitelisté pas d'alarm-> OK pour tests
* dumpsys deviceidle unforce -> on revient au fonctionnement normal
* 
* ****Whitelist (wl)
* Je pense que c'est au moment où tu mAlarmManager.setRepeating() que l'état "whitelist ou pas" est important 
* Il faut le refaire à chaque nouvelle install je pense car à l'uninstall de com.example.android.newalarm je n'ai plus rien dans dumpsys deviceidle whitelist
* dumpsys alarm
* mDeviceIdleUserWhitelist  --> mDeviceIdleUserWhitelist=[10099]
* 
* dumpsys deviceidle help
* dumpsys deviceidle whitelist
* 
* dumpsys deviceidle whitelist +com.example.android.newalarm
* dumpsys deviceidle whitelist -com.example.android.newalarm
* 
* Dumpsys alarm montre au bout de qqes minutes: 
*   u0a102:com.example.android.newalarm +35ms running, 6 wakeups:
+35ms 6 wakes 6 alarms, last -29s402ms:
  *walarm*:com.example.android.newalarm/.AlarmService
* 
* 
* 
* AlarmManager --> fw->bas/services/core/java/.../server/AlarmManagerService.java
* 
* 

* 
* 
* 
* 
* 

 */

package com.example.android.newalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Bundle;

import android.util.Log;


public class AlarmActivity extends Activity {
	
	private static final String TAG = "AlarmVvnx";
	
    // 30 * 1000 = 30 seconds in milliseconds 
    //de toutes façons en dessous de 60s: W AlarmManager: Suspiciously short interval 30000 millis; expanding to 60 seconds

    private static final long PERIODE_MS = 62 * 1000;

    // An intent for AlarmService, to trigger it as if the Activity called startService().
    private PendingIntent mAlarmSender;

    // Contains a handle to the system alarm service
    private AlarmManager mAlarmManager;

    /**
     * This method is called when Android starts the activity. 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
				
		//sinon E AndroidRuntime: android.util.SuperNotCalledException: Activity {com.example.android.newalarm/com.example.android.newalarm.AlarmActivity} did not call through to super.onCreate()
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, "AlarmActivity **Vvnx**:  onCreate()");
		
        // Create a PendingIntent to trigger a startService() for AlarmService
        mAlarmSender = PendingIntent.getService(  // set up an intent for a call to a service (voir dev guide intents à "Using a pending intent")
            AlarmActivity.this,  // the current context
            0,  // request code (not used)
            new Intent(AlarmActivity.this, AlarmService.class),  // A new Service intent 'c'est un intent explicite'
            0   // flags (none are required for a service)
        );

        // Gets the handle to the system alarm service
        mAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        
        long firstAlarmTime = SystemClock.elapsedRealtime();
        
        mAlarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP, // based on time since last wake up
                firstAlarmTime,  // sends the first alarm immediately
                PERIODE_MS,  // repeats every XX
                mAlarmSender  // when the alarm goes off, sends this Intent
            );        
        
    }

    //Shuts off the repeating countdown timer.
	private void stopAlarm() {
		    // Cancels the repeating countdown timer
            mAlarmManager.cancel(mAlarmSender);
	};

}

/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.newalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.Toast;
//vincent
import android.util.Log;

/**
 * <p>
 * This class implements a service. The service is started by AlarmActivity, which contains a
 * repeating countdown timer that sends a PendingIntent. The user starts and stops the timer with
 * buttons in the UI.
 * </p>
 * <p>
 * When this service is started, ça passe dans onCreate et onStartCommand, dans les deux j'arrête le schmilblik avec
 * un stopSelf(). 
 * </p>
 * <p>
 * Note: le sample d'origine est based on the Android 1.5 platform, it does not implement
 * onStartCommand. mais j'en ai rajouté pour voir si ça passe dedans.
 * </p>
 */
public class AlarmService extends Service {
	
	private static final String TAG = "AlarmVvnx";
	
    // Defines a label for the thread that this service starts
    private static final String ALARM_SERVICE_THREAD = "AlarmService";

    // Defines 15 seconds
    public static final long WAIT_TIME_SECONDS = 15;

    // Define the number of milliseconds in one second
    public static final long MILLISECS_PER_SEC = 1000;

    /*
     * For testing purposes, the following variables are defined as fields and set to
     * package visibility.
     */

    // The NotificationManager used to send notifications to the status bar.
    //NotificationManager mNotificationManager;

    // An Intent that displays the client if the user clicks the notification.
    //PendingIntent mContentIntent;

    // A Notification to send to the Notification Manager when the service is started.
    //Notification mNotification;

    // A Binder, used as the lock object for the worker thread.
    IBinder mBinder = new AlarmBinder();

    // A Thread object that will run the background task
    //Thread mWorkThread;

    /**
     *  Makes a full concrete subclass of Binder, rather than doing it in line, for readability.
     */
    public class AlarmBinder extends Binder {
        // Constructor. Calls the super constructor to set up the instance.
        public AlarmBinder() {
            super();
        }

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags)
            throws RemoteException {

            // Call the parent method with the arguments passed in
            return super.onTransact(code, data, reply, flags);
        }
    }

    /**
     * Initializes the service when it is first started by a call to startService() or
     * bindService().
     */
    @Override
    public void onCreate() {
        stopSelf();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "AlarmServiceVvnx: OnStartCommand");
		stopSelf();
		return START_NOT_STICKY;
	}

    /**
     * Stops the service in response to the stopSelf() issued when the wait is over. Other
     * clients that use this service could stop it by issuing a stopService() or a stopSelf() on
     * the service object.
     */
    @Override
    public void onDestroy() {		
		//Log.d(TAG, "AlarmServiceVvnx: OnDestroy");		
	 }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    /*private void showNotification() {
        CharSequence notificationText = getText(R.string.alarm_service_started);        
        mContentIntent = PendingIntent.getActivity(this, 0, new Intent(this, AlarmActivity.class), 0);
        mNotification = new Notification.Builder(this)  
                .setSmallIcon(R.drawable.stat_sample) 
                .setTicker(notificationText) 
                .setWhen(System.currentTimeMillis()) 
                .setContentTitle(getText(R.string.alarm_service_label)) 
                .setContentText(notificationText) 
                .setContentIntent(mContentIntent) 
                .build();
        mNotificationManager.notify(
            R.string.alarm_service_started,
            mNotification
        );
    }*/
}

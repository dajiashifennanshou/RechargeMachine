package com.develop.xdk.df.rechargemachine.Application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.reader.ReaderAndroid;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;

public class App extends Application {

    private static App instance;
    private Set<Activity> allActivities;
    private static Context context;
    public ReaderAndroid mSerialPort = null;
    public static  int baudrate = 9600;
    public static String path = "/dev/ttyS2";

    public ReaderAndroid getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
			/* Open the serial port */
            mSerialPort = new ReaderAndroid(new File(path), baudrate);
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close(mSerialPort.getHandle());
            mSerialPort = null;
        }
    }


    public static synchronized App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context =getApplicationContext();
        instance = this;
    }
    public  static Context getContext(){
        return context;
    }


    /**
     * 添加activity
     */
    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    /**
     * 移除activity
     */
    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    /**
     * 退出app
     */
    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
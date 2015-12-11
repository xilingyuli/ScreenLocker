package com.xilingyuli.screenlocker;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class MyService extends Service {

    private Intent startIntent = null;
    private Intent toMainIntent = null;
    private BroadcastReceiver receiver = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        flags = START_STICKY;
        super.onStartCommand(intent,flags,startId);

        if(startIntent==null)
            startIntent=intent;

        if(toMainIntent==null) {
            toMainIntent = new Intent().setClass(MyService.this, MainActivity.class);
            toMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        if(receiver==null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            //filter.addAction(Intent.ACTION_SCREEN_OFF);
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    startActivity(toMainIntent);
                    //KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
                    //KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
                    //keyguardLock.disableKeyguard();
                }
            };
            registerReceiver(receiver, filter);
        }

        Notification notification = (new Notification.Builder(this)).setContentTitle(getText(R.string.app_name)).setSmallIcon(R.drawable.icon).build();
        this.startForeground(0x111,notification);

        return flags;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return  null;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        this.stopForeground(true);
        if(startIntent!=null){
            startService(startIntent);
        }
        super.onDestroy();
    }

}

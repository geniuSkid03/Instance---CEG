package com.inspiregeniussquad.handstogether.appBroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.inspiregeniussquad.handstogether.appInterfaces.Action;

public class SignalReceiver extends BroadcastReceiver {

    private Context context;
    private String signal;
    private BroadcastReceiver broadcastReceiver;
    private boolean isRegistered;
    private Action action;


    public SignalReceiver(Context context, String signal, final Action action) {
        this.context = context;
        this.signal = signal;
        this.action = action;

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                action.onReceive(context, intent);
            }
        };
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public Action getAction() {
        return action;
    }

    public void register() {
        if(!isRegistered) {
            context.registerReceiver(broadcastReceiver, new IntentFilter(signal));
        }
    }

    public void unRegister() {
        if(isRegistered) {
            context.unregisterReceiver(broadcastReceiver);
        }
    }
}

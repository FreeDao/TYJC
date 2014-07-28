package com.kun.tyjc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBrocastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent intent1 = new Intent();
		intent1.setClass(context, ReadDataService.class);
		intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startService(intent1);
	}

}

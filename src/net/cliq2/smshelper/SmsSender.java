package net.cliq2.smshelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SmsSender {
	public static final String TAG = Config.TAG;
	private String to_num = "";
	
	public SmsSender(String num) {
		this.to_num = num;
	}

	public void sendMsg(String content) {
		SmsManager smsMgr = SmsManager.getDefault();
		// listenToSms();
		smsMgr.sendTextMessage(this.to_num, null, content, null, null);
	}

}
package net.cliq2.smshelper;

import android.content.Context;
import android.content.IntentFilter;

public class Util {

	public static void listenToSms(Context ctx, SmsReceiver mReceiver) {
		IntentFilter iFilter = null; // 意图过滤对象
		iFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED"); // 意图过滤初始化
		iFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY); // 设置优先级
//		ctx.unregisterReceiver(mReceiver);
		ctx.registerReceiver(mReceiver, iFilter);
		Logger.d("listenToSms");
	}

	public static void sendSms(String msg, SmsReceiver receiver) {
		SmsSender sms = new SmsSender(Config.getServiceNumber());
		sms.sendMsg(msg);
	}
}

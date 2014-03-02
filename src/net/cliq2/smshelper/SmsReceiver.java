package net.cliq2.smshelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
	private Context ctx;
	private OnReceivedListener hdlr;
	SmsReceiver(Context contex, OnReceivedListener handler){
		this.ctx = contex;
		this.hdlr = handler;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.d("SmsReceiver::onReceive");
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");

		StringBuffer sb = new StringBuffer();
		for (Object p : pdus) {
			byte[] pdu = (byte[]) p;
			SmsMessage message = SmsMessage.createFromPdu(pdu);
			String senderNumber = message.getOriginatingAddress();
			if (senderNumber.equals(Config.getServiceNumber())) {
				Logger.d("is service num %s", Config.getServiceNumber());
				sb.append(message.getMessageBody());
				// abortBroadcast();// 终止广播
			}
		}
		hdlr.onRecv(sb.toString());
	}
}
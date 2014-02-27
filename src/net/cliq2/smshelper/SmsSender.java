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
	private OnReceivedListener recv_hdlr;
	private Context app_ctx;
	SmsReceiver mReceiver = new SmsReceiver(); // 广播接收类初始化

	public SmsSender(Context ctx, String num) {
		this.to_num = num;
		this.app_ctx = ctx;
	}

	public void sendMsg(String content) {
		SmsManager smsMgr = SmsManager.getDefault();

		// listenToSms();
		smsMgr.sendTextMessage(this.to_num, null, content, null, null);
	}

	public void setOnReceived(OnReceivedListener listener) {
		this.recv_hdlr = listener;
	}

	public void listenToSms() {		
		IntentFilter iFilter = null; // 意图过滤对象		
		iFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED"); // 意图过滤初始化
		iFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY); // 设置优先级
		app_ctx.registerReceiver(mReceiver, iFilter); // 注册广播接
		Logger.d("listenToSms");
	}

	class SmsReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Logger.d("SmsReceiver::onReceive");
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");

			StringBuffer sb = new StringBuffer();
			for (Object p : pdus) {
				byte[] pdu = (byte[]) p;
				SmsMessage message = SmsMessage.createFromPdu(pdu);
				String senderNumber = message.getOriginatingAddress();
				if (senderNumber.equals("10086")) {
					Logger.d("is 10086");
					sb.append(message.getMessageBody());
					//abortBroadcast();// 终止广播
				}
			}
			recv_hdlr.onRecv(sb.toString());
		}
	}
}
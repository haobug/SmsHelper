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

	public SmsSender(Context ctx, String num) {
		this.to_num = num;
		this.app_ctx = ctx;
	}

	public void sendMsg(String content) {
		SmsManager smsMgr = SmsManager.getDefault();
		
		//listenToSms();
		smsMgr.sendTextMessage(this.to_num, null, content, null, null);
	}

	public void setOnReceived(OnReceivedListener listener) {
		this.recv_hdlr = listener;
	}
	
	public void listenToSms(){
		SmsReceiver mReceiver = null; // 广播接收类 对象  
        IntentFilter iFilter = null; // 意图过滤对象  
        mReceiver = new SmsReceiver(); // 广播接收类初始化  
        iFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED"); // 意图过滤初始化  
        iFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY); // 设置优先级  
        app_ctx.registerReceiver(mReceiver, iFilter); // 注册广播接
        Logger.d("listenToSms");
	}
	class SmsReceiver extends BroadcastReceiver {	    
		@Override
	    public void onReceive(Context context, Intent intent) {
			Logger.d("SmsReceiver::onReceive");
	        // 返回OBJ对象  
	        Object[] pdus = (Object[]) intent.getExtras().get("pdus");  
	        // for循环判断  
	        for (Object p : pdus) {  
	            byte[] pdu = (byte[]) p;  
	            // 信息对象 初始化 = 信息对象.创建来来自(OBJ对象)  
	            SmsMessage message = SmsMessage.createFromPdu(pdu);  
	            // 返回信息来源号码  
	            String senderNumber = message.getOriginatingAddress();  
	            // 判断号码(XXXXXXX) 将终止系统广播,删除短信  
	            if (senderNumber.equals("10086")) {
	            	Logger.d("is 10086");
	            	recv_hdlr.onRecv(message.getMessageBody());
	                abortBroadcast();// 终止广播  
	            }  
	        }  
	        //Toast.makeText(context, "判断是否拦截", Toast.LENGTH_LONG).show();  
	    }
	}  
}
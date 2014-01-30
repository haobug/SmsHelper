package net.cliq2.smshelper;

import android.app.*;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {
	private String service_num = Config.SERVICE_NUM;
	TextView txt_status = null;
	Button btn_send;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Logger.setDebugLogging(true);
		
		setContentView(R.layout.main);
		
		txt_status = (TextView)findViewById(R.id.txt_status);
		
		btn_send = (Button)findViewById(R.id.btn_send);
		btn_send.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Logger.d("listener::onClick");
				SmsSender sms = new SmsSender(MainActivity.this,service_num);
				sms.listenToSms();
				sms.setOnReceived(new OnReceivedListener() {
					public void onRecv(String recvContent) {
						Logger.d(recvContent);
						txt_status.setText("received:" + recvContent);
						parseSms(recvContent);
						btn_send.setVisibility(View.GONE);
						
					}

					
				});
				sms.sendMsg("10086");
			}
		});
	}
	private void parseSms(String recvContent) {
		
		
	}
}
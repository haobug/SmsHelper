package net.cliq2.smshelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.*;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.view.View.OnClickListener;

public class MainActivity extends Activity
implements OnReceivedListener {
	private String service_num = Config.getServiceNumber();
	TextView txt_status = null;
	Button btn_send;
	protected ViewGroup root;
	SmsReceiver mReceiver;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Logger.setDebugLogging(true);

		setContentView(R.layout.main);
		
		txt_status = (TextView) findViewById(R.id.txt_status);

		btn_send = (Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Logger.d("listener::onClick");
				mReceiver = new SmsReceiver(MainActivity.this, MainActivity.this);
				Util.listenToSms(MainActivity.this, mReceiver);
				Util.sendSms("10086", mReceiver);
			}
		});

		root = (ViewGroup) btn_send.getParent();
	}

	@Override
	public void onStop() {
		super.onStop();
		if(mReceiver != null){
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}


	@Override
	public void onRecv(String recvContent) {
		Logger.d(recvContent);

		Intent myIntent = new Intent(MainActivity.this, OptionActivity.class);
		myIntent.putExtra("sms", recvContent); // Optional parameters
		MainActivity.this.startActivity(myIntent);
	}
}
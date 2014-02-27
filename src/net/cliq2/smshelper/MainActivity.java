package net.cliq2.smshelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	protected ViewGroup root;

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
				sendSms("10086");
			}
		});

		root = (ViewGroup) btn_send.getParent();
	}

	void sendSms(String msg) {
		SmsSender sms = new SmsSender(MainActivity.this, service_num);
		sms.listenToSms();
		sms.setOnReceived(new OnReceivedListener() {
			public void onRecv(String recvContent) {
				Logger.d(recvContent);
				root.removeAllViews();
				
				Button btn_back = new Button(MainActivity.this);
				btn_back.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						/* go back */
					}
					
				});
				root.addView(btn_back);
				
				int menu_pos = recvContent.indexOf(Config.MENU_TEXT);
				if (menu_pos != -1) {
					recvContent = recvContent.substring(menu_pos
							+ Config.MENU_TEXT.length());
					List<Option> opts = parseSms(recvContent);
					if (opts != null && opts.size() > 0) {
						if (opts.size() > 1) {							
							for (Option opt : opts)
								appendButton(root, opt);
						} else {
							leafMessage(recvContent);
						}
					}					
					btn_send.setVisibility(View.GONE);
				} else {
					leafMessage(recvContent);
				}
			}
						
		});
		sms.sendMsg(msg);
	}
	
	void leafMessage(String recvContent) {
		Logger.d("is leaf");
		root.addView(txt_status);
		txt_status.setText(recvContent);
		root.addView(btn_send);
		btn_send.setVisibility(View.VISIBLE);
	}
	
	void appendButton(ViewGroup parent, Option opt) {
		// ViewGroup root = (ViewGroup) btn_send.getParent();
		Button btn = new Button(this);
		btn.setText(opt.text);
		btn.setTag(opt.value);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendSms((String) v.getTag());
			}
		});
		parent.addView(btn);
	}

	private List<Option> parseSms(String recvContent) {
		Pattern pattern = Pattern.compile("([0-9]*).([^\n]*)\n");
		Matcher matcher = pattern.matcher(recvContent);
		List<Option> opts = new ArrayList<Option>();
		int last_pos = 0;
		while (matcher.find()) {
			last_pos = matcher.end();
			String text = matcher.group().replace("\n", "");
			String num = matcher.group(1);
			Logger.d("now is:" + text);
			opts.add(new Option(text, num));
		}
		if (last_pos < recvContent.length() - 1) {
			String last_line = recvContent.substring(last_pos);
			Logger.d("last part is" + last_line);
			opts.add(new Option(last_line, "10086"));
		}
		return opts;
	}

}
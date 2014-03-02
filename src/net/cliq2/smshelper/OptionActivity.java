package net.cliq2.smshelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OptionActivity extends Activity
implements OnReceivedListener {
	private Option[] options;
	LinearLayout option_root;
	private SmsReceiver mReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String recvContent = intent.getStringExtra("sms");
		setContentView(R.layout.layout_option);
		
		
		
		option_root = (LinearLayout) findViewById(R.id.btn_back).getParent();
		
		bindOptions(recvContent);
	}
	@Override
	public void onStop(){
		super.onStop();
		if(mReceiver != null){
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		if(mReceiver != null){
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}
	
	public void onBackClick(View v) {
		this.finish();
	}

	private void bindOptions(String recvContent) {
		int menu_pos = recvContent.indexOf(Config.getMenuText());
		if (menu_pos != -1) {
			recvContent = recvContent.substring(menu_pos
					+ Config.getMenuText().length());
			List<Option> opts = parseSms(recvContent);
			if (opts != null && opts.size() > 0) {
				if (opts.size() > 1) {
					for (Option opt : opts)
						appendButton(option_root, opt);
				} else {
					leafMessage(option_root,recvContent);
				}
			}
		} else {
			leafMessage(option_root,recvContent);
		}

	}
	void leafMessage(ViewGroup parent, String recvContent) {
		Logger.d("is leaf");
		TextView txt = new TextView(this);
		txt.setText(recvContent);
		parent.addView(txt);
	}

	private List<Option> parseSms(String recvContent) {
		Pattern pattern = Pattern.compile(Config.getItemRegex());
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
			opts.add(new Option(last_line, Config.getServiceNumber()));
		}
		return opts;
	}

	void appendButton(ViewGroup parent, Option opt) {
		Button btn = new Button(this);
		btn.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT)
		);
		btn.setText(opt.text);
		btn.setTag(opt.value);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mReceiver = new SmsReceiver(OptionActivity.this, OptionActivity.this);
				Util.listenToSms(OptionActivity.this, mReceiver);
				Util.sendSms((String) v.getTag(), mReceiver);
			}
		});
		parent.addView(btn);
	}
	@Override
	public void onRecv(String recvContent) {
		Logger.d(recvContent);

		Intent myIntent = new Intent(OptionActivity.this,
				OptionActivity.class);
		myIntent.putExtra("sms", recvContent);
		OptionActivity.this.startActivity(myIntent);
	}
}

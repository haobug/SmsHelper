package net.cliq2.smshelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SplashActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Logger.setDebugLogging(true);

		setContentView(R.layout.choose_operator);
	}
	
	public void onRadioButtonClicked(View v){
		Logger.d("onRadioButtonClicked");
		switch(v.getId()){
		case R.id.rdo_cmcc:
			Config.setOperator(Config.CHINA_MOBILE);
			break;
		case R.id.rdo_cucc:
			Config.setOperator(Config.CHINA_UNICOM);
			break;
		case R.id.rdo_ctcc:
			Config.setOperator(Config.CHINA_TELECOM);
			break;
		case R.id.rdo_guess:
			Config.setOperator(guess());
			break;
		default:
			Config.setOperator(Config.CHINA_MOBILE);
			break;
		}


		Intent myIntent = new Intent(SplashActivity.this, MainActivity.class);
		SplashActivity.this.startActivity(myIntent);
	}

	private int guess() {
		// TODO Auto-generated method stub
		return 0;
	}
}

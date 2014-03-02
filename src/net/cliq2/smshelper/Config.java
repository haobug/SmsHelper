package net.cliq2.smshelper;

public class Config {
	/* confgurations */
	public static final int CHINA_MOBILE = 0;
	public static final int CHINA_UNICOM = 1;
	public static final int CHINA_TELECOM = 2;

	static Operator[] operators = {
			new Operator("10086", "请直接回复序号：", "([0-9]*).([^\n]*)\n")
			,new Operator("10010", "请回复以下编码办理业务：", "([0-9]*)：([^\n|^\r]*)[\r|\n]")
			,new Operator("10000", "？不确定？：", "？不确定？")
	};

	public static final String TAG = "sms_helper";
	private static int operator;

	public static String getServiceNumber() {
		Logger.d("service num is %s",  operators[operator].serviceNumber);
		return operators[operator].serviceNumber;
	}
	
	public static String getMenuText() {
		Logger.d("menu text is %s",  operators[operator].menuText);
		return operators[operator].menuText;
	}
	
	public static String getItemRegex(){
		Logger.d("item regex is %s",  operators[operator].itemRegex);
		return operators[operator].itemRegex;
	}

	public static void setOperator(int operator) {
		Config.operator = operator;
	}
}

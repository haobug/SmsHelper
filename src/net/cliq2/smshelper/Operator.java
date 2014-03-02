package net.cliq2.smshelper;

public class Operator {

	public String serviceNumber;
	public String menuText;
	public String itemRegex;

	public Operator(String service_num, String menu_text, 
			String item_regex) {
		this.serviceNumber = service_num;
		this.menuText = menu_text;
		this.itemRegex = item_regex;
	}

}

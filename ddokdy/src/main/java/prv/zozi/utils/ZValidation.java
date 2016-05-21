package prv.zozi.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZValidation {
	//이메일
	public boolean isEmail(String email) {
	        if (email==null) return false;
	        boolean b = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+",email.trim());
	        return b;
	    }

	//휴대폰
	public boolean isValidCellPhoneNumber(String cellphoneNumber) {
	  boolean returnValue = false;
	 // Log.i("cell", cellphoneNumber);
	  String regex = "^\\s*(010|011|012|013|014|015|016|017|018|019)(-|\\)|\\s)*(\\d{3,4})(-|\\s)*(\\d{4})\\s*$";
	  Pattern p = Pattern.compile(regex);
	  Matcher m = p.matcher(cellphoneNumber);
	  if (m.matches()) {
	   returnValue = true;
	  }
	  return returnValue;
	 }

	//일반 전화번호
	public boolean isValidPhoneNumber(String phoneNumber) {
	  boolean returnValue = false;
	  String regex = "^\\s*(02|031|032|033|041|042|043|051|052|053|054|055|061|062|063|064|070)?(-|\\)|\\s)*(\\d{3,4})(-|\\s)*(\\d{4})\\s*$";
	  Pattern p = Pattern.compile(regex);
	  Matcher m = p.matcher(phoneNumber);
	  if (m.matches()) {
	   returnValue = true;
	  }
	  return returnValue;
	 }
}

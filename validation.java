package com.trivediinfoway.theinnontheriver;

import java.util.regex.Pattern;

public class validation {


    public static boolean isValidEmail(String email) {
        //	String emailreg ="^[_a-zA-Z_\\.\\-]+[_A-Za-z_0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]{2,10}+(\\.[A-Za-z]{2,5}+)*(\\.[A-Za-z]{2,5})$";
        String emailreg = "^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$";
        Boolean b = email.matches(emailreg);

        return b;
    }

	/*public static boolean isMobNo(String str) {
        Pattern mobNO = Pattern.compile("^\\d{10,15}$");
        Matcher matcher = mobNO.matcher(str);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
	}*/
	
	/*public static boolean iswebsite(String str) {
		Pattern p = Pattern.compile("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?");  

	    Matcher m = p.matcher(str); 
        if (m.find()) {
            return true;
        } else {
            return false;
        }
	}*/

    public static boolean isMobileNo(String str) {
        //	if(str.matches("[a-zA-Z0-9\\s(/)(-)(,)(.)(#)]+")){  //space validation and wild character
        if (str.matches("^[123456789][0-9]*")) {    // allows only number
            return true;
        } else {
            return false;
        }
    }

    public static boolean isnumber(String str) {
        Pattern pattern = Pattern.compile(".*[^0-9].*");


        if (!pattern.matcher(str).matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isText(String str) {
        if (str.matches("^[a-zA-Z]+")) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isText_space(String str) {
        if (str.matches("^[a-zA-Z\\s]+")) {    //space validation
            return true;
        } else {
            return false;
        }

    }

    public static boolean isalphanumeric(String str) {
        if (str.matches("^[a-zA-Z0-9\\s]+")) {  // only alpha and number
            return true;
        } else {
            return false;
        }
    }

    public static boolean isalphanumeric_add(String str) {
        //	if(str.matches("[a-zA-Z0-9\\s(/)(-)(,)(.)(#)]+")){  //space validation and wild character
        if (str.matches("^[a-zA-Z0-9-,./#\\s]+")) {    // allows special characters - , . / # space
            return true;
        } else {
            return false;
        }
    }

    /*	public static boolean isPassword(String str) {
            //	if(str.matches("[a-zA-Z0-9\\s(/)(-)(,)(.)(#)]+")){  //space validation and wild character
                if(str.matches("^[a-zA-Z0-9-,./#@$%&]+")){  	// allows special characters - , . / # @ $ % &
                     return true;
                    }else {
                        return false;
                    }
        }
        */
    public static boolean isPassword(String str) {

        if (str.matches("^(?=.*[0-9])(?=.*[a-z])([a-z0-9_-]+)([@#$%^&+=<>:;?*-~!()])$")) {   // allows special characters - , . / # @ $ % &
            //alphnumeric
            return true;
        } else {
            return false;
        }

        //
    }
/*	public static boolean isalphanumeric(String str) {
	//	if(str.matches("[a-zA-Z0-9\\s(/)(-)(,)(.)(#)]+")){  //space validation and wild character
		if(str.matches("^[a-zA-Z0-9-\\s]+")){  	// allows special characters - , . / # space
			 return true;
			}else {
	            return false;
	        }
       
	}*/

    public static boolean isMCInumber(String str) {
        if (str.matches("^[a-zA-Z0-9-]+")) {  // only alpha and number
            return true;
        } else {
            return false;
        }

    }


}

package net.hunme.baselibrary.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidation {
	/**
	 * 是否符合手机号码标准
	 * @param mobiles 手机号码
	 * @return
     */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^[1][0-9]{10}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 *  是否符合身份证号码标准
	 * @param cardid 身份证号码
	 * @return
     */
	public static boolean isCardID(String cardid) {
		Pattern p = Pattern.compile("^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$");
		Matcher m = p.matcher(cardid);
		return m.matches();
	}
}

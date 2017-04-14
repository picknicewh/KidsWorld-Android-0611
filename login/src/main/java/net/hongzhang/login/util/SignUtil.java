package net.hongzhang.login.util;

import net.hongzhang.baselibrary.util.Constant;
import net.hongzhang.baselibrary.util.MD5Utils;

/**
 * 作者： wanghua
 * 时间： 2017/3/27
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SignUtil {
    /**
     * 获取登陆的sign
     * @param url 接口地址
     */
    public static String getSign(String url) {
        String sign;
        url = url.replace(".do","");
        String currentTime = String.valueOf(System.currentTimeMillis());
        String hashValue = url + currentTime + Constant.AND_KEY;
        sign = currentTime + "-" + MD5Utils.encode(hashValue);
        return sign;
    }
}

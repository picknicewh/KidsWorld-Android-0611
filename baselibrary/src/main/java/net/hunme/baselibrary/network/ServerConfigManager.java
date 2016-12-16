package net.hunme.baselibrary.network;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： zll
 * 时间： 2016-6-7
 * 名称： 服务器配置文件
 * 版本说明：代码规范整改
 * 附加注释：
 * 主要接口：
 */
public class ServerConfigManager {
	/**
	 * 服务器状态地址
	 */
	public static final String SERVER_STATUS_IP = "https://s1.openhunme.com/authuser/queryServer";
	/**
	 * 服务器IP列表
	 */
	public static final List<String> SERVER_IP_LIST = new ArrayList<String>();
	/**
	 * 主服务器地址
	 */
	public static final String SERVER_IP = "http://zhu.hunme.net:8080/KidsWorld";//测试服
   // public static final String SERVER_IP = "http://192.168.1.200:8080/KidsWorld"; //胡诗昊
//  public static final String SERVER_IP = "http://192.168.1.108:8080/KidsWorld";  //匡军虹
   // public static final String SERVER_IP = "https://eduslb.openhunme.com/KidsWorld"; //正式服
    public static final String WEB_IP = "file:///android_asset/www/kidsworld"; //web本地绝对路径
//	public static final String WEB_IP = "http://192.168.5.136:8989/webSVN/kidsWorld";//汪建波

}

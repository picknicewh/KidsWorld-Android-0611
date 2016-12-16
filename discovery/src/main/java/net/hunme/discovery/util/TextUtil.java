package net.hunme.discovery.util;

import android.util.Log;

import net.hunme.discovery.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 作者： Administrator
 * 时间： 2016/11/15
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class TextUtil {
    public static String getSongList(String lry) {
        String pStart = "<p>";
        String pEnd = "</p>";
        String enter = "\\\n";
        StringBuffer stringBuffer = new StringBuffer();
        String[] items = lry.split(pStart);
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            item = item.replace(pEnd, enter);
            String temp = item.replace("\\n", "n");
            stringBuffer.append(temp.replace("\\", ""));
        }
        return stringBuffer.toString();
    }

    public static String  encodeChineseUrl(String url){
        Log.i("bbbbb",url);
        if (!url.contains(".")){
            return url;
        }
        int lastIndex =  url.lastIndexOf("/");
        int dosIndex = url.lastIndexOf(".");
        if (dosIndex<lastIndex){
            return  url;
        }else {
            String chineseUrl = url.substring(lastIndex+1,dosIndex);
            try {
                String encodeUrl = URLEncoder.encode(chineseUrl, "utf-8");
                url = url.replace(chineseUrl,encodeUrl);
                if (url.contains("+")){
                    url=   url.replace("+"," ");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return url;
    }
    public static  int[] bgimages = new int[]{R.mipmap.ic_music_bg1, R.mipmap.ic_music_bg2, R.mipmap.ic_music_bg3, R.mipmap.ic_music_bg4};
    public static  int[] formImages =new int[]{R.mipmap.ic_source_from1,R.mipmap.ic_source_from2,R.mipmap.ic_source_from3,R.mipmap.ic_source_from4,};
    public static int[] musicCircle = new int[]{R.mipmap.ic_music_circle1,R.mipmap.ic_music_circle2,R.mipmap.ic_music_circle3,R.mipmap.ic_music_circle4};
    public  static int getBgImage(String createName){
        if (createName==null){
            return bgimages[0];
        }
        if (createName.equals("KIKIBA儿童资源库")){
            return bgimages[0];
        }else if (createName.equals("毛毛虫童书")){
            return bgimages[1];
        }else if (createName.equals("小石头")){
            return bgimages[2];
        }else if (createName.equals("小宝姐姐")){
            return bgimages[3];
        }else if (createName.equals("方素素")){
            return bgimages[0];
        }else if (createName.equals("小天天")){
            return bgimages[1];
        }else if (createName.equals("山林")){
            return bgimages[2];
        }else if (createName.equals("王老师")){
            return bgimages[3];
        }else if (createName.equals("刘璐璐")){
            return bgimages[1];
        }else if (createName.equals("李明东")){
            return bgimages[2];
        }else {
            return bgimages[0];
        }
    }
    public  static int getCircleImage(String createName){
        if (createName==null){
            return musicCircle[0];
        }
        if (createName.equals("KIKIBA儿童资源库")){
            return musicCircle[0];
        }else if (createName.equals("毛毛虫童书")){
            return musicCircle[1];
        }else if (createName.equals("小石头")){
            return musicCircle[2];
        }else if (createName.equals("小宝姐姐")){
            return musicCircle[3];
        }else if (createName.equals("方素素")){
            return musicCircle[0];
        }else if (createName.equals("小天天")){
            return musicCircle[1];
        }else if (createName.equals("山林")){
            return musicCircle[2];
        }else if (createName.equals("王老师")){
            return musicCircle[3];
        }else if (createName.equals("刘璐璐")){
            return musicCircle[1];
        }else if (createName.equals("李明东")){
            return musicCircle[2];
        }else {
            return musicCircle[0];
        }
    }
    public  static int getFromImage(String createName){
        if (createName==null){
            return formImages[0];
        }
        if (createName.equals("KIKIBA儿童资源库")){
            return formImages[0];
        }else if (createName.equals("毛毛虫童书")){
            return formImages[1];
        }else if (createName.equals("小石头")){
            return formImages[2];
        }else if (createName.equals("小宝姐姐")){
            return formImages[3];
        }else if (createName.equals("方素素")){
            return formImages[0];
        }else if (createName.equals("小天天")){
            return formImages[1];
        }else if (createName.equals("山林")){
            return formImages[2];
        }else if (createName.equals("王老师")){
            return formImages[3];
        }else if (createName.equals("刘璐璐")){
            return formImages[1];
        }else if (createName.equals("李明东")){
            return formImages[2];
        }else {
            return formImages[0];
        }
    }
}

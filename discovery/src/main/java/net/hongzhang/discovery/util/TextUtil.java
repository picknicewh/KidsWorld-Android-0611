package net.hongzhang.discovery.util;

import android.net.Uri;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.discovery.R;

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

    public static String encodeChineseUrl(String url) {
        int lastIndex = 0;
        if (url.contains("Music")) {
            lastIndex = url.lastIndexOf("Music");
        } else if (url.contains("Video")) {
            lastIndex = url.lastIndexOf("Video");
        }else if (url.contains("Image")){
            lastIndex = url.lastIndexOf("Image");
        }
        int dosIndex = url.lastIndexOf(".");
        if (dosIndex < lastIndex) {
            return url;
        } else {
            String chineseUrl = url.substring(lastIndex, dosIndex);
            G.log("============chineseUrl=============" + chineseUrl);
            try {
                String encodeUrl = Uri.encode(chineseUrl);
                url = url.replace(chineseUrl, encodeUrl);

                if (url.contains("%2F")) {
                    url = url.replace("%2F", "/");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return url;
    }
    public static  int[] bgimages = new int[]{R.mipmap.ic_music_bg1, R.mipmap.ic_music_bg2, R.mipmap.ic_music_bg3, R.mipmap.ic_music_bg4};
    public static  int[] formImages =new int[]{R.mipmap.ic_source_from1,R.mipmap.ic_source_from2,R.mipmap.ic_source_from3,R.mipmap.ic_source_from4,};
    public static int[] musicCircle = new int[]{R.mipmap.ic_music_circle1,R.mipmap.ic_music_circle2,R.mipmap.ic_music_circle3,R.mipmap.ic_music_circle4};
    public  static int getInforImage(String createName,int source){
      if (source==1){
          return  getImage(bgimages,createName);
      }else if (source==2){
          return  getImage(formImages,createName);
      }else{
          return  getImage(musicCircle,createName);
      }
    }
    private static int getImage(int[] images ,String createName){
        if (createName==null){
            return images[0];
        }
        if (createName.equals("KIKIBA儿童资源库")){
            return images[0];
        }else if (createName.equals("毛毛虫童书")){
            return images[1];
        }else if (createName.equals("小石头")){
            return images[2];
        }else if (createName.equals("小宝姐姐")){
            return images[3];
        }else if (createName.equals("方素素")){
            return images[0];
        }else if (createName.equals("小天天")){
            return images[1];
        }else if (createName.equals("山林")){
            return images[2];
        }else if (createName.equals("王老师")){
            return images[3];
        }else if (createName.equals("刘璐璐")){
            return images[0];
        }else if (createName.equals("李明东")){
            return images[1];
        }else {
            return images[0];
        }
    }
}

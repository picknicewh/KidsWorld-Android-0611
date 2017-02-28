package main.picturesee.util;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者： Administrator
 * 时间： 2016/8/24
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class FileUtil {
    /**
     * 以当前时间创建文件夹
     * @param suffix 文件类型
     */
    public static File createRecordFile(String suffix) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File parentFile = new File(Environment.getExternalStorageDirectory(), "贝贝虎");
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "IMG_" + timeStamp;
            File tempFile;
            tempFile = new File(parentFile, fileName + "."+suffix);
            if (!tempFile.exists()) {
                try {
                    tempFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tempFile;
        }
        return null;
    }
}

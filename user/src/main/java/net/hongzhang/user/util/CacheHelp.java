package net.hongzhang.user.util;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/27
 * 描    述：处理缓存工具类
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class CacheHelp {
    /**
     * 得到一个文件夹的带大小
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            if(null==fileList){
                return 0;
            }
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     *  转化获取的大小
     * @param file
     * @return
     * @throws Exception
     */
    public static String getPathSize(File file) throws Exception {
        return getFormatSize(getFolderSize(file));
    }
    /**
     * 删除指定文件夹的文件
     *
     * @param filePath       文件路径
     * @param deleteThisPath 是否删除
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 如果下面还有文件
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }


//    public static String creatDestFile(String path,String Imagepath){
//        File destDir = new File(path);
//        if (!destDir.exists()) {
//            destDir.mkdirs();
//        }
//        saveMyBitmap(path, BitmapFactory.decodeFile(Imagepath));
//        G.log("------path----"+path);
//        return path;
//    }

    private void saveFile(List<File>list,String path){
        for (int i=0;i<list.size();i++){
            File files=list.get(i);
            try {
                FileInputStream fis=new FileInputStream(files);
                File file = new File(path + "/"+Math.random()*100+".jpg");
                if(!file.exists()){
                    file.createNewFile();
                    FileOutputStream fos=new FileOutputStream(file);
                    int read=fis.read();
                    byte[] b=new byte[1024];
                    while (read!=-1){
                        fos.write(read);
                        read=fis.read();
                    }
                    fos.flush();
                    fos.close();
                    fis.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存文件到指定的路径下面
     * @param bitmap
     */
    private static void saveMyBitmap(String path, Bitmap bitmap) {
        File f = new File(path + Math.random()*100 + ".png");
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

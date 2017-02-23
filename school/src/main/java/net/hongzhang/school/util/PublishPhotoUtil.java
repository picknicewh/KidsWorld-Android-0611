package net.hongzhang.school.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.PermissionsChecker;
import net.hongzhang.user.adapter.GridAlbumAdapter;

import java.util.ArrayList;
import java.util.List;

import main.picturesee.util.ImagePagerActivity;

/**
 * 作者： wh
 * 时间： 2016/10/12
 * 名称：选择照片发布类
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PublishPhotoUtil {
    // 访问相册所需的全部权限
    private final static String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //读写权限
            Manifest.permission.CAMERA
    };
    /**
     * 发布照片页面
     */
    public static void showPhoto(final Activity context, final ArrayList<String> itemList , GridView gridView, final int maxContent){
        final GridAlbumAdapter  adapter=new GridAlbumAdapter(itemList,context,maxContent);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == itemList.size()) {
                    goSelectImager(itemList,context,adapter,maxContent);
                }else {
                    imageBrower(i,itemList,context);
                }
            }
        });
    }
    public static void imageBrower(int position, ArrayList<String> urls,Context context) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtra("source","local");
        context.startActivity(intent);
    }
    public static void imageBrowernet(int position, ArrayList<String> urls,Context context) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtra("source","net");
        context.startActivity(intent);
    }
    /**
     * 前往获取图片
     */
    public static void goSelectImager(final ArrayList<String> itemList,Activity context, final GridAlbumAdapter  adapter,int maxContent){
        PermissionsChecker checker=PermissionsChecker.getInstance(context);
        if(checker.lacksPermissions(PERMISSIONS)){
            checker.getPerMissions(PERMISSIONS);
            return;
        }
        AndroidImagePicker androidImagePicker =   AndroidImagePicker.getInstance();
        androidImagePicker.setSelectLimit(maxContent-itemList.size());
        androidImagePicker.pickMulti(context, true, new AndroidImagePicker.OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(List<ImageItem> items) {
                if(items != null && items.size() > 0){
                    for(ImageItem item:items){
                        G.log("选择了===="+item.path);
                        itemList.add(item.path);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

}

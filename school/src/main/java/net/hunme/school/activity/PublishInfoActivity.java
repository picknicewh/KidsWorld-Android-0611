package net.hunme.school.activity;

import android.os.Bundle;
import android.widget.GridView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.school.R;
import net.hunme.school.util.PublishPhotoUtil;

import java.util.ArrayList;

/**
 * 作者： wh
 * 时间： 2016/10/12
 * 名称：发布通知
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PublishInfoActivity extends BaseActivity {
    /**
     * 发布照片
     */
    private GridView gv_photo;
    /**
     * 最多照片
     */
    private int maxContent=9;
    /**
     * 获取照片的列表
     */
    private ArrayList<String> itemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_info);
        initView();
    }
   private void initView(){
       gv_photo = $(R.id.gv_info_photo);
       itemList = new ArrayList<>();
       PublishPhotoUtil.showPhoto(this,itemList,gv_photo,maxContent);
   }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("发布通知");
        setSubTitle("确定");
    }
}

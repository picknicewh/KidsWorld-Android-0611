package net.hunme.user.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.hunme.baselibrary.activity.BaseActivity;
import net.hunme.user.R;
import net.hunme.user.adapter.PhotoAdapter;
import net.hunme.user.mode.PhotoVo;

import java.util.ArrayList;
import java.util.List;
/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：用户相册
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class UPhotoActivity extends BaseActivity implements View.OnClickListener {
    private ListView lv_photo;
    private PhotoAdapter adapter;
    private List<PhotoVo>photoList; //用户相册实体类 list
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_photo);
        initView();
        initDate();
    }
    @Override
    protected void setToolBar() {
        setCententTitle("我的相册");
        setLiftImage(R.mipmap.ic_launcher);
        setSubTitle("添加");
        setLiftOnClickListener(this);
        setRightOnClickListener(this);
    }

    private void initView(){
        lv_photo= (ListView) findViewById(R.id.lv_photo);
    }

    private void initDate(){
        photoList=new ArrayList<>();
        adapter=new PhotoAdapter(photoList,this);
        lv_photo.setAdapter(adapter);
        for (int i=0;i<5;i++){
            photoList.add(TestDate());
        }
        adapter.notifyDataSetChanged();
        lv_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(UPhotoActivity.this,UploadPhotoActivity.class));
            }
        });
    }

    //测试数据
    private PhotoVo TestDate(){
        PhotoVo photoVo=new PhotoVo();
        photoVo.setPhotoBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_test01));
        photoVo.setPhotoName("掠天之翼");
        photoVo.setPhotoNumber("共15张");
        return  photoVo;
    }

    @Override
    public void onClick(View view) {
        int viewID=view.getId();
        if(viewID==R.id.iv_left){
            finish();
        }else if(viewID==R.id.iv_right){

        }
    }
}

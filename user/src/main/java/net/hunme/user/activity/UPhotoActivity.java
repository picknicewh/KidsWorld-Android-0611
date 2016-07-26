package net.hunme.user.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.util.G;
import net.hunme.user.R;
import net.hunme.user.adapter.PhotoAdapter;
import net.hunme.user.mode.PhotoVo;
import net.hunme.user.util.MyAlertDialog;

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
        setLiftImage(R.mipmap.ic_arrow_lift);
        setSubTitle("添加");
        setLiftOnClickClose();
        setSubTitleOnClickListener(this);
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
        if(viewID==R.id.tv_subtitle){
            //获取View
            final View dialogView = LayoutInflater.from(this).inflate(R.layout.alertdialog_add_album, null);
            //获取弹框
            final AlertDialog alertDialog =MyAlertDialog.getDialog(dialogView,this,1);
            Button alertCancel= (Button) dialogView.findViewById(R.id.b_cancel);
            Button alertConfirm= (Button) dialogView.findViewById(R.id.b_confirm);
            final EditText etAlbumName= (EditText) dialogView.findViewById(R.id.et_album_name);
            etAlbumName.setFocusable(true);
            etAlbumName.setFocusableInTouchMode(true);
            etAlbumName.requestFocus();
            //取消
            alertCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            //确定
            alertConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String albumName=etAlbumName.getText().toString().trim();
                    if(G.isEmteny(albumName)){
                        G.showToast(UPhotoActivity.this,"相册名不能为空");
                        return;
                    }
                    PhotoVo photoVo=new PhotoVo();
                    photoVo.setPhotoBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_test01));
                    photoVo.setPhotoName(albumName);
                    photoList.add(0,photoVo);
                    adapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                }
            });
        }
    }
}

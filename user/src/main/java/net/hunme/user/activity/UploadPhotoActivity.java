package net.hunme.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import net.hunme.baselibrary.activity.BaseActivity;
import net.hunme.user.R;
import net.hunme.user.adapter.GridAdapter;
import net.hunme.user.util.Bimp;

import java.io.File;

public class UploadPhotoActivity extends BaseActivity implements View.OnClickListener {
    private GridView gv_photo;
    private LinearLayout ll_selcet_photoname;
    private GridAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        initView();
        initDate();
    }

    private void initView(){
        gv_photo=$(R.id.gv_photo);
        ll_selcet_photoname=$(R.id.ll_selcet_photoname);
    }

    private void initDate(){
        adapter=new GridAdapter(this);
        adapter.update();
        gv_photo.setAdapter(adapter);
        gv_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == Bimp.bmp.size()) {
//                    Intent intent = new Intent(Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                    Intent intent = new Intent(UploadPhotoActivity.this,
                            PicActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_launcher);
        setCententTitle("上传图片");
        setSubTitle("完成");
        setLiftOnClickListener(this);
        setSubTitleOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId=view.getId();
        if(viewId==R.id.iv_left){
            finish();
            Bimp.bmp.clear();
        }else if(viewId==R.id.tv_subtitle){

        }
    }
    private static final int TAKE_PICTURE = 0x000000;
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < 9 && resultCode == -1) {
                    File file = new File(Environment.getExternalStorageDirectory()
                            + "/myimage/", String.valueOf(System.currentTimeMillis())
                            + ".jpg");
                    Bimp.tempSelectBitmap.add(file.getPath());
                }
                break;
        }
    }


}

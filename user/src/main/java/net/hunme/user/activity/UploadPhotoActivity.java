package net.hunme.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.user.R;
import net.hunme.user.adapter.GridAdapter;
import net.hunme.user.util.Bimp;

import java.io.File;
import java.util.ArrayList;


/**
 * ================================================
 * 作    者：ZLL
 * 时    间：2016/7/18
 * 描    述：上传图片
 * 版    本：1.0
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class UploadPhotoActivity extends BaseActivity implements View.OnClickListener {
    private GridView gv_photo;
    private LinearLayout ll_selcet_photoname;
    private GridAdapter adapter;
    private TextView tv_album_name;
    private ArrayList<String> albumNameList;
    public static int position=0;
    public static final int TAKE_PICTURE = 0x000000;
    private static final int Album_NAME_SELECT=1111;
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
        tv_album_name=$(R.id.tv_album_name);
        ll_selcet_photoname.setOnClickListener(this);
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
                            AlbumActivity.class);
                    startActivityForResult(intent,TAKE_PICTURE);
                }
            }
        });
        albumNameList=new ArrayList<>();
        albumNameList.add("默认相册");
        albumNameList.add("相册一");
        albumNameList.add("相册二");
        tv_album_name.setText(albumNameList.get(position));
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
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

        }else if(viewId==R.id.ll_selcet_photoname){
            Intent intent=new Intent(this, AlbumSelectActivity.class);
            intent.putStringArrayListExtra("namelist",albumNameList);
            startActivityForResult(intent,Album_NAME_SELECT);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < 9 && resultCode == -1) {
                    File file = new File(Environment.getExternalStorageDirectory()
                            + "/myimage/", String.valueOf(System.currentTimeMillis())
                            + ".jpg");
                    Bimp.tempSelectBitmap.add(file.getPath());
                }
                adapter.update();
                adapter.notifyDataSetChanged();
                break;
            case Album_NAME_SELECT:
                tv_album_name.setText(albumNameList.get(position));
                break;
        }
    }


}

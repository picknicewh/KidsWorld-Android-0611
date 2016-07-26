package net.hunme.user.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import net.hunme.baselibrary.activity.PermissionsActivity;
import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.PermissionsChecker;
import net.hunme.user.R;
import net.hunme.user.adapter.GridAlbumAdapter;

import java.util.ArrayList;
import java.util.List;


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
    private TextView tv_album_name;
    private ArrayList<String> albumNameList;
    public static int position=0;
    private static final int Album_NAME_SELECT=1111;
    private GridAlbumAdapter mAdapter;
    private List<ImageItem> itemList;
    //https://github.com/jeasonlzy0216/ImagePicker
    //权限返回码
    private static final int  REQUEST_CODE = 0;
    // 访问相册所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //读写权限
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    // 权限检测器
    private PermissionsChecker mPermissionsChecker;
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
        mPermissionsChecker = new PermissionsChecker(this);
        G.initDisplaySize(this);
        itemList=new ArrayList<>();
        mAdapter=new GridAlbumAdapter(itemList,this);
        gv_photo.setAdapter(mAdapter);
        gv_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i ==itemList.size()) {
                    //检查是否有权限访问相册 ，没有的话弹框需要用户授权
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                        PermissionsActivity.startActivityForResult(UploadPhotoActivity.this, REQUEST_CODE, PERMISSIONS);
                        return;
                    }
                    AndroidImagePicker.getInstance().pickMulti(UploadPhotoActivity.this, true, new AndroidImagePicker.OnImagePickCompleteListener() {
                        @Override
                        public void onImagePickComplete(List<ImageItem> items) {
                            if(items != null && items.size() > 0){
                                for(ImageItem item:items){
                                    G.log("选择了===="+item.path);
                                    if(itemList.size()<9){
                                        itemList.add(item);
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
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
        setCententTitle("上传照片");
        setSubTitle("完成");
        setLiftOnClickClose();
        setSubTitleOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId=view.getId();
        if(viewId==R.id.tv_subtitle){

        }else if(viewId==R.id.ll_selcet_photoname){
            Intent intent=new Intent(this, AlbumSelectActivity.class);
            intent.putStringArrayListExtra("namelist",albumNameList);
            startActivityForResult(intent,Album_NAME_SELECT);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Album_NAME_SELECT:
                tv_album_name.setText(albumNameList.get(position));
                break;
            case REQUEST_CODE:
                //检测到没有授取权限 关闭页面
                if(resultCode == PermissionsActivity.PERMISSIONS_DENIED){
                    finish();
                }
                break;
        }
    }

}

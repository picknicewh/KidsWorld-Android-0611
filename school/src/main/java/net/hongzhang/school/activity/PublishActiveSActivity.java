package net.hongzhang.school.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import net.hongzhang.baselibrary.activity.PermissionsActivity;
import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.PermissionsChecker;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.NoScrollGirdView;
import net.hongzhang.school.R;
import net.hongzhang.school.widget.PictureChoosePopWindow;
import net.hongzhang.user.adapter.GridAlbumAdapter;
import net.hongzhang.user.util.PublishPhotoUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class PublishActiveSActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvUserInfo;
    private ToggleButton tbopen;
    private NoScrollGirdView gvPhoto;
    private TextView tvPublishTime;
    private TextView tvEndTime;
    private UserMessage userMessage;
    private ArrayList<String> itemList;
    private GridAlbumAdapter adapter;
    private int maxContent = 9;
    // 访问相册所需的全部权限
    private final static String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //读写权限
            Manifest.permission.CAMERA
    };
    private PictureChoosePopWindow pictureChoosePopWindow;
    private boolean isRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_active);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        tvTitle = $(R.id.tv_title);
        tvUserInfo = $(R.id.tv_user_info);
        tbopen = $(R.id.tb_open);
        gvPhoto = $(R.id.gv_photo);
        tvPublishTime = $(R.id.tv_publish_time);
        tvEndTime = $(R.id.tv_end_time);
        tvPublishTime.setOnClickListener(this);
        itemList = new ArrayList<>();
    }

    private void initData() {
        userMessage = UserMessage.getInstance(this);
        Intent intent = getIntent();
        tvTitle.setText(intent.getStringExtra("title"));
        tvPublishTime.setText(intent.getStringExtra("publicDate"));
        tvEndTime.setText(intent.getStringExtra("endDate"));
        tvUserInfo.setText(userMessage.getClassName() + "     " + userMessage.getUserName());
        pictureChoosePopWindow = new PictureChoosePopWindow(PublishActiveSActivity.this);
        adapter = new GridAlbumAdapter(itemList, this, maxContent);
        gvPhoto.setAdapter(adapter);
        gvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == itemList.size()) {
                    pictureChoosePopWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
                } else {
                    if (!isRecord){
                        PublishPhotoUtil.imageBrower(position, itemList, PublishActiveSActivity.this);
                    }else {
                        Intent i  = new Intent(getApplicationContext(),PreviewVideoActivity.class);
                        i.putExtra("path",itemList.get(0));
                        startActivity(i);
                    }
                }
            }
        });
    }

    /**
     * 前往获取图片
     */
    public void goSelectImager(final Activity activity) {
        PermissionsChecker checker = PermissionsChecker.getInstance(activity);
        if (checker.lacksPermissions(PERMISSIONS)) {
            checker.getPerMissions(PERMISSIONS);
            return;
        }
        AndroidImagePicker androidImagePicker = AndroidImagePicker.getInstance();
        androidImagePicker.setSelectLimit(maxContent - itemList.size());
        androidImagePicker.pickMulti(activity, true, new AndroidImagePicker.OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(List<ImageItem> items) {
                if (items != null && items.size() > 0) {
                    for (ImageItem item : items) {
                        G.log("选择了====" + item.path);
                        itemList.add(item.path);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        G.log("-----------xxxonResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //接收上个页面拍摄的照片

        G.log("-----------xxxonRestart");
    }

    @Override
    protected void setToolBar() {
        setCententTitle("活动上传");
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setSubTitle("发送");
        setSubTitleOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        G.log("--------xxx-----------"+"onActivityResult");
        switch (requestCode) {
            case PermissionsChecker.REQUEST_CODE:
                //检测到没有授取权限 关闭页面
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    G.showToast(this, "权限没有授取，本次操作取消，请到权限中心授权");
                } else if (resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
                    G.showToast(this, "权限获取成功！");
                }
                break;
        }
        switch (resultCode){
            case CameraCaptureActivity.REQUEST_PHOTO:
                String photoPath = data.getStringExtra("photoPath");
                itemList.add(photoPath);
                adapter.notifyDataSetChanged();
                break;
            case CameraCaptureActivity.REQUEST_VIDEO:
                String recorderPath = data.getStringExtra("recorderPath");
                itemList.add(recorderPath);
                isRecord = true;
                adapter.setVideo(isRecord,1);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onClick(View view) {

    }
}

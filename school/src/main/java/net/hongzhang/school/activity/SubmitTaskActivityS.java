package net.hongzhang.school.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import net.hongzhang.baselibrary.activity.PermissionsActivity;
import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.takevideo.PreviewVideoActivity;
import net.hongzhang.baselibrary.takevideo.VideoUtil;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.PermissionsChecker;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.NoScrollGirdView;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.PhotoVideoAlbumAdapter;
import net.hongzhang.school.presenter.SubmitTaskSContract;
import net.hongzhang.school.presenter.SubmitTaskSPresenter;
import net.hongzhang.school.widget.PictureChoosePopWindow;
import net.hongzhang.user.util.PublishPhotoUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 作者： wanghua
 * 时间： 2017/5/9
 * 名称： 家长提交活动作品
 * 版本说明：
 * 附加注释：
 * 主要接口：提交作品
 */
public class SubmitTaskActivityS extends BaseActivity implements View.OnClickListener, SubmitTaskSContract.View {
    /**
     * 标题
     */
    private TextView tvTitle;
    /**
     * 用户信息
     */
    private TextView tvUserInfo;
    /**
     * 是否公开
     */
    private ToggleButton tbopen;
    /**
     * 图片和视频列表
     */
    private NoScrollGirdView gvPhoto;
    /**
     * 发布时间
     */
    private TextView tvPublishTime;
    /**
     * 截止时间
     */
    private TextView tvEndTime;
    /**
     * 备注内容
     */
    private EditText et_content;
    private UserMessage userMessage;
    /**
     * 图片列表
     */
    private ArrayList<String> itemList;
    /**
     * 适配器
     */
    private PhotoVideoAlbumAdapter adapter;
    /**
     * 最大图片数
     */
    private int maxContent = 9;
    // 访问相册所需的全部权限
    private final static String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //读写权限
            Manifest.permission.CAMERA
    };
    /**
     * 图片选择
     */
    private PictureChoosePopWindow pictureChoosePopWindow;
    /**
     * 数据类
     */
    private SubmitTaskSPresenter presenter;
    /**
     * 活动id
     */
    private String activityId;
    /**
     * 视频位置
     */
    private String recorderPath;
    private boolean hasVideo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_task_s);
        ButterKnife.bind(this);
        initView();

        initData();
    }

    private void initView() {
        tvTitle = $(R.id.tv_title);
        et_content = $(R.id.et_content);
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
        final Intent intent = getIntent();
        tvTitle.setText(intent.getStringExtra("title"));
        tvPublishTime.setText(intent.getStringExtra("publicDate"));
        tvEndTime.setText(intent.getStringExtra("endDate"));
        activityId = intent.getStringExtra("activityId");
        tvUserInfo.setText(userMessage.getClassName() + "     " + userMessage.getUserName());
        pictureChoosePopWindow = new PictureChoosePopWindow(SubmitTaskActivityS.this);
        adapter = new PhotoVideoAlbumAdapter(itemList, this, maxContent);
        gvPhoto.setAdapter(adapter);
        gvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == itemList.size()) {
                    pictureChoosePopWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);
                } else {
                    if (itemList.size() - 1 == position && hasVideo) {
                        //  G.log("----------------------------------" + videoList.get(0));
                        Intent i = new Intent(getApplicationContext(), PreviewVideoActivity.class);
                        i.putExtra("path", recorderPath);
                        intent.putExtra("source",PreviewVideoActivity.SOURCE_SCHOOL);
                        startActivityForResult(i, PreviewVideoActivity.SOURCE_SCHOOL);
                    } else {
                        PublishPhotoUtil.imageBrower(position, itemList, SubmitTaskActivityS.this);
                    }
                }
            }
        });
        presenter = new SubmitTaskSPresenter(SubmitTaskActivityS.this, this);
    }

    /**
     * 前往获取图片
     *
     * @param activity
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
                        if (!hasVideo) {
                            itemList.add(item.path);
                        } else {
                            itemList.add(itemList.size() - 1, item.path);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void setToolBar() {
        setCententTitle("活动上传");
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setSubTitle("发送");
        setSubTitleOnClickListener(this);
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
        pictureChoosePopWindow.setHasVideo(hasVideo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        switch (resultCode) {
            case CameraCaptureActivity.REQUEST_PHOTO:
                String photoPath = data.getStringExtra("photoPath");
                itemList.add(photoPath);
                adapter.notifyDataSetChanged();
                break;
            case CameraCaptureActivity.REQUEST_VIDEO:
                recorderPath = data.getStringExtra("recorderPath");
                String path = VideoUtil.getVideoFirstFrame(recorderPath, this);
                hasVideo = true;
                pictureChoosePopWindow.setHasVideo(hasVideo);
                itemList.add(path);
                adapter.setHasVideo(hasVideo);
                adapter.notifyDataSetChanged();
                break;
            case PreviewVideoActivity.RESULT_CANCELED:
                if (itemList.size() > 0) {
                    itemList.remove(itemList.size() - 1);
                    hasVideo = false;
                    adapter.setHasVideo(hasVideo);
                    adapter.notifyDataSetChanged();
                }


        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_subtitle) {
            int isPublic = tbopen.isChecked() ? 1 : 2;
            String content = et_content.getText().toString().trim();
            if (!G.isEmteny(recorderPath)) {
                List<String> imageList = presenter.getImageList(itemList, recorderPath);
                presenter.submitTask(activityId, userMessage.getTsId(), content, isPublic, imageList);
            } else {
                presenter.submitTask(activityId, userMessage.getTsId(), content, isPublic, itemList);
            }
        }
    }
}

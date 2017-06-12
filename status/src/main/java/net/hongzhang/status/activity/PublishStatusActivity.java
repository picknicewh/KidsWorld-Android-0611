package net.hongzhang.status.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.activity.PermissionsActivity;
import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.takevideo.PreviewVideoActivity;
import net.hongzhang.baselibrary.takevideo.VideoUtil;
import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.PermissionsChecker;
import net.hongzhang.status.R;
import net.hongzhang.status.presenter.PublicStatusContract;
import net.hongzhang.status.presenter.PublicStatusPresenter;
import net.hongzhang.status.widget.StatusPublishPopWindow;
import net.hongzhang.user.adapter.GridAlbumAdapter;
import net.hongzhang.user.util.PublishPhotoUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wh
 * 时间： 2016/7/19
 * 名称：发布动态
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PublishStatusActivity extends BaseActivity implements View.OnClickListener, PublicStatusContract.View {
    /**
     * 文字的内容
     */
    private EditText et_content;
    /**
     * 文字的长度
     */
    private TextView tv_count;
    /**
     * 显示
     */
    private GridView gv_photo;
    private ArrayList<String> itemList;
    /**
     * 选择可见范围
     */
    private LinearLayout ll_permitchoose;
    /**
     * 选择的内容
     */
    private TextView tv_permitchoose;
    /**
     * 发布类型
     */
    private String dynamicType;
    /**
     * 限制内容
     */
    private RelativeLayout rl_restrict;
    private TextView tv_subtilte;
    /**
     * 班级id
     */
    private String classId;
    private int maxContent;
    private PublicStatusPresenter presenter;
    private boolean hasVideo;
    private String recorderPath;
    private List<String> recorderPaths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_status);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setSubTitle("完成");
        setSubTitleOnClickListener(this);
        setLiftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActviity();
            }
        });
    }

    //发布视频的第一帧
    private GridAlbumAdapter adapter;

    private void initVideoAdapter() {
        adapter = new GridAlbumAdapter(itemList, this, 1);
        gv_photo.setAdapter(adapter);
        gv_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (hasVideo) {
                    Intent intent = new Intent(getApplicationContext(), PreviewVideoActivity.class);
                    intent.putExtra("path", recorderPath);
                    intent.putExtra("source",PreviewVideoActivity.SOURCE_STATUS);
                    startActivityForResult(intent, PreviewVideoActivity.SOURCE_STATUS);
                } else {
                    Intent intent = new Intent(getApplicationContext(), CaptureVideoActivity.class);
                    intent.putExtra("type", StatusPublishPopWindow.VEDIO);
                    intent.putExtra("groupId", classId);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 初始化
     */
    private void initView() {
        et_content = $(R.id.et_pcontent);
        tv_count = $(R.id.tv_pcount);
        gv_photo = $(R.id.gv_photo);
        tv_permitchoose = $(R.id.tv_permitchoose);
        ll_permitchoose = $(R.id.ll_permitchoose);
        rl_restrict = $(R.id.rl_restrict);
        tv_subtilte = $(R.id.tv_subtitle);
        tv_subtilte.setOnClickListener(this);
        ll_permitchoose.setOnClickListener(this);
        initData();
    }

    private void initData() {
        itemList = new ArrayList<>();
        recorderPaths = new ArrayList<>();
        Intent data = getIntent();
        DateUtil.setEditContent(et_content, tv_count);
        rl_restrict.setVisibility(View.VISIBLE);
        classId = data.getStringExtra("groupId");
        presenter = new PublicStatusPresenter(PublishStatusActivity.this, this);
        int type = data.getIntExtra("type",1);
        if (type==StatusPublishPopWindow.VEDIO){
            initVideoAdapter();
        }
        showView(type);
    }

    /**
     * 显示不同值传过来界面的状态
     *
     * @param type 类型值
     */
    private void showView(int type) {
        switch (type) {
            case StatusPublishPopWindow.WORDS:
                setCententTitle("发布文字");
                dynamicType = "3";
                break;
            case StatusPublishPopWindow.PICTURE:
                setCententTitle("发布图片");
                maxContent = 9;
                dynamicType = "1";
                PublishPhotoUtil.goSelectImager(itemList, this, gv_photo, maxContent);
                PublishPhotoUtil.showPhoto(this, itemList, gv_photo, maxContent);
                break;
            case StatusPublishPopWindow.VEDIO:
                setCententTitle("发布视频");
                dynamicType = "2";
                maxContent = 1;
                recorderPath = getIntent().getStringExtra("recorderPath");
                recorderPaths.add(recorderPath);
                if (!G.isEmteny(recorderPath)) {
                    String path = VideoUtil.getVideoFirstFrame(recorderPath, this);
                    hasVideo = true;
                    itemList.add(path);
                    adapter.notifyDataSetChanged();
                }
                break;
            case 4:
                rl_restrict.setVisibility(View.GONE);
                setCententTitle("发布课程");
                maxContent = 1;
                dynamicType = "4";
                PublishPhotoUtil.goSelectImager(itemList, this, gv_photo, maxContent);
                PublishPhotoUtil.showPhoto(this, itemList, gv_photo, maxContent);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.ll_permitchoose) {
            Intent intent = new Intent();
            intent.setClass(this, ChoosePermitActivity.class);
            intent.putExtra("permit", tv_permitchoose.getText().toString());
            startActivityForResult(intent, ChoosePermitActivity.CHOOSE_PERMIT);
        } else if (viewId == R.id.tv_subtitle) {
            tv_subtilte.setEnabled(false);
            String dyContent = et_content.getText().toString().trim();
            if (dynamicType.equals("4")) {
                presenter.publishcaurse(dyContent, itemList);
            } else {
                presenter.publishstatus(dyContent, dynamicType, recorderPaths, classId);
            }
        }
    }
    private void closeActviity(){
        if (!G.isEmteny(et_content.getText().toString().trim())
                || dynamicType.equals("1") && itemList.size() >= 1
                || dynamicType.equals("4") && itemList.size() == 1) {
            presenter.getExitPrompt();
        } else {
            finish();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PermissionsChecker.REQUEST_CODE) {
            //检测到没有授取权限 关闭页面
            if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                G.showToast(this, "权限没有授取，本次操作取消，请到权限中心授权");
            } else if (resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
                G.showToast(this, "权限获取成功！");
            }
        }
        //预览图中删除视频
        if (resultCode==PreviewVideoActivity.RESULT_DELETE){
            if (itemList!=null && itemList.size()>0){
                itemList.remove(0);
                adapter.notifyDataSetChanged();
            }

        }

    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!G.isEmteny(et_content.getText().toString().trim())
                    || dynamicType.equals("1") && itemList.size() > 1) {
                presenter.getExitPrompt();
            } else {
                finish();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (dynamicType.equals("4") && itemList.size() < 1
                || null != dynamicType && dynamicType.equals("1") && itemList.size() < 1) {
            finish();
        }
    }
    @Override
    public void setSubTitleEnable(boolean enable) {
        tv_subtilte.setEnabled(enable);
    }
}


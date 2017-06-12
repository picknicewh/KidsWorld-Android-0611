package net.hongzhang.school.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bigkoo.pickerview.TimePickerView;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import net.hongzhang.baselibrary.activity.PermissionsActivity;
import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.PermissionsChecker;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.NoScrollGirdView;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.MyDimensionalityAdapter;
import net.hongzhang.school.presenter.PublishActiveTContract;
import net.hongzhang.school.presenter.PublishActiveTPresenter;
import net.hongzhang.user.adapter.GridAlbumAdapter;
import net.hongzhang.user.util.PublishPhotoUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 老师发布活动
 * 发布活动
 */
public class PublishActiveActivityT extends BaseDateSelectActivity implements View.OnClickListener, PublishActiveTContract.View {
    /**
     * 图片选择
     */
    private NoScrollGirdView gvPicture;
    /**
     * 图片列表
     */
    private ArrayList<String> imgList;
    /**
     * 适配器
     */
    private GridAlbumAdapter albumAdapter;
    /**
     * 提交时间
     */
    private LinearLayout ll_commit_time;
    private TextView tv_commit_time;
    /**
     * 发布时间
     */
    private LinearLayout ll_publish_time;
    private TextView tv_publish_time;
    /**
     * 最大发布时间数
     */
    private int maxContent = 9;
    /**
     * 是否评估
     */
    private ToggleButton tb_conform_comment;
    /**
     * 考核维度
     */
    private RecyclerView rv_dimensionality;
    /**
     * 活动分类
     */
    private RadioButton rb_yubao;
    private RadioButton rb_teach;
    /**
     * 活动标题编辑框
     */
    private EditText et_title;
    /**
     * 活动内容编辑框
     */
    private EditText et_content;
    private ImageView iv_back;
    // 访问相册所需的全部权限
    private final static String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //读写权限
            Manifest.permission.CAMERA
    };
    private PublishActiveTPresenter presenter;
    private String tsId;
    private MyDimensionalityAdapter dimensionalityAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_active_t);
        initView();
    }

    private void initView() {
        iv_back = $(R.id.iv_back);
        gvPicture = $(R.id.gv_picture);
        ll_commit_time = $(R.id.ll_commit_time);
        ll_publish_time = $(R.id.ll_publish_time);
        tv_commit_time = $(R.id.tv_commit_time);
        tv_publish_time = $(R.id.tv_publish_time);
        tb_conform_comment = $(R.id.tb_conform_comment);
        et_content = $(R.id.et_content);
        et_title = $(R.id.et_title);
        rv_dimensionality = $(R.id.rv_dimensionality);
        rb_yubao = $(R.id.rb_yubao);
        rb_teach = $(R.id.rb_teach);
        ll_publish_time.setOnClickListener(this);
        ll_commit_time.setOnClickListener(this);
        setTv_start(tv_publish_time);
        setTv_end(tv_commit_time);
        initData();
        //  selectTagTrans(tv_select_more);


    }

    private void initData() {
        initDate();
        imgList = new ArrayList<>();
        tsId = UserMessage.getInstance(this).getTsId();
        albumAdapter = new GridAlbumAdapter(imgList, this, 9);
        gvPicture.setAdapter(albumAdapter);
        gvPicture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == imgList.size()) {
                    goSelectImager(PublishActiveActivityT.this);
                } else {
                    PublishPhotoUtil.imageBrower(position, imgList, PublishActiveActivityT.this);
                }
            }
        });
        presenter = new PublishActiveTPresenter(PublishActiveActivityT.this, this);
        presenter.getActivityType();
        presenter.getDimensionality();
        presenter.setBackRepAnimation(iv_back);
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
        androidImagePicker.setSelectLimit(maxContent - imgList.size());
        androidImagePicker.pickMulti(activity, true, new AndroidImagePicker.OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(List<ImageItem> items) {
                if (items != null && items.size() > 0) {
                    for (ImageItem item : items) {
                        //  G.log("选择了====" + item.path);
                        imgList.add(item.path);
                    }
                }
                albumAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.ll_publish_time) {
            presenter.getTimePickerView(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    startTime =  date.getTime();
                    tv_start.setText(DateUtil.format_chinese.format(date));
                }
            }).show();

        } else if (viewId == R.id.ll_commit_time) {
            presenter.getTimePickerView(this, new TimePickerView.OnTimeSelectListener() {

                @Override
                public void onTimeSelect(Date date, View v) {
                    endTime =  date.getTime();
                    tv_end.setText(DateUtil.format_chinese.format(date));
                }
            }).show();
        } else if (viewId == R.id.tv_subtitle) {
            String title = et_title.getText().toString();
            String content = et_content.getText().toString();
            int isAppraise = tb_conform_comment.isChecked() ? 1 : 2;
            if (G.isEmteny(title) || G.isAllSpace(title)) {
                G.showToast(this, "活动主题不能为空哦！");
                return;
            }
            if (G.isEmteny(content) || G.isAllSpace(content)) {
                G.showToast(this, "活动内容不能为空哦！");
                return;
            }
            String dimensionality = dimensionalityAdapter.getSelected();
            if (G.isEmteny(dimensionality)) {
                G.showToast(this, "活动维度不能为空！");
                return;
            }
            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            if (endTime == 0) {
                G.showToast(this, "请选择截止时间哦！");
                return;
            }
            String type;
            if (rb_teach.isChecked()) {
                type = codes.get(0);
            } else {
                type = codes.get(1);
            }
            presenter.publishActive(tsId, title, content, type, dimensionality, startTime, endTime, isAppraise, imgList);
        }
    }

    @Override
    protected void setToolBar() {
        setLiftOnClickClose();
        setCententTitle("发布活动");
        setSubTitle("发送");
        setSubTitleOnClickListener(this);
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
    }

    private List<String> codes;

    @Override
    public void setActivityType(Map<String, String> param) {
        List<String> values = new ArrayList<>();
        codes = new ArrayList<>();
        Set<Map.Entry<String, String>> entries = param.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            values.add(entry.getValue());
            codes.add(entry.getKey());
        }
        rb_teach.setText(values.get(0));
        rb_yubao.setText(values.get(1));
    }

    @Override
    public void setDimensionality(Map<String, String> param) {
        dimensionalityAdapter = new MyDimensionalityAdapter(this, param);
        rv_dimensionality.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_dimensionality.setAdapter(dimensionalityAdapter);
    }
}

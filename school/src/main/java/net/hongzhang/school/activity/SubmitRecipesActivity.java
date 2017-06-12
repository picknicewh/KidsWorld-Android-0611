package net.hongzhang.school.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import net.hongzhang.baselibrary.activity.PermissionsActivity;
import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.MapVo;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.PermissionsChecker;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.NoScrollGirdView;
import net.hongzhang.school.R;
import net.hongzhang.school.presenter.SubmitRecipesContract;
import net.hongzhang.school.presenter.SubmitRecipesPresenter;
import net.hongzhang.user.adapter.GridAlbumAdapter;
import net.hongzhang.user.util.PublishPhotoUtil;

import java.util.ArrayList;
import java.util.List;

public class SubmitRecipesActivity extends BaseActivity implements View.OnClickListener, SubmitRecipesContract.View {
    private TextView tv_date;
    private TextView tv_class;
    private EditText et_content;
    private NoScrollGirdView gv_photo;
    /**
     * 适配器
     */
    private GridAlbumAdapter adapter;
    /**
     * 最大图片数
     */
    private int maxContent = 9;
    /**
     * 图片列表
     */
    private ArrayList<String> itemList;
    // 访问相册所需的全部权限
    private final static String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, //读写权限
            Manifest.permission.CAMERA
    };
    private LinearLayout ll_time;
    private TextView tv_time;
    private SubmitRecipesPresenter presenter;
    private UserMessage userMessage;
    private String classId;
    private List<MapVo> mapVoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_recipes);
        initView();
    }

    private void initView() {
        tv_date = $(R.id.tv_date);
        tv_class = $(R.id.tv_class);
        et_content = $(R.id.et_content);
        gv_photo = $(R.id.gv_photo);
        ll_time = $(R.id.ll_time);
        tv_time = $(R.id.tv_time);
        ll_time.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        classId = intent.getStringExtra("classId");
        tv_date.setText(intent.getStringExtra("date"));
        userMessage = new UserMessage(this);
        itemList = new ArrayList<>();
        mapVoList = new ArrayList<>();
        adapter = new GridAlbumAdapter(itemList, this, maxContent);
        gv_photo.setAdapter(adapter);
        gv_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == itemList.size()) {
                    goSelectImager(SubmitRecipesActivity.this);
                } else {
                    PublishPhotoUtil.imageBrower(position, itemList, SubmitRecipesActivity.this);
                }
            }
        });
        presenter = new SubmitRecipesPresenter(SubmitRecipesActivity.this, this);
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("食谱");
        setSubTitle("发送");
        setLiftOnClickClose();
        setSubTitleOnClickListener(this);
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
                        itemList.add(item.path);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
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

    private String chooseType;

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tv_time || viewId == R.id.ll_time) {
            presenter.optionsPickerView(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    chooseType = mapVoList.get(options1).getKey();
                    tv_time.setText(mapVoList.get(options1).getValue());
                }
            }).show();
        } else if (viewId == R.id.tv_subtitle) {
            String content = et_content.getText().toString();
            if (G.isEmteny(content) || G.isAllSpace(content)) {
                G.showToast(this, "食谱描述不能为空哦！");
                return;
            }
            if (G.isEmteny(chooseType)) {
                G.showToast(this, "时间点不能为空哦！");
                return;
            }
            presenter.submitRecipes(userMessage.getTsId(), classId, content, chooseType, itemList);
        }
    }


    @Override
    public void setRecipesTime(List<MapVo> times) {
        this.mapVoList = times;
    }
}

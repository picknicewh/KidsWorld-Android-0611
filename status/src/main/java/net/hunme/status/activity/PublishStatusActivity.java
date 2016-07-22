package net.hunme.status.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.status.R;
import net.hunme.status.widget.StatusPublishPopWindow;
import net.hunme.user.activity.AlbumActivity;
import net.hunme.user.activity.UploadPhotoActivity;
import net.hunme.user.adapter.GridAdapter;
import net.hunme.user.util.Bimp;

import java.io.File;

/**
 * 作者： wh
 * 时间： 2016/7/19
 * 名称：发布动态
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PublishStatusActivity extends BaseActivity implements View.OnClickListener{
    /**
     * 文字的内容
     */
    private EditText et_content;
    /**
     *文字的长度
     */
    private TextView tv_count;
    private GridView gv_photo;
    private GridAdapter adapter;
    /**
     * 选择可见范围
     */
    private LinearLayout ll_permitchoose;
    /**
     * 选择的内容
     */
    private TextView tv_permitchoose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_status);
        initView();
        showView(getIntent().getIntExtra("type",-1));
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickListener(this);
        setCententTitle("发布");
        setSubTitle("完成");
    }
    /**
     * 显示不同值传过来界面的状态
     * @param  type 类型值
     */
    private void showView(int type){
        switch (type){
            case StatusPublishPopWindow.WORDS:
                break;
            case StatusPublishPopWindow.PICTURE:
                Intent intent = new Intent(PublishStatusActivity.this,
                        AlbumActivity.class);
                startActivityForResult(intent,UploadPhotoActivity.TAKE_PICTURE);
                showPhoto();
                break;
            case StatusPublishPopWindow.VEDIO:
                break;
        }
    }
    /**
     * 初始化
     */
    private void initView(){
        et_content = $(R.id.et_pcontent);
        tv_count = $(R.id.tv_pcount);
        gv_photo =$(R.id.gv_photo);
        tv_permitchoose =  $(R.id.tv_permitchoose);
        ll_permitchoose = $(R.id.ll_permitchoose);
        ll_permitchoose.setOnClickListener(this);
        setEditContent();

    }

    /**
     * 计算文字的长度，如果文字的长度大于100则不能再输入
     */
    private void setEditContent() {
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                int count = 0;
                count = str.length();
                if (count > 140) {
                    deleteSelection(s);
                }else {
                    tv_count.setText(count+"/140");
                }
            }
            private void deleteSelection(Editable s) {
                int selection = et_content.getSelectionStart();
                if (selection > 1) {
                    s.delete(selection - 1, selection);
                }
            }
        });
    }

    /**
     * 发布照片页面
     */
    private void showPhoto(){
        adapter=new GridAdapter(this);
        adapter.update();
        gv_photo.setAdapter(adapter);
        gv_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == Bimp.bmp.size()) {
                    Intent intent = new Intent(PublishStatusActivity.this,
                            AlbumActivity.class);
                    startActivityForResult(intent,UploadPhotoActivity.TAKE_PICTURE);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case UploadPhotoActivity.TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < 9 && resultCode == -1) {
                    File file = new File(Environment.getExternalStorageDirectory()
                            + "/myimage/", String.valueOf(System.currentTimeMillis())
                            + ".jpg");
                    Bimp.tempSelectBitmap.add(file.getPath());
                }
                adapter.update();
                adapter.notifyDataSetChanged();
                break;
            case ChoosePermitActivity.CHOOSE_PERMIT:
                if (data!=null){
                    String permit =  data.getStringExtra("permit");
                    tv_permitchoose.setText(permit);
                }
                break;
        }
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId==R.id.iv_left){
            finish();
        }else if (viewId==R.id.ll_permitchoose){
            Intent intent = new Intent();
            intent.setClass(this,ChoosePermitActivity.class);
            intent.putExtra("permit",tv_permitchoose.getText().toString());
            startActivityForResult(intent,ChoosePermitActivity.CHOOSE_PERMIT);
        }
    }
}


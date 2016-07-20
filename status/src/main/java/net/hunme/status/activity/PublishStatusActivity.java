package net.hunme.status.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.status.R;
import net.hunme.status.widget.StatusPublishPopWindow;

/**
 * 作者： wh
 * 时间： 2016/7/19
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class PublishStatusActivity extends BaseActivity {
    /**
     * 文字的内容
     */
    private EditText et_content;
    /**
     *文字的长度
     */
    private TextView tv_count;
    /**
     * 发布类型的值
     */
    private  int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_status);
        initView();
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setCententTitle("发布");
        setSubTitle("完成");
        setLiftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /**
     * 显示不同值传过来界面的状态
     * @param  type 类型值
     */
    private void  showView(int type){
        switch (type){
            case StatusPublishPopWindow.WORDS:

                break;
            case StatusPublishPopWindow.PICTURE:
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
        setEditContent();
        type  = getIntent().getIntExtra("type",-1);
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
                if (count > 100) {
                    deleteSelection(s);
                }else {
                    tv_count.setText(count+"/100");
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
}


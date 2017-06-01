package net.hongzhang.school.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.widget.MyAlertDialog;
import net.hongzhang.school.R;
import net.hongzhang.school.adapter.TaskCommentChildTagAdapter;

/**
 * 作者： wh
 * 时间： 2016/9/8
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class AddTagDialog implements View.OnClickListener {
    private Activity context;
    /**
     * 对话框布局
     */
    private View contentView;
    /**
     * 确定
     */
    private Button bt_conform;
    /**
     * 取消
     */
    private Button bt_cancel;
    /**
     * 创建的群组名
     */
    private EditText et_tag;
    /**
     * 对话框
     */
    private AlertDialog alertDialog;
    private TaskCommentChildTagAdapter adapter;
    public AddTagDialog(Activity context, TaskCommentChildTagAdapter adapter) {
        this.context = context;
        this.adapter =adapter;
    }

    public void initView() {
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_add_tag, null);
        alertDialog = MyAlertDialog.getDialog(contentView, context, 1);
        bt_conform = (Button) contentView.findViewById(R.id.bt_conform);
        bt_cancel = (Button) contentView.findViewById(R.id.bt_cancel);
        et_tag = (EditText) contentView.findViewById(R.id.et_tag);
        bt_cancel.setOnClickListener(this);
        bt_conform.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_cancel) {
        } else if (view.getId() == R.id.bt_conform) {
            String tag = et_tag.getText().toString().trim();
            if (!G.isAllSpace(tag)|| !G.isEmteny(tag)){
               // adapter.addTag(et_tag.getText().toString());
            }else {
                G.showToast(context,"标签不能为空哦！");
            }
        }
        alertDialog.dismiss();
    }
}

package net.hongzhang.school.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.MyAlertDialog;
import net.hongzhang.school.R;
import net.hongzhang.school.presenter.SubmitMedicinePresenter;

/**
 * 作者： wh
 * 时间： 2016/9/8
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class AddTypeDialog implements View.OnClickListener {
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
    private EditText et_text;
    /**
     * 对话框
     */
    private AlertDialog alertDialog;
    private String content;
    private SubmitMedicinePresenter presenter;
    private String tsId;
    private TextView tv_title;
    public AddTypeDialog(Activity context, String content, SubmitMedicinePresenter presenter) {
        this.context = context;
        this.content = content;
        this.presenter = presenter;
        tsId = new UserMessage(context).getTsId();
    }

    public void initView() {
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_add_type, null);
        alertDialog = MyAlertDialog.getDialog(contentView, context, 1);
        tv_title =  (TextView) contentView.findViewById(R.id.tv_title);
        bt_conform = (Button) contentView.findViewById(R.id.bt_conform);
        bt_cancel = (Button) contentView.findViewById(R.id.bt_cancel);
        et_text = (EditText) contentView.findViewById(R.id.et_text);
        et_text.setHint(content);
        if (content.contains("患病")){
            tv_title.setText("添加患病类型");
        }else {
            tv_title.setText("添加药品类型");
        }
        bt_cancel.setOnClickListener(this);
        bt_conform.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_cancel) {
            alertDialog.dismiss();
        } else if (view.getId() == R.id.bt_conform) {
            String tag = et_text.getText().toString().trim();
            if (!G.isAllSpace(tag) || !G.isEmteny(tag)) {
                if (content.contains("患病")) {
                    presenter.setSickText(tag);
                    presenter.addSickenType(tsId, tag);
                } else {
                    presenter.setMedicineText(tag);
                    presenter.addDrigType(tsId, tag);
                }
            } else {
                G.showToast(context, "所填项不能为空哦！");
            }
            alertDialog.dismiss();
        }

    }
}

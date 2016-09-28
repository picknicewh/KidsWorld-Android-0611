package net.hunme.user.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.MyAlertDialog;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.user.R;
import net.hunme.user.activity.UMassageActivity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/9/28
 * 名称：编辑签名对话框
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class SignDialog implements View.OnClickListener, OkHttpListener {
    private Activity context;
    private AlertDialog dialog;
    /**
     * 取消按钮
     */
    private Button b_cancle;
    /**
     * 确定按钮
     */
    private Button b_confirm;
    /**
     * 签名
     */
    private EditText et_sign;
    private UserMessage userMessage;
    private String sign;
    public  SignDialog(Activity context){
       this.context = context;
        userMessage =UserMessage.getInstance(context);
    }
    public void initView(){
        //获取View
         View dialogView = LayoutInflater.from(context).inflate(R.layout.alertdialog_add_album, null);
        //获取弹框
        dialog= MyAlertDialog.getDialog(dialogView,context,1);
        b_cancle= (Button) dialogView.findViewById(R.id.b_cancel);
        b_confirm= (Button) dialogView.findViewById(R.id.b_confirm);
        et_sign= (EditText) dialogView.findViewById(R.id.et_album_name);
        et_sign.setHint("请输入签名");
        b_confirm.setOnClickListener(this);
        b_cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId==R.id.b_cancel){
            dialog.dismiss();
        }else if (viewId==R.id.b_confirm){
            sign=et_sign.getText().toString();
            if (G.isEmteny(sign)) {
                G.showToast(context,"签名不能为空");
                return;
            }
            userSignSubmit(sign);
            dialog.dismiss();
        }
    }
    /**
     * 提交用户个性签名
     * @param userSign
     */
    public void userSignSubmit(String userSign){
        Map<String,Object> map=new HashMap<>();
        map.put("tsId", UserMessage.getInstance(context).getTsId());
        map.put("signature",userSign);
        Type type=new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.SETSIGN,map,this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if( Apiurl.SETSIGN.equals(uri)){
            userMessage.setUserSign(sign);
            UMassageActivity.tv_sign.setText(userMessage.getUserSign());
            G.showToast(context,"签名修改成功");
        }
    }

    @Override
    public void onError(String uri, String error) {
        G.showToast(context,error);
    }
}

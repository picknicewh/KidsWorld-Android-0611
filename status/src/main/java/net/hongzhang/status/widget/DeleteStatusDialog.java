package net.hongzhang.status.widget;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.BaseConformDialog;
import net.hongzhang.status.R;
import net.hongzhang.status.StatusFragement;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/9/19
 * 名称：删除我的动态对话框
 * 版本说明：
 * 附加注释：
 * 主要接口：1.删除我的动态
 */
public class DeleteStatusDialog extends BaseConformDialog implements View.OnClickListener, OkHttpListener {
    private StatusFragement statusFragement;
    private Activity context;
    /**
     * 课程id
     */
    private String dynamicId;

    /**
     * 位置
     */
    private int position;
    public DeleteStatusDialog(StatusFragement statusFragement, String dynamicId,int position){
        this.statusFragement  = statusFragement;
        context = statusFragement.getActivity();
        this.dynamicId = dynamicId;
        this.position = position;
    }
    /**
     * 初始化view
     */
    public void  initView(){
        String content = "确定删除这条动态吗？";
        initView(context,content);
    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.bt_conform){
            deletedynamic(dynamicId);
            alertDialog.dismiss();
        }
    }
    @Override
    public void onSuccess(String uri, Object date) {
        Result<String> data = (Result<String>) date;
        if (data!=null){
            String  result = data.getData();
            Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
            statusFragement.updateDelete(position);
        }
        alertDialog.dismiss();
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
    }
    /**
     * 删除课程
     * @param  dynamicId 动态id
     */
    private void deletedynamic(String dynamicId){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(context).getTsId());
        params.put("dynamicId",dynamicId);
        Type type = new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.DELETEMYDYNAMICS,params,this);
    }
}

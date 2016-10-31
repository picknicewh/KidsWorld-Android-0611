package net.hunme.status.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.MyAlertDialog;
import net.hunme.status.R;
import net.hunme.status.StatusFragement;

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
public class DeleteStatusDialog implements View.OnClickListener, OkHttpListener {
    private StatusFragement statusFragement;
    private Context context;
    /**
     * 确定
     */
    private Button bt_conform;
    /**
     * 取消
     */
    private Button bt_cancel;
    /**
     * 对话框
     */
    private AlertDialog alertDialog;
    /**
     * 课程id
     */
    private String dynamicId;
    /**
     * 页码数
     */

    private String groupId;
    private String groupType;
    public DeleteStatusDialog(StatusFragement statusFragement, String dynamicId,String  groupId,String  groupType){
        this.statusFragement  = statusFragement;
        context = statusFragement.getActivity();
        this.dynamicId = dynamicId;
        this.groupId = groupId;
        this.groupType =groupType;
    }
    /**
     * 初始化view
     */
    public void  initView(){
         View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_status, null);
         alertDialog = MyAlertDialog.getDialog(contentView, statusFragement.getActivity(),1);
         bt_conform = (Button) contentView.findViewById(R.id.bt_conform);
         bt_cancel = (Button) contentView.findViewById(R.id.bt_cancel);
         bt_cancel.setOnClickListener(this);
        bt_conform.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.bt_conform){
            deletedynamic(dynamicId);
            alertDialog.dismiss();
        }else if (view.getId()==R.id.bt_cancel){
            alertDialog.dismiss();
        }
    }
    @Override
    public void onSuccess(String uri, Object date) {
        Result<String> data = (Result<String>) date;
        if (data!=null){
            String  result = data.getData();
            Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
            for (int i = 0 ; i<statusFragement.statusVoList.size();i++){
                if (statusFragement.statusVoList.get(i).getDynamicId().equals(dynamicId)){
                    statusFragement.statusVoList.remove(i);
                }
            }
            statusFragement.adapter.notifyDataSetChanged();
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
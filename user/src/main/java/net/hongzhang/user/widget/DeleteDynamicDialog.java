package net.hongzhang.user.widget;

import android.view.View;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.BaseConformDialog;
import net.hongzhang.user.R;
import net.hongzhang.user.activity.MyDynamicActivity;

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
public class DeleteDynamicDialog  extends BaseConformDialog implements View.OnClickListener, OkHttpListener {
    private MyDynamicActivity context;
    /**
     * 动态id
     */
    public String dynamicId;
    /**
     * 位置
     */
    private int position;
    public DeleteDynamicDialog(MyDynamicActivity context, String dynamicId,int position){
        this.context  = context;
        this.dynamicId = dynamicId;
        this.position  = position;
       // this.pageNumber = pageNumber;
    }
    /**
     * 初始化view
     */
    public void  initView(){
         String content = "确定删除这条动态吗？";
         initView(context,content);
         /*View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_dynamic, null);
         alertDialog = MyAlertDialog.getDialog(contentView, context,1);
         bt_conform = (Button) contentView.findViewById(R.id.bt_conform);
         bt_cancel = (Button) contentView.findViewById(R.id.bt_cancel);
        // bt_cancel.setOnClickListener(this);
         bt_conform.setOnClickListener(this);*/
    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.bt_conform){
            deletedynamic(dynamicId);
            alertDialog.dismiss();
        }/*else if (view.getId()==R.id.bt_cancel){
            alertDialog.dismiss();
        }*/
    }
    @Override
    public void onSuccess(String uri, Object date) {
        context.stopLoadingDialog();
        Result<String> data = (Result<String>) date;
        if (data!=null){
            String  result = data.getData();
            Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
         /*   for (int i = 0;i< context.dynamicInfoVoList.size();i++){
                if (context.dynamicInfoVoList.get(i).getDynamicId().equals(dynamicId)){
                    context.dynamicInfoVoList.remove(i);
                }
            }
            context.adapter.notifyDataSetChanged();*/
            context.updateDelete(position);
        }
        alertDialog.dismiss();
    }
    @Override
    public void onError(String uri, String error) {
        context.stopLoadingDialog();
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
    }
    /**
     * 删除动态
     * @param  dynamicId 动态id
     */
    private void deletedynamic(String dynamicId){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(context).getTsId());
        params.put("dynamicId",dynamicId);
        Type type = new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.DELETEMYDYNAMICS,params,this);
        context.showLoadingDialog();
    }
}

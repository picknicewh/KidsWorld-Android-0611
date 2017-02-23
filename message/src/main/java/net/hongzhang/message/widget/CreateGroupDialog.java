package net.hongzhang.message.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.contract.GroupDb;
import net.hongzhang.baselibrary.contract.GroupsDbHelper;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.widget.MyAlertDialog;
import net.hongzhang.message.R;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/9/8
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class CreateGroupDialog implements View.OnClickListener, OkHttpListener {
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
    private EditText et_Groupname;
    /**
     * 对话框
     */
    private AlertDialog alertDialog;
    /**
     * 群组成员所有id
     */
    private String targetIds;
    /**
     * 群名称
     */
    private String groupName;
    /**
     * 修改数据库
     */
    public CreateGroupDialog(Activity context, String targetIds){
        this.context  = context;
        this.targetIds = targetIds;


    }
    public void  initView(){
         contentView = LayoutInflater.from(context).inflate(R.layout.dialog_addgroup, null);
         alertDialog = MyAlertDialog.getDialog(contentView, context,1);
         bt_conform = (Button) contentView.findViewById(R.id.bt_gconform);
         bt_cancel = (Button) contentView.findViewById(R.id.bt_gcancel);
         et_Groupname = (EditText) contentView.findViewById(R.id.et_gname);
         bt_cancel.setOnClickListener(this);
         bt_conform.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
     if (view.getId()==R.id.bt_gcancel){
         alertDialog.dismiss();
     }else if (view.getId()==R.id.bt_gconform){
         createGroup();
         alertDialog.dismiss();
         bt_conform.setClickable(false);
      }
    }
    /**
     * 创建群
     */
    private void createGroup(){
        Map<String,Object> params = new HashMap<>();
        if (TextUtils.isEmpty(et_Groupname.getText().toString())){
            Toast.makeText(context,"群名字不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        groupName = et_Groupname.getText().toString();
        params.put("groupName",groupName);
        params.put("tsIds",targetIds);
        Type type =new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_CREATE_GROUP,params,this);
    }
    @Override
    public void onSuccess(String uri, Object date) {
        if (uri.equals(Apiurl.MESSAGE_CREATE_GROUP)){
            Result<String> data  = (Result<String>) date;
            if (data!=null){
                String result = data.getData();
                GroupsDbHelper  helper = GroupsDbHelper.getinstance();
                SQLiteDatabase  database = new GroupDb(context).getWritableDatabase();
                helper.insert(database,groupName,targetIds,0,0);
                Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                context.finish();
            }
        }
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
    }
}

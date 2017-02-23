package net.hongzhang.message.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.contract.GroupDb;
import net.hongzhang.baselibrary.contract.GroupsDbHelper;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.message.R;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/9/12
 * 名称：修改群组用户名
 * 版本说明：3.0.2
 * 附加注释：
 * 主要接口：
 */
public class ModifyNameActivity extends BaseActivity implements OkHttpListener {
    private EditText et_name;
    /**
     * 群数据库操作类
     */
    private GroupsDbHelper dbHelper;
    /**
     * 群数据库类
     */
    private   GroupDb groupDb;
    /**
     * 更新群组的名称
     */
    private String groupName;
    /**
     * 群id
     */
    private String targetGroupId;
    /**
     * 群名称
     */
    private    String targetGroupName;

    private SharedPreferences spf;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyname);
        initView();
    }
    /**
     * 初始化数据
     */
    private void initView() {
         et_name = $(R.id.et_groupname);
         dbHelper = new GroupsDbHelper();
         groupDb = new GroupDb(this);
         setGroupInfo();
         setSubTitleOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editName(targetGroupId);
                finish();
            }
        });
    }
    private void setGroupInfo(){
        spf=getSharedPreferences("name", Context.MODE_PRIVATE);
        editor=spf.edit();
        targetGroupId =  spf.getString("targetGroupId","");
        targetGroupName = spf.getString("groupName","");
        et_name.setText(targetGroupName);
//        et_name.setHint(targetGroupName);
    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("群聊名称");
        setSubTitle("完成");
    }
    /**
     * 修改群名称
     * @param targetGroupId 群组id
     */
     private void editName(String targetGroupId){
         groupName = et_name.getText().toString();
         editor.putString("groupName",groupName);
         editor.commit();
        Map<String,Object> params = new HashMap<>();
        params.put("tsId",UserMessage.getInstance(this).getTsId());
        params.put("groupChatAdmin", UserMessage.getInstance(this).getTsId());
        params.put("groupChatId",targetGroupId);
        if (TextUtils.isEmpty(et_name.getText().toString())){
            Toast.makeText(this,"群组名不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        params.put("groupChatName",groupName);
         //更新数据库
        dbHelper.updateGroupName(groupDb.getWritableDatabase(),groupName,targetGroupId);
        Type type =new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_EDITGROUPNAME,params,this);
    }
     @Override
     public void onSuccess(String uri, Object date) {
        Result<String> data  = (Result<String>) date;
        if (data!=null){
            String result = data.getData();
            Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
        }
     }
     @Override
     public void onError(String uri, String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
     }
}

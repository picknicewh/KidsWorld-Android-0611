package net.hongzhang.message.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import net.hongzhang.baselibrary.network.DetaiCodeUtil;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.MyAlertDialog;
import net.hongzhang.message.R;
import net.hongzhang.message.activity.ConservationActivity;
import net.hongzhang.message.activity.GroupDetailActivity;

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
public class GroupNameOperationDialog implements View.OnClickListener, OkHttpListener {
    public static final int CREATE_GROUP = 0;
    public static final int EDIT_GROUP = 1;
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
    private int operation;

    public GroupNameOperationDialog(Activity context, String targetIds, int operation) {
        this.context = context;
        this.targetIds = targetIds;
        this.operation = operation;
    }

    public void initView() {
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_addgroup, null);
        alertDialog = MyAlertDialog.getDialog(contentView, context, 1);
        bt_conform = (Button) contentView.findViewById(R.id.bt_gconform);
        bt_cancel = (Button) contentView.findViewById(R.id.bt_gcancel);
        et_Groupname = (EditText) contentView.findViewById(R.id.et_gname);
        bt_cancel.setOnClickListener(this);
        bt_conform.setOnClickListener(this);
        et_Groupname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               String str = editable.toString();
                if (str.contains("\n")){
                    groupName = str.replace("\n","");
                    et_Groupname.setText(groupName);
                }else {
                    groupName = str;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_gcancel) {
            alertDialog.dismiss();
        } else if (view.getId() == R.id.bt_gconform) {
            groupName = et_Groupname.getText().toString().trim();
            if (operation==CREATE_GROUP){
                createGroup(targetIds,groupName);
            }else {
                editName(targetIds,groupName);
            }
        }
    }

    /**
     * 创建群
     */
    private void createGroup(String targetIds,String groupName) {
        Map<String, Object> params = new HashMap<>();
        if (TextUtils.isEmpty(groupName) || G.isAllSpace(groupName)) {
            G.showToast(context,"群名字不能为空!");
            return;
        }
        params.put("groupName", groupName);
        params.put("tsIds", targetIds);
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_CREATE_GROUP, params, this);
    }

    /**
     * 修改群名称
     *
     * @param targetGroupId 群组id
     */
    private void editName(String targetGroupId,String groupName) {
        Map<String, Object> params = new HashMap<>();
        if (TextUtils.isEmpty(groupName) ||G.isAllSpace(groupName)) {
            G.showToast(context,"群名字不能为空!");
            return;
        }
        params.put("tsId", UserMessage.getInstance(context).getTsId());
      //  params.put("groupChatAdmin", UserMessage.getInstance(context).getTsId());
        params.put("groupChatId", targetGroupId);
        params.put("groupChatName", groupName);
        //更新数据库
        Type type = new TypeToken<Result<String>>() {
        }.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_EDITGROUPNAME, params, this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if (uri.equals(Apiurl.MESSAGE_CREATE_GROUP)) {
            Result<String> data = (Result<String>) date;
            if (data != null) {
                String result = data.getData();
                GroupsDbHelper helper = GroupsDbHelper.getinstance();
                SQLiteDatabase database = new GroupDb(context).getWritableDatabase();
                helper.insert(database, groupName, targetIds, 0, 0);
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                context.finish();
            }
        } else if (uri.equals(Apiurl.MESSAGE_EDITGROUPNAME)) {
            Result<String> data = (Result<String>) date;
            if (data != null) {
                String result = data.getData();
                G.showToast(context, result);
                alertDialog.dismiss();
                if (context instanceof GroupDetailActivity) {
                    GroupDetailActivity activity = (GroupDetailActivity) context;
                    activity.setTargetGroupName(groupName);
                    //修改群名称后通知前面一个对话框页面页修改名称
                   /* Intent intent  = new Intent(BroadcastConstant.EDITGROUPNAME);
                    intent.putExtra("groupName",groupName);
                    activity.sendBroadcast(intent);*/
                    ConservationActivity.name= groupName;
                }
            }
        }
    }

    @Override
    public void onError(String uri, Result error) {
        DetaiCodeUtil.errorDetail(error, context);
        alertDialog.dismiss();
    }

}

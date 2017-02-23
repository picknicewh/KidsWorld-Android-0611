package net.hongzhang.message.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.contract.GroupDb;
import net.hongzhang.baselibrary.contract.GroupsDbHelper;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.NoScrollGirdView;
import net.hongzhang.message.R;
import net.hongzhang.message.adapter.MemberAdapter;
import net.hongzhang.message.bean.GroupMemberVo;
import net.hongzhang.message.bean.GroupMemberVos;
import net.hongzhang.message.widget.OperationGroupDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * 作者： wh
 * 时间： 2016/9/9
 * 名称：群组详情
 * 版本说明：3.0.2
 * 附加注释：
 * 主要接口：查看群成员
 */
public class GroupDetailActivity extends BaseActivity implements OkHttpListener ,View.OnClickListener{
    public  static  final  int EDIT_NMAE = 1;
    public  static  final  int ENTEREDIT = 2;
    /**
     * 显示成员
     */
    private NoScrollGirdView girdView;
    /**
     * 适配器
     */
    private MemberAdapter adapter;
    /**
     * 群成员
     */
    private TextView tv_count;
    private RelativeLayout rl_count;
    /**
     * 群名称
     */
    private TextView tv_name;
    /**
     * 免打扰切换按钮
     */
    private ToggleButton tg_nodisturb;
    /**
     * 顶置聊天
     */
    private ToggleButton tg_Overhead;

    /**
     * 清空聊天记录
     */
    private TextView tv_clean;
    /**
     *删除并退出
     */
    private Button btn_exit;
    /**
     * 群名称
     */
    private LinearLayout ll_name;
    /**
     * 免打扰
     */
    private RelativeLayout rl_nodisturb;
    /**
     * 顶置聊天
     */
    private RelativeLayout rl_overhead;
    /**
     * 群id
     */
    private String targetGroupId;
    /**
     * 群名称
     */
    private String targetGroupName;
    /**
     * 群主id
     */
    private   String  ganapatiId;
    /**
     *
     */
    private ImageView iv_name_arrow;
    private GroupsDbHelper dbHelper;
    private SQLiteDatabase db;
    private SharedPreferences spf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_groupdetail);

        initView();
    }
    /**
     * 初始化按钮
     */
    private void initView(){
        girdView = $(R.id.gv_gdetail);
        tg_nodisturb = $(R.id.tg_nodiscribe);
        tg_Overhead = $(R.id.tg_overhead);
        tv_clean = $(R.id.tv_clean);
        btn_exit = $(R.id.btn_exit);
        tv_name = $(R.id.tv_mname);
        tv_count = $(R.id.tv_mcount);
        ll_name = $(R.id.ll_mname);
        rl_count = $(R.id.rl_count);
        iv_name_arrow = $(R.id.iv_name_arrow);
        rl_nodisturb = $(R.id.rl_nodiscribe);
        rl_overhead = $(R.id.rl_overhead);
        tg_Overhead.setOnClickListener(this);
        tg_nodisturb.setOnClickListener(this);
        tv_clean.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        ll_name.setOnClickListener(this);
        rl_overhead.setOnClickListener(this);
        rl_nodisturb.setOnClickListener(this);
        rl_count.setOnClickListener(this);
        spf=getSharedPreferences("name", Context.MODE_PRIVATE);
       // targetGroupId =  spf.getString("targetGroupId","");
        targetGroupId = getIntent().getStringExtra("targetGroupId");
        setCheck(targetGroupId);
        setGroupInfo();
    }
    /**
     * 设置ToggleButton选中的状态
     * @param targetGroupId 群组id
     */
    private void setCheck(String targetGroupId){
        dbHelper = new GroupsDbHelper();
        db = new GroupDb(this).getWritableDatabase();
        tg_nodisturb.setChecked(dbHelper.getStatus(db,targetGroupId));
        tg_Overhead.setChecked(dbHelper.getTop(db,targetGroupId));
    }
    /**
     * 获取并设置当前群组的id号和姓名
     */
    private void setGroupInfo(){
        targetGroupName = spf.getString("groupName","");
        tv_name.setText(targetGroupName);
    }
    /**
     * 获取所有群成员
     * @param  targetGroupId
     */
    private  void getMemberList(String targetGroupId){
        Map<String,Object> params = new HashMap<>();
        params.put("groupChatId",targetGroupId);
        Type type =new TypeToken<Result<GroupMemberVos>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_SEARCH_MEMBER,params,this);
        showLoadingDialog();
    }
    @Override
    protected void setToolBar() {
          setLiftImage(R.mipmap.ic_arrow_lift);
          setLiftOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });
    }
    @Override
    public void onSuccess(String uri, Object date) {
        if (uri.contains(Apiurl.MESSAGE_SEARCH_MEMBER)){
            stopLoadingDialog();
            Result<GroupMemberVos> data = (Result<GroupMemberVos>) date;
            if (data!=null){
                GroupMemberVos  groupMemberVos = data.getData();
                if (groupMemberVos!=null){
                    ganapatiId = groupMemberVos.getGanapatiId();
                    if (groupMemberVos.getFixed()==0){
                        btn_exit.setVisibility(View.GONE);
                    }else {
                        btn_exit.setVisibility(View.VISIBLE);
                    }
                    List<GroupMemberVo> groupMemberVoList = groupMemberVos.getMemberList();
                    setCententTitle("群信息"+"("+groupMemberVoList.size()+")");
                    tv_count.setText("全部群成员("+groupMemberVoList.size()+")");
                    if (isganapati(ganapatiId)){
                        groupMemberVoList.addAll(getLast());
                        btn_exit.setText("解散群组");
                        iv_name_arrow.setVisibility(View.VISIBLE);
                    }else {
                        btn_exit.setText("删除并退出");
                        iv_name_arrow.setVisibility(View.GONE);
                    }
                    setGirdView(groupMemberVoList);
                }
            }
        }
    }
    /**
     * 设置列表显示
     * @param  groupMemberVoList 数据
     */
   private void setGirdView(final List<GroupMemberVo> groupMemberVoList){
      final List<GroupMemberVo> groupMemberVoList2 = new ArrayList<>();
       for (int i = 0 ; i <groupMemberVoList.size();i++){
           if (i<40){
               groupMemberVoList2.add(i,groupMemberVoList.get(i));
           }
       }
       adapter = new MemberAdapter(this,groupMemberVoList2,targetGroupId,targetGroupName);
       girdView.setAdapter(adapter);
       girdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               final GroupMemberVo groupMemberVo = groupMemberVoList2.get(i);
               if (RongIM.getInstance()!=null){
                   RongIM.getInstance().startPrivateChat(GroupDetailActivity.this,groupMemberVo.getTs_id(),groupMemberVo.getTs_name());
                   RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                       @Override
                       public UserInfo getUserInfo(String userId) {
                           if(null!=groupMemberVo.getImgUrl()){
                               return  new UserInfo(groupMemberVo.getTs_id(),groupMemberVo.getTs_name(), Uri.parse(groupMemberVo.getImgUrl()));
                           }
                           return null;
                       }
                   }, true);
                   if(null!=groupMemberVo.getImgUrl()){
                       RongIM.getInstance().refreshUserInfoCache(new UserInfo(groupMemberVo.getTs_id(), groupMemberVo.getTs_name(), Uri.parse(groupMemberVo.getImgUrl())));
                   }
               }
           }
       });
   }
    @Override
    protected void onResume() {
        super.onResume();
        setGroupInfo();
        getMemberList(targetGroupId);
    }

    /**
     * 判断是否是群组
     * @param  ganapatiId 群主id
     */
    private boolean isganapati(String ganapatiId){
        if (UserMessage.getInstance(this).getTsId().equals(ganapatiId)){
            return true;
        }else {
            return false;
        }
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }
    /**
     * 获取最后两个添加和删除
     */
    private  List<GroupMemberVo> getLast(){
        List<GroupMemberVo> memberJsons = new ArrayList<>();
        GroupMemberVo add = new GroupMemberVo();
        add.setTs_id("add");
        memberJsons.add(add);
        GroupMemberVo del = new GroupMemberVo();
        del.setTs_id("del");
        memberJsons.add(del);
        return memberJsons;
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId==R.id.tg_nodiscribe){
            if (tg_nodisturb.isChecked()){
                setStatus(Conversation.ConversationNotificationStatus.DO_NOT_DISTURB);
                dbHelper.updateIsStatus(db,1,targetGroupId);
            }else {
                setStatus(Conversation.ConversationNotificationStatus.NOTIFY);
                dbHelper.updateIsStatus(db,0,targetGroupId);
            }
        }else if (viewId==R.id.tg_overhead){
            if (tg_Overhead.isChecked()){
                setTopConversation(true);
                dbHelper.updateIsTop(db,1,targetGroupId);
            }else {
                setTopConversation(false);
                dbHelper.updateIsTop(db,0,targetGroupId);
            }
        }else if (viewId==R.id.btn_exit){
            OperationGroupDialog dialog;
            if (isganapati(ganapatiId)){//解散群
                 dialog = new OperationGroupDialog(this,UserMessage.getInstance(this).getTsId(),
                        targetGroupId, OperationGroupDialog.FLAG_DISSOLVE);
            }else {//退出群
                dialog = new OperationGroupDialog(this,UserMessage.getInstance(this).getTsId(),
                        targetGroupId,targetGroupName, OperationGroupDialog.FLAG_REMOVE);
            }
            dialog.initView();
        }else if (viewId==R.id.tv_clean) {
            //清空消息
            OperationGroupDialog dialog = new OperationGroupDialog(this,targetGroupId, OperationGroupDialog.FLAG_CLEAN);
            dialog.initView();
        }else if (viewId==R.id.ll_mname){
            if (isganapati(ganapatiId)) {
                Intent intent = new Intent();
                intent.setClass(this,ModifyNameActivity.class);
               //intent.putExtra("targetGroupId",targetGroupId);
                //intent.putExtra("targetGroupName",targetGroupName);
                startActivityForResult(intent,ENTEREDIT);
            }
        }else if (viewId==R.id.rl_nodiscribe){
            tg_nodisturb.setChecked(!tg_nodisturb.isChecked());
            if (tg_nodisturb.isChecked()){
                setStatus(Conversation.ConversationNotificationStatus.DO_NOT_DISTURB);
                dbHelper.updateIsStatus(db,1,targetGroupId);
            }else {
                setStatus(Conversation.ConversationNotificationStatus.NOTIFY);
                dbHelper.updateIsStatus(db,0,targetGroupId);
            }
        }else if (viewId==R.id.rl_overhead){
            tg_Overhead.setChecked(!tg_Overhead.isChecked());
            if (tg_Overhead.isChecked()){
                setTopConversation(true);
                dbHelper.updateIsTop(db,1,targetGroupId);
            }else {
                setTopConversation(false);
                dbHelper.updateIsTop(db,0,targetGroupId);
            }
        }else if (viewId==R.id.rl_count){
            Intent intent = new Intent();
            intent.setClass(this, GroupMemberDetailActivity.class);
            intent.putExtra("targetGroupId",targetGroupId);
            intent.putExtra("targetGroupName",targetGroupName);
            startActivity(intent);
        }
    }
    /**
     * 设置消息免打扰
     * @param  status 消息免打扰模式
     */
   private void setStatus(Conversation.ConversationNotificationStatus status){
       if (RongIM.getInstance()!=null){
         RongIM.getInstance().setConversationNotificationStatus(Conversation.ConversationType.GROUP, targetGroupId,status, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
          @Override
          public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
              if (conversationNotificationStatus.getValue()==1){
                  Toast.makeText(getApplicationContext(),"取消免打扰模式！",Toast.LENGTH_SHORT).show();
              }else {
                  Toast.makeText(getApplicationContext(),"设置免打扰模式！",Toast.LENGTH_SHORT).show();
              }
          }
          @Override
          public void onError(RongIMClient.ErrorCode errorCode) {
              Toast.makeText(getApplicationContext(),errorCode.getMessage(),Toast.LENGTH_SHORT).show();
          }
         });
       }
  }
    /**
     * 设置消息顶置
     * @param isTop 是否置顶
     */
    private void setTopConversation(final boolean isTop) {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().setConversationToTop(Conversation.ConversationType.GROUP, targetGroupId, isTop, new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean issuccess) {
                    if (issuccess){
                        if (isTop){
                            Toast.makeText(getApplicationContext(),"置顶该会话！",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),"取消置顶！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Toast.makeText(getApplicationContext(),errorCode.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

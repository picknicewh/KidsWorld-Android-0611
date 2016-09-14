package net.hunme.message.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.baselibrary.widget.NoScrollGirdView;
import net.hunme.message.R;
import net.hunme.message.adapter.MemberAdapter;
import net.hunme.message.bean.GroupMemberVo;
import net.hunme.message.bean.GroupMemberVos;
import net.hunme.message.widget.OperationGroupDialog;

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
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class GroupDetailActivity extends BaseActivity implements OkHttpListener ,View.OnClickListener{
    public  static  final  int EDIT_NMAE = 1;
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

    private SharedPreferences spf;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_groupdetail);
        initView();
    }
    private void initView(){
        girdView = $(R.id.gv_gdetail);
        tg_nodisturb = $(R.id.tg_nodiscribe);
        tg_Overhead = $(R.id.tg_overhead);
        tv_clean = $(R.id.tv_clean);
        btn_exit = $(R.id.btn_exit);
        tv_name = $(R.id.tv_mname);
        tv_count = $(R.id.tv_mcount);
        ll_name = $(R.id.ll_mname);
        rl_nodisturb = $(R.id.rl_nodiscribe);
        rl_overhead = $(R.id.rl_overhead);
        targetGroupId = getIntent().getStringExtra("targetGroupId");
        targetGroupName =  getIntent().getStringExtra("title");
        tv_name.setText(targetGroupName);
        spf=getSharedPreferences("group", Context.MODE_PRIVATE);
        editor=spf.edit();
        tg_nodisturb.setChecked(spf.getBoolean("isStatus",false));
        tg_Overhead.setChecked(spf.getBoolean("isTop",false));
        tg_Overhead.setOnClickListener(this);
        tg_nodisturb.setOnClickListener(this);
        tv_clean.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        ll_name.setOnClickListener(this);
        rl_overhead.setOnClickListener(this);
        rl_nodisturb.setOnClickListener(this);
        tv_count.setOnClickListener(this);
        getMemberList(targetGroupId);
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
                    }
                    setGirdView(groupMemberVoList);
                    if (groupMemberVoList.size()>40){
                        tv_count.setClickable(true);
                    }else {
                        tv_count.setClickable(false);
                    }
                }
            }
        }
    }
    /**
     * 设置列表显示
     * @param  groupMemberVoList 数据
     */
   private void setGirdView(final List<GroupMemberVo> groupMemberVoList){
       adapter = new MemberAdapter(this,groupMemberVoList,targetGroupId,targetGroupName);
       girdView.setAdapter(adapter);
       girdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               final GroupMemberVo groupMemberVo = groupMemberVoList.get(i);
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
                editor.putBoolean("isStatus",true);
            }else {
                setStatus(Conversation.ConversationNotificationStatus.NOTIFY);
                editor.putBoolean("isStatus",false);
            }
            editor.commit();
        }else if (viewId==R.id.tg_overhead){
            if (tg_Overhead.isChecked()){
                setTopConversation(true);
                editor.putBoolean("isTop",true);
            }else {
                setTopConversation(false);
                editor.putBoolean("isTop",false);
            }
            editor.commit();
        }else if (viewId==R.id.btn_exit){
            OperationGroupDialog dialog;
            if (isganapati(ganapatiId)){//解散群
                 dialog = new OperationGroupDialog(this,UserMessage.getInstance(this).getTsId(),
                        targetGroupId, OperationGroupDialog.FLAG_DISSOLVE);
            }else {//退出群
                dialog = new OperationGroupDialog(this,UserMessage.getInstance(this).getTsId(),
                        targetGroupId,targetGroupName, OperationGroupDialog.FLAG_DISSOLVE);
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
                intent.putExtra("targetGroupId",targetGroupId);
                intent.putExtra("targetGroupName",targetGroupName);
                startActivityForResult(intent,EDIT_NMAE);
              //  startParallaxSwipeBackActivty(this,intent);
                finish();
            }
        }else if (viewId==R.id.rl_nodiscribe){
            tg_nodisturb.setChecked(!tg_nodisturb.isChecked());
            if (tg_nodisturb.isChecked()){
                setStatus(Conversation.ConversationNotificationStatus.DO_NOT_DISTURB);
                editor.putBoolean("isStatus",true);
            }else {
                setStatus(Conversation.ConversationNotificationStatus.NOTIFY);
                editor.putBoolean("isStatus",false);
            }
            editor.commit();
        }else if (viewId==R.id.rl_overhead){
            tg_Overhead.setChecked(!tg_Overhead.isChecked());
            if (tg_Overhead.isChecked()){
                setTopConversation(true);
                editor.putBoolean("isTop",true);
            }else {
                setTopConversation(false);
                editor.putBoolean("isTop",false);
            }
            editor.commit();
        }else if (viewId==R.id.tv_mcount){
            Intent intent = new Intent();
            intent.setClass(this, GroupMemberDetailActivity.class);
            intent.putExtra("targetGroupId",targetGroupId);
            intent.putExtra("targetGroupName",targetGroupName);
            startParallaxSwipeBackActivty(this,intent);
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
     */
    private void setTopConversation(final boolean isTop) {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().setConversationToTop(Conversation.ConversationType.GROUP, targetGroupId, isTop, new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean issuccess) {
                    if (issuccess){
                        if (isTop){
                            Toast.makeText(getApplicationContext(),"顶置该会话！",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),"取消顶置！",Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode==EDIT_NMAE){
           targetGroupId = data.getStringExtra("targetGroupId");
           targetGroupName =  data.getStringExtra("title");
           tv_name.setText(targetGroupName);
       }
    }
}

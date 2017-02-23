package net.hongzhang.message.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.ContractInfoVo;
import net.hongzhang.baselibrary.mode.GroupInfoVo;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.message.R;
import net.hongzhang.message.adapter.ContractMemberAdapter;
import net.hongzhang.message.bean.GroupMemberVo;
import net.hongzhang.message.bean.GroupMemberVos;
import net.hongzhang.message.widget.CreateGroupDialog;
import net.hongzhang.message.widget.OperationGroupDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wh
 * 时间： 2016/9/12
 * 名称：添加联系人或删除联系人列表
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class ContractMemberActivity extends BaseActivity implements OkHttpListener,View.OnClickListener {
    private ListView lv_member;
    private ContractMemberAdapter adapter;
    /**
     * 是否选中对话框的item
     */
    private HashMap<Integer, Boolean> isSelected;
    /**
     * 用户id
     */
    private List<String> userids;
    /**
     * 群名称
     */
    private   String targetGroupName;
    /**
     * 群id
     */
    private   String targetGroupId;
    /**
     * 删除或者添加
     */
    private int type;
    /**
     * 右边内容
     */
    private TextView tv_subTitle;
    private TextView tv_nocontract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_contract);
        initListview();
    }
    private void initListview(){
         lv_member = $(R.id.lv_member);
         tv_nocontract = $(R.id.tv_nocontract);
         type = getIntent().getIntExtra("type",0);
         targetGroupId = getIntent().getStringExtra("targetGroupId");
         targetGroupName = getIntent().getStringExtra("targetGroupName");
         userids = new ArrayList<>();
         operation(type,targetGroupId);
    }
    /**
     * 相对应操作
     * @param  type type = 0 创建群 ，type = 1添加群成员，type = 2 删除群成员
     */
    private void  operation(int type,String targetGroupId){
        switch (type){
            case 0:allContract(4);break;
            case 1:if (targetGroupId!=null){withoutMember(targetGroupId);}break;
            case 2:if (targetGroupId!=null){inMember(targetGroupId);}break;
        }
    }
    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle(getIntent().getStringExtra("title"));
        setSubTitle("确定");
        tv_subTitle = $(R.id.tv_subtitle);
        tv_subTitle.setClickable(false);
        tv_subTitle.setTextColor(getResources().getColor(R.color.line_gray));
        setSubTitleOnClickListener(this);

    }
    /**
     *  获取不在群内成员
     *  @param  targetGroupId
     */
    private void withoutMember(String targetGroupId){
        Map<String,Object> params = new HashMap<>();
        params.put("groupChatId",targetGroupId);
        params.put("tsId",UserMessage.getInstance(this).getTsId());
        Type type =new TypeToken<Result<List<GroupMemberVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_WITHOUT_MEMBER,params,this);
        stopLoadingDialog();
    }
    /**
     *  获取在群内成员
     *  @param  targetGroupId
     */
    private void inMember(String targetGroupId){
        Map<String,Object> params = new HashMap<>();
        params.put("groupChatId",targetGroupId);
        Type type =new TypeToken<Result<GroupMemberVos>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_SEARCH_MEMBER,params,this);
        showLoadingDialog();
    }
    /**
     * 全部联系人
     * @param  mtype mtype =0 获取所有联系人列表
     */
    private void allContract(int mtype){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId",UserMessage.getInstance(this).getTsId());
        params.put("type",mtype);
        Type type =new TypeToken<Result<List<GroupInfoVo>>>(){}.getType();
        OkHttps.sendPost(type, Apiurl.MESSAGE_GETGTOUP,params,this);
    }
    @Override
    public void onClick(View view) {
      if (view.getId()==R.id.tv_subtitle){
          if (!tv_subTitle.getText().equals("确定")){
              tv_subTitle.setClickable(true);
              tv_subTitle.setTextColor(getResources().getColor(R.color.white));
              if (type ==0){
                  CreateGroupDialog createGroupDialog = new CreateGroupDialog(ContractMemberActivity.this,getTargets(userids));
                  createGroupDialog.initView();
              }else if (type==1){
                  OperationGroupDialog exitGroupDialog = new OperationGroupDialog(
                          ContractMemberActivity.this,getTargets(userids),targetGroupId,targetGroupName, OperationGroupDialog.FLAG_ADD);
                  exitGroupDialog.initView();
              }else if (type==2){
                  OperationGroupDialog exitGroupDialog = new OperationGroupDialog(
                          ContractMemberActivity.this,getTargets(userids),targetGroupId,targetGroupName, OperationGroupDialog.FLAG_REMOVE_MEMBER);
                  exitGroupDialog.initView();
              }
          }else {
              tv_subTitle.setClickable(false);
              tv_subTitle.setTextColor(getResources().getColor(R.color.line_gray));
          }
      }
    }
    /**
     * 列表点击事件
     */
    private  void itemclick(final List<GroupMemberVo> groupMemberVos){
        lv_member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    ContractMemberAdapter.ViewHolder vholder = (ContractMemberAdapter.ViewHolder) view.getTag();
                    vholder.checkBox.setChecked(!vholder.checkBox.isChecked());
                    adapter.getIsSelected().put(position,vholder.checkBox.isChecked());
                    isSelected = adapter.getIsSelected();
                    userids= getGroupItem(isSelected,groupMemberVos);
                    if (userids.size()>0){
                        setSubTitle("确定("+userids.size()+")");
                        tv_subTitle.setTextColor(getResources().getColor(R.color.white));
                        tv_subTitle.setClickable(true);
                    }else {
                        tv_subTitle.setClickable(false);
                        tv_subTitle.setTextColor(getResources().getColor(R.color.line_gray));
                        setSubTitle("确定");
                    }
            }
        });
    }
    /**
     * 获取选中的列表
     */
    private List<String> getGroupItem(HashMap<Integer, Boolean> isSelected, List<GroupMemberVo> memberJsons) {
        List<String> userids = new ArrayList<>();
        for (int i = 0; i < isSelected.size(); i++) {
            boolean ischeck = isSelected.get(i);
            if (ischeck) {
                String userid =memberJsons.get(i).getTs_id();
                userids.add(userid);
            }
        }
        return userids;
    }
    /**
     * 设置listView数据信息
     * @param groupMemberVoList 数据列表
     */
    private void  setListView(List<GroupMemberVo> groupMemberVoList){
        if (groupMemberVoList.size()>0){
            tv_subTitle.setClickable(true);
            tv_nocontract.setVisibility(View.GONE);
            adapter = new ContractMemberAdapter(this,groupMemberVoList);
            lv_member.setAdapter(adapter);
            itemclick(groupMemberVoList);
            tv_subTitle.setTextColor(getResources().getColor(R.color.white));
        }else {
            tv_subTitle.setClickable(false);
            tv_subTitle.setTextColor(getResources().getColor(R.color.line_gray));
            tv_nocontract.setVisibility(View.VISIBLE);
        }

    }
    /**
     * 把 List<String> 类型tsids转换成为以逗号，分割的String类型的字符串
     * @param  userids 所有成员的id
     */
    private String getTargets(List<String> userids){
        String targetids;
        StringBuilder builder = new StringBuilder();
        if (type!=2 ){
            builder.append(UserMessage.getInstance(this).getTsId()).append(",");
        }
        for (int i = 0;i<userids.size();i++){
            String targetid=userids.get(i);
            builder.append(targetid).append(",");
        }
        if (builder.lastIndexOf(",")!=-1){
            builder.deleteCharAt(builder.lastIndexOf(","));
        }
        targetids = builder.toString();
        return targetids;

    }
    /**
     * 将ContractInfoVo转变成GroupMemberVo
     * @param groupInfoVos  联系信息列表
     * 剔除
     */
    private  List<GroupMemberVo> getGroupMemberVoList(List<GroupInfoVo>  groupInfoVos){
        List<GroupMemberVo> groupMemberVoList = new ArrayList<>();
        List<ContractInfoVo> contractInfoVos = groupInfoVos.get(0).getMenberList();
        for (int i = 0 ;i < contractInfoVos.size();i++){
            ContractInfoVo contractInfoVo = contractInfoVos.get(i);
            if (!contractInfoVo.getTsId().equals(UserMessage.getInstance(this).getTsId())){
                GroupMemberVo groupMemberVo= new GroupMemberVo();
                groupMemberVo.setTs_id(contractInfoVo.getTsId());
                groupMemberVo.setImgUrl(contractInfoVo.getImg());
                groupMemberVo.setRy_id(contractInfoVo.getRyId());
                groupMemberVo.setTs_name(contractInfoVo.getTsName());
                groupMemberVoList.add(groupMemberVo);
            }
        }
        return groupMemberVoList;
    }
    @Override
    public void onSuccess(String uri, Object date) {
        if (uri.contains(Apiurl.MESSAGE_WITHOUT_MEMBER)){
            Result<List<GroupMemberVo>> data = (Result<List<GroupMemberVo>>) date;
            if (data!=null){
                List<GroupMemberVo> groupMemberVoList = data.getData();
                setListView(groupMemberVoList);
            }
        }else if (uri.contains(Apiurl.MESSAGE_SEARCH_MEMBER)){
            Result<GroupMemberVos> data = (Result<GroupMemberVos>) date;
            if (data!=null){
                //去掉群主
                GroupMemberVos groupMemberVos = data.getData();
                String ganapatiId =groupMemberVos.getGanapatiId();
                List<GroupMemberVo> groupMemberVoList = new ArrayList<>();
                for (int i=0;i<groupMemberVos.getMemberList().size();i++){
                    GroupMemberVo vo = groupMemberVos.getMemberList().get(i);
                    if (!vo.getTs_id().equals(ganapatiId)){
                        groupMemberVoList.add(vo);
                    }
                }
                setListView(groupMemberVoList);
            }
        }else if (uri.contains(Apiurl.MESSAGE_GETGTOUP)){
            Result<List<GroupInfoVo>> data = (Result<List<GroupInfoVo>>) date;
            if (data!=null){
                List<GroupInfoVo>  groupJsonList = data.getData();
                List<GroupMemberVo> groupMemberVoList = getGroupMemberVoList(groupJsonList);
                setListView(groupMemberVoList);
            }
        }else if (uri.contains(Apiurl.MESSAGE_CREATE_GROUP)){
            Result<String> data = (Result<String>) date;
            if (data!=null){
                String result = data.getData();
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            }

        }
        stopLoadingDialog();
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }
}

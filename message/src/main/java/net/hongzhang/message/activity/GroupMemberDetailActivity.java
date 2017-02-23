package net.hongzhang.message.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseActivity;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.message.R;
import net.hongzhang.message.adapter.MemberAdapter;
import net.hongzhang.message.bean.GroupMemberVo;
import net.hongzhang.message.bean.GroupMemberVos;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

public class GroupMemberDetailActivity extends BaseActivity implements OkHttpListener {
    private GridView gv_memberDetail;
    private MemberAdapter adapter;
    /**
     * 群id
     */
    private String targetGroupId;
    /**
     * 群名称
     */
    private   String targetGroupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_detail);

        gv_memberDetail = $(R.id.gv_memberdetail);
        targetGroupId = getIntent().getStringExtra("targetGroupId");
        targetGroupName = getIntent().getStringExtra("targetGroupName");
        getMemberList(targetGroupId);
    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
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
    public void onSuccess(String uri, Object date) {
        Result<GroupMemberVos> data = (Result<GroupMemberVos>) date;
        if (data!=null){
            GroupMemberVos  groupMemberVos = data.getData();
            if (groupMemberVos!=null){
                List<GroupMemberVo> groupMemberVoList = groupMemberVos.getMemberList();
                setCententTitle("群信息"+"("+groupMemberVoList.size()+")");
                setGirdView(groupMemberVoList);
            }
        }
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }
    /**
     * 设置列表显示
     * @param  groupMemberVoList 数据
     */
    private void setGirdView(final List<GroupMemberVo> groupMemberVoList){
        adapter = new MemberAdapter(this,groupMemberVoList,targetGroupId,targetGroupName);
        gv_memberDetail.setAdapter(adapter);
        gv_memberDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final GroupMemberVo groupMemberVo = groupMemberVoList.get(i);
                if (RongIM.getInstance()!=null){
                    RongIM.getInstance().startPrivateChat(GroupMemberDetailActivity.this,groupMemberVo.getTs_id(),groupMemberVo.getTs_name());
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
}

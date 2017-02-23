package net.hongzhang.message.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hongzhang.baselibrary.base.BaseFragement;
import net.hongzhang.baselibrary.mode.GroupInfoVo;
import net.hongzhang.baselibrary.mode.Result;
import net.hongzhang.baselibrary.network.Apiurl;
import net.hongzhang.baselibrary.network.OkHttpListener;
import net.hongzhang.baselibrary.network.OkHttps;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.message.R;
import net.hongzhang.message.activity.ContractMemberActivity;
import net.hongzhang.message.adapter.ClassAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;

/**
 * 作者： wh
 * 时间： 2016/7/15
 * 名称：通讯首页--班级
 * 版本说明：
 * 附加注释：通过服务端传递过来的班级列表，进行群聊
 * 主要接口：
 */
public class GroupListFragment extends BaseFragement implements OkHttpListener,View.OnClickListener{
    /**
     * 班级列表
     */
    private ListView lv_class;
    /**
     * 适配器
     */
    private ClassAdapter adapter;
    private SharedPreferences spf;
    private SharedPreferences.Editor editor;
    private   List<GroupInfoVo> groupInfoVos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grouplist, null);
        init(view);
        return view;
    }
   private void  init(View view){
       spf=getActivity().getSharedPreferences("name", Context.MODE_PRIVATE);
       editor=spf.edit();
       lv_class = $(view,R.id.lv_class);
       groupInfoVos  = new ArrayList<>();
       getClassinfor();
   }
    private void setlistview(final List<GroupInfoVo> groupJsons){
        adapter = new ClassAdapter(getActivity(),groupJsons);
        lv_class.setAdapter(adapter);
        lv_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                GroupInfoVo groupJson = groupJsons.get(i);
                final String classId = groupJson.getClassId();
                final String groupName = groupJson.getGroupName();
                editor.putString("groupName",groupName);
                editor.putString("targetGroupId",classId);
                editor.commit();
                if (RongIM.getInstance()!=null){
                    RongIM.getInstance().startGroupChat(getActivity(),classId,groupName);
                    RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
                        @Override
                        public Group getGroupInfo(String s) {
                            if (s.equals(classId)){
                                Group group = new Group(classId,groupName, Uri.parse(""));
                                return group;
                            }
                            return null;
                        }
                    },true);

                    if(!G.isEmteny(classId)&&!G.isEmteny(groupName)){
                        RongIM.getInstance().refreshGroupInfoCache(new Group(classId,groupName, Uri.parse("")));
                    }
                   // RongIM.getInstance().refreshGroupUserInfoCache(new GroupUserInfo(classId,groupName,groupName));
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (groupInfoVos.size()>0){
            getClassinfor();
        }
        Log.i("aaaaaaaa","===========onResume=============");
    }
    /**
     * 获取所有班级信息
     */
    private  void getClassinfor(){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(getActivity()).getTsId());
        //1=群，2=老师，3=家长
        params.put("type",1);
        Type type =new TypeToken<Result<List<GroupInfoVo>>>(){}.getType();
        OkHttps.sendPost(type,Apiurl.MESSAGE_GETGTOUP,params,this,2,"contract_class");
    }
    @Override
    public void onSuccess(String uri, Object date) {
        Result<List<GroupInfoVo>> data = (Result<List<GroupInfoVo>>) date;
        if (data!=null){
             groupInfoVos =data.getData();
            if (groupInfoVos!=null||groupInfoVos.size()!=0){
                setlistview(groupInfoVos);
            }
        }
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.iv_right){
            Intent intent = new Intent();
            intent.setClass(getActivity(), ContractMemberActivity.class);
            intent.putExtra("title","创建群聊");
            intent.putExtra("type",0);
            startActivity(intent);
        }else if (view.getId()==R.id.iv_left){
            getActivity().finish();
        }
    }
}

package net.hunme.message.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.Apiurl;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.message.R;
import net.hunme.message.adapter.ClassAdapter;
import net.hunme.message.bean.GroupInfoVo;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
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
public class ClassActivity extends BaseActivity implements OkHttpListener,View.OnClickListener{
    /**
     * 班级列表
     */
    private ListView lv_class;
    /**
     * 适配器
     */
    private ClassAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        getClassinfor();
    }

    @Override
    protected void setToolBar() {
        setCententTitle("班级");
         setLiftOnClickClose();
        setLiftImage(R.mipmap.ic_arrow_lift);
        setRightImage(R.mipmap.ic_add);
        setRightOnClickListener(this);
    }

    private void setlistview(final List<GroupInfoVo> groupJsons){
        lv_class = $(R.id.lv_class);
        adapter = new ClassAdapter(this,groupJsons);
        lv_class.setAdapter(adapter);
        lv_class.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                GroupInfoVo groupJson = groupJsons.get(i);
                Log.i("TAF","groupName:"+groupJson.getGroupName());
                final String classId = groupJson.getClassId();
                final String groupName = groupJson.getGroupName();
                if (RongIM.getInstance()!=null){
                    RongIM.getInstance().startGroupChat(ClassActivity.this,classId,groupName);
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
                    RongIM.getInstance().refreshGroupInfoCache(new Group(classId,groupName, Uri.parse("")));
                   // RongIM.getInstance().refreshGroupUserInfoCache(new GroupUserInfo(classId,groupName,groupName));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getClassinfor();
    }

    /**
     * 获取所有班级信息
     */
    private  void getClassinfor(){
        Map<String,Object> params = new HashMap<>();
        params.put("tsId", UserMessage.getInstance(this).getTsId());
        //1=群，2=老师，3=家长
        params.put("type",1);
        Type type =new TypeToken<Result<List<GroupInfoVo>>>(){}.getType();
        OkHttps.sendPost(type,Apiurl.MESSAGE_GETGTOUP,params,this,2,"contract_class");
    }
    @Override
    public void onSuccess(String uri, Object date) {
        Result<List<GroupInfoVo>> data = (Result<List<GroupInfoVo>>) date;
        if (data!=null){
            List<GroupInfoVo> groupJsonList =data.getData();
            if (groupJsonList!=null||groupJsonList.size()!=0){
                setlistview(groupJsonList);
            }
        }
    }
    @Override
    public void onError(String uri, String error) {
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.iv_right){
            Intent intent = new Intent();
            intent.setClass(this, ContractMemberActivity.class);
            intent.putExtra("title","创建群聊");
            intent.putExtra("type",0);
            startParallaxSwipeBackActivty(this,intent);
        }
    }
}

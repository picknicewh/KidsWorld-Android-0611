package net.hunme.login;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.hunme.baselibrary.base.BaseActivity;
import net.hunme.baselibrary.mode.Result;
import net.hunme.baselibrary.network.OkHttpListener;
import net.hunme.baselibrary.network.OkHttps;
import net.hunme.baselibrary.util.G;
import net.hunme.baselibrary.util.UserMessage;
import net.hunme.login.mode.CharacterSeleteVo;
import net.hunme.login.util.UserAction;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserChooseActivity extends BaseActivity implements OkHttpListener {
    private ListView lv_user_choose;
    private UserChooseAdapter adapter;
    private UserMessage um;
    private List<CharacterSeleteVo> seleteList;
    private final String SELECTUSER="/app/selectUser.do";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_choose);
        initView();
    }
    private void initView(){
        um=UserMessage.getInstance(this);
        //拿到用户信息再次解析
        Type type =new TypeToken<List<CharacterSeleteVo>>(){}.getType();
        seleteList= new Gson().fromJson(um.getUserMessageJsonCache(),type);
        lv_user_choose=$(R.id.lv_user_choose);
        if (seleteList.size()!=0){
            adapter=new UserChooseAdapter(this,seleteList);
            lv_user_choose.setAdapter(adapter);
            lv_user_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    adapter.setSelectPosition(i);
                    CharacterSeleteVo data=seleteList.get(i);
                    //通过用户选择身份保存用户信息
                    UserAction.saveUserMessage(UserChooseActivity.this,data.getName(),
                            data.getImg(),data.getClassName(),data.getSchoolName(),
                            data.getRyId(),data.getTsId(),data.getType());
                    selectUserSubmit(data.getTsId());
                    finish();
                }
            });
        }

    }

    @Override
    protected void setToolBar() {
        setLiftImage(R.mipmap.ic_arrow_lift);
        setLiftOnClickClose();
        setCententTitle("选择账号");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(G.isEmteny(um.getUserName())){
            // 如果用户没有点击选择默认选择第一个身份
            CharacterSeleteVo data=seleteList.get(0);
            UserAction.saveUserMessage(UserChooseActivity.this,data.getName(),
                    data.getImg(),data.getClassName(),data.getSchoolName(),
                    data.getRyId(),data.getTsId(),data.getType());
            selectUserSubmit(data.getTsId());
        }
    }

    //用户选择提交用户ID
    public void selectUserSubmit(String tsid){
        Map<String,Object>map=new HashMap<>();
        map.put("tsId",tsid);
        Type type =new TypeToken<Result<String>>(){}.getType();
        OkHttps.sendPost(type,SELECTUSER,map,this);
    }

    @Override
    public void onSuccess(String uri, Object date) {
        if(SELECTUSER.equals(uri)){
            Result<String> result= (Result<String>) date;
            G.log(result.getData());
        }
    }

    @Override
    public void onError(String uri, String error) {
        G.log(uri);
    }
}
